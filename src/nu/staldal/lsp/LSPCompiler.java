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

import java.io.*;
import java.util.*;

import org.xml.sax.*;

import nu.staldal.xtree.*;

// *** Check for {...} in <lsp:include>, declare as executeDynamic if found.

public class LSPCompiler
{
    private static final String LSP_CORE_NS = "http://staldal.nu/LSP/core";
    private static final String XML_NS = "http://www.w3.org/XML/1998/namespace";

    private XMLReader saxParser;
    private TreeBuilder tb;
    private URLResolver resolver;

    private Hashtable importedFiles;
    private Vector includedFiles;
    private boolean compileDynamic;
    private boolean executeDynamic;

    public LSPCompiler()
        throws LSPException
    {
        try {
			saxParser =
				new org.apache.xerces.parsers.SAXParser();

        	saxParser.setFeature(
				"http://xml.org/sax/features/validation",
        	    false);
        	saxParser.setFeature(
				"http://xml.org/sax/features/external-general-entities",
        	    true);
// 			saxParser.setFeature(
//				"http://xml.org/sax/features/external-parameter-entities",
//      	     false); // not supported by Xerces
	        saxParser.setFeature(
				"http://xml.org/sax/features/namespaces",
	            true);

            tb = null;
        }
        catch (SAXException e)
        {
            throw new LSPException(e.getMessage());
        }
    }

	private void resetParser() throws SAXException
	{
		try {
			((org.apache.xerces.parsers.SAXParser)saxParser).reset();
		}
		catch (SAXException e)
		{
			throw e;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SAXException(e);
		}
	}

    public ContentHandler startCompile(URLResolver r)
    {
    	importedFiles = new Hashtable();
	    includedFiles = new Vector();
        compileDynamic = false;
        executeDynamic = false;

        resolver = r;

        tb = new TreeBuilder();
        return tb;
    }

    public LSPPage finishCompile()
    	throws SAXException, IOException
    {
        if (tb == null) throw new IllegalStateException(
            "startCompile() must be invoked before finishCompile()");

        Element tree = tb.getTree();

        compileElement(tree);

        tb = null;

        return new LSPInterpreter(tree, importedFiles, includedFiles,
            compileDynamic, executeDynamic);
    }

    private void compileElement(Element el)
    	throws SAXException, IOException
    {
		for (int i = 0; i < el.numberOfChildren(); i++)
		{
			if (!(el.getChild(i) instanceof Element)) continue;

			Element child = (Element)el.getChild(i);

			if ((child.getNamespaceURI() != null)
					&& child.getNamespaceURI().equals(LSP_CORE_NS))
			{
				// Dispatch LSP command
				if (child.getLocalName().equals("import"))
				{
					String url = LSPUtil.getAttr("file", child, true);
                    if (LSPUtil.absoluteURL(url))
                    {
                        compileDynamic = true;
                    }
                    else
                    {
                        if (importedFiles.put(url, url) != null)
                        {
                            // *** check for circular import
                        }
                    }

                    InputSource inputSource = resolver.resolve(url);

					resetParser();

					TreeBuilder tb = new TreeBuilder(child.getBaseURI());
					saxParser.setContentHandler(tb);
					saxParser.parse(inputSource);
					Element importedDoc = tb.getTree();

					el.replaceChild(importedDoc, i);
					compileElement(importedDoc);
				}
				else if (child.getLocalName().equals("root"))
				{
					compileElement(child);
				}
				else if (child.getLocalName().equals("processing-instruction"))
				{
					LSPUtil.getAttr("name", child, true);
					compileElement(child);
				}
				else if (child.getLocalName().equals("include"))
				{
					String url = LSPUtil.getAttr("file", child, true);

                    if (LSPUtil.absoluteURL(url))
                    {
                        executeDynamic = true;
                    }
                    else
                    {
						includedFiles.addElement(url);
                    }
				}
                else if (child.getLocalName().equals("if"))
				{
					LSPUtil.getAttr("test", child, true);
					compileElement(child);
				}
				else if (child.getLocalName().equals("raw"))
				{
					compileElement(child);
				}
				else
				{
					throw new LSPException("unrecognized LSP command: "
							+ child.getLocalName());
				}
			}
			else
			{
				compileElement(child);
			}
		}
	}

}
