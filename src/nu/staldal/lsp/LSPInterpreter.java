/*
 * Copyright (c) 2001, Mikael Ståldal
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the author nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * Note: This is known as "the modified BSD license". It's an approved
 * Open Source and Free Software license, see
 * http://www.opensource.org/licenses/
 * and
 * http://www.gnu.org/philosophy/license-list.html
 */

package nu.staldal.lsp;

import java.io.IOException;
import java.util.*;

import org.xml.sax.*;

import nu.staldal.xtree.*;

public class LSPInterpreter implements LSPPage
{
    static final long serialVersionUID = -1168364109491726217L;

    private static final String LSP_CORE_NS = "http://staldal.nu/LSP/core";
    private static final String XML_NS = "http://www.w3.org/XML/1998/namespace";

    private long timeCompiled;
    private Element theTree;
    private Hashtable importedFiles;
    private Vector includedFiles;
    private boolean compileDynamic;
    private boolean executeDynamic;

    private transient URLResolver resolver = null;
    private transient Hashtable params = null;
    private transient int raw = 0;

    public LSPInterpreter(Element theTree, Hashtable importedFiles,
        Vector includedFiles, boolean compileDynamic, boolean executeDynamic)
        throws LSPException
    {
        this.timeCompiled = System.currentTimeMillis();
        this.theTree = theTree;
        this.importedFiles = importedFiles;
        this.includedFiles = includedFiles;
        this.compileDynamic = compileDynamic;
        this.executeDynamic = executeDynamic;
    }


    public Enumeration getCompileDependentFiles()
    {
        return importedFiles.keys();
    }

    public Enumeration getExecuteDependentFiles()
    {
        return includedFiles.elements();
    }

    public boolean isCompileDynamic()
    {
        return compileDynamic;
    }

    public boolean isExecuteDynamic()
    {
        return executeDynamic;
    }

    public long getTimeCompiled()
    {
        return timeCompiled;
    }


    public void execute(ContentHandler ch, URLResolver resolver,
        Hashtable params)
        throws SAXException
    {
        this.params = params;
        this.resolver = resolver;
        processNode(theTree, ch);
        this.resolver = null;
        this.params = null;
    }


	private XMLReader createParser() throws SAXException
	{
        XMLReader saxParser =
            new org.apache.xerces.parsers.SAXParser();

        saxParser.setFeature(
            "http://xml.org/sax/features/validation",
            false);
        saxParser.setFeature(
            "http://xml.org/sax/features/external-general-entities",
            true);
        //saxParser.setFeature(
        // 	"http://xml.org/sax/features/external-parameter-entities",
        //   false); // not supported by Xerces
        saxParser.setFeature(
            "http://xml.org/sax/features/namespaces",
            true);

        return saxParser;
	}

    private String processTemplate(String template)
        throws LSPException
    {
        try {
            return TemplateProcessor.processTemplate('{', '}', '\'', '\"',
                template,
                new ExpressionEvaluator() {
                    public String eval(String expr) throws LSPException
                    {
                        return evalExprAsString(expr);
                    }
                });
        }
        catch (TemplateException e)
        {
            Exception ee = e.getException();
            if (ee != null)
                throw (LSPException)ee;
            else
                throw new LSPException("Illegal template: " + e.getMessage());
        }
    }

    private void processNode(Node node, ContentHandler sax)
        throws SAXException
    {
        if (node instanceof Element)
            processNode((Element)node, sax);
        else if (node instanceof Text)
            processNode((Text)node, sax);
        else if (node instanceof ProcessingInstruction)
            ; // processNode((ProcessingInstruction)node);
    }

	private void processChildren(Element el, ContentHandler sax)
		throws SAXException
	{
		for (int i = 0; i < el.numberOfChildren(); i++)
		{
			Node child = el.getChild(i);
			processNode(child, sax);
		}
	}


    private void processNode(Element el, ContentHandler sax)
        throws SAXException
    {
		if ((el.getNamespaceURI() != null)
				&& el.getNamespaceURI().equals(LSP_CORE_NS))
		{
			// Dispatch LSP command
			if (el.getLocalName().equals("root"))
			{
				process_root(el, sax);
			}
			else if (el.getLocalName().equals("processing-instruction"))
			{
				process_processing_instruction(el, sax);
			}
			else if (el.getLocalName().equals("include"))
			{
				process_include(el, sax);
			}
			else if (el.getLocalName().equals("if"))
			{
				process_if(el, sax);
			}
			else if (el.getLocalName().equals("raw"))
			{
				process_raw(el, sax);
			}
			// *** more to implement
			else
			{
				throw new LSPException("unrecognized LSP command "
						+ "(should have been detected by compilation): "
						+ el.getLocalName());
			}
		}
		else
		{
			// Copy element to output verbatim

			for (int i = 0; i < el.numberOfNamespaceMappings(); i++)
			{
				String[] m = el.getNamespaceMapping(i);
				if (!m[1].equals(LSP_CORE_NS))
					sax.startPrefixMapping(m[0], m[1]);
			}

			org.xml.sax.helpers.AttributesImpl saxAtts =
				new org.xml.sax.helpers.AttributesImpl();

			for (int i = 0; i < el.numberOfAttributes(); i++)
			{
				String URI = el.getAttributeNamespaceURI(i);
				String local = el.getAttributeLocalName(i);
				String type = el.getAttributeType(i);
				String value = el.getAttributeValue(i);

				String newValue = (raw > 0)
                    ? value
                    : processTemplate(value);

				saxAtts.addAttribute(URI, local, "", type, newValue);
			}
			// *** include qName
			sax.startElement(el.getNamespaceURI(), el.getLocalName(), "",
				saxAtts);

			processChildren(el, sax);

			sax.endElement(el.getNamespaceURI(), el.getLocalName(), "");

			for (int i = 0; i < el.numberOfNamespaceMappings(); i++)
			{
				String[] m = el.getNamespaceMapping(i);
				if (!m[1].equals(LSP_CORE_NS))
					sax.endPrefixMapping(m[0]);
			}
		}
    }

    private void processNode(Text text, ContentHandler sax)
        throws SAXException
    {
        String newValue = (raw > 0)
            ? text.getValue()
            : processTemplate(text.getValue());

        sax.characters(newValue.toCharArray(), 0, newValue.length());
    }


	private void process_root(Element el, ContentHandler sax)
		throws SAXException
	{
		processChildren(el, sax);
	}


	private void process_processing_instruction(Element el, ContentHandler sax)
		throws SAXException
	{
		String target = processTemplate(LSPUtil.getAttr("name", el, true));

		StringHandler sh = new StringHandler();

		processChildren(el, sh);

		sax.processingInstruction(target, sh.getBuf().toString());
	}


	private void process_include(Element el, ContentHandler sax)
		throws SAXException
	{
        String url = processTemplate(LSPUtil.getAttr("file", el, true));
		try {
            InputSource inputSource = resolver.resolve(url);

			IncludeHandler ih = new IncludeHandler(sax);

			XMLReader saxParser = createParser();
			saxParser.setContentHandler(ih);
			saxParser.setErrorHandler(ih);

			saxParser.parse(inputSource);
		}
		catch (IOException e)
		{
			throw new SAXException(e);
		}
	}


	private void process_if(Element el, ContentHandler sax)
		throws SAXException
	{
		String expr = LSPUtil.getAttr("test", el, true);

		if (evalExprAsBoolean(expr))
		{
			processChildren(el, sax);
		}
	}


	private void process_raw(Element el, ContentHandler sax)
		throws SAXException
	{
        raw++;
        processChildren(el, sax);
        raw--;
	}


	private String evalExprAsString(String expr) throws LSPException
	{
		// *** more to implement

        if (expr.charAt(0) == '$')
        {
            String value = (String)params.get(expr.substring(1));
            if (value == null)
                return "";
            else
                return value;
        }
        else
            throw new LSPException("Illegal LSP expression: " + expr);
	}


	private boolean evalExprAsBoolean(String expr) throws LSPException
	{
		// *** more to implement

        if (expr.charAt(0) == '$')
        {
            String value = (String)params.get(expr.substring(1));
            if (value == null)
                return false;
            else
                return (value.length() > 0);
        }
        else
            throw new LSPException("Illegal LSP expression: " + expr);
	}


	private double evalExprAsNumber(String expr) throws LSPException
	{
		// *** dummy implementation

		return 0.0;
	}

}
