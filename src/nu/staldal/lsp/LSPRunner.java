/*
 * Copyright (c) 2004, Mikael Ståldal
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
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.StreamResult;

import nu.staldal.util.Utils;
import nu.staldal.xmlutil.ContentHandlerFixer;
import nu.staldal.lsp.*;


/**
 * Execute LSP pages in a stand-alone environment.
 * Useful for testing and debugging.
 */
public class LSPRunner
{
	private final SAXParserFactory spf;
	private final SAXTransformerFactory tfactory;
	
    private Map lspPages;
    
    
	/**
	 * Output type XML.
	 */
	public static final String XML = "xml";
		
	/**
	 * Output type HTML.
	 */
	public static final String HTML = "html";
		
	/**
	 * Output type XHTML.
	 */
	public static final String XHTML = "xhtml";
		
	/**
	 * Output type TEXT.
	 */
	public static final String TEXT = "text";		

	
	public LSPRunner()
	{
		lspPages = Collections.synchronizedMap(new HashMap());
		
		spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		spf.setValidating(false);

		TransformerFactory tf = TransformerFactory.newInstance();
        if (!(tf.getFeature(SAXTransformerFactory.FEATURE)
              	&& tf.getFeature(StreamResult.FEATURE)))
        {
            throw new Error("The transformer factory "
                + tf.getClass().getName() + " doesn't support SAX");
        }            
		tfactory = (SAXTransformerFactory)tf;
	}


	/**
	 * The method executes an LSP page and write the result to an 
	 * {@link java.io.OutputStream}. 
	 *
 	 * @param thePage     the LSP page
	 * @param lspParams   parameters to the LSP page
	 * @param out         the {@link java.io.OutputStream}
	 * @param outputType  how to serialize the page; XML, HTML, XHTML or TEXT
	 * @param doctypePublic the XML DOCTYPE PUBLIC (<code>null</code> for default)
	 * @param doctypeSystem the XML DOCTYPE SYSTEM (<code>null</code> for default)
	 * @param encoding    character encoding to use (<code>null</code> for default)
     *
     * @throws SAXException  if any error occurs while executing the page
     * @throws IOException   if any I/O error occurs while executing the page
	 */	
	public void executePage(LSPPage thePage, Map lspParams, 
							OutputStream out, String outputType,
							String doctypePublic, String doctypeSystem,
                            String encoding)
		throws SAXException, IOException
	{
		ContentHandler sax;						
		try {
			TransformerHandler th = tfactory.newTransformerHandler();
			th.setResult(new StreamResult(out));
		
			Transformer trans = th.getTransformer();
			
			Properties outputProperties = new Properties();
			outputProperties.setProperty(OutputKeys.METHOD, outputType);				
			if (outputType.equals(HTML))
			{
                outputProperties.setProperty(OutputKeys.ENCODING, "iso-8859-1"); 
				outputProperties.setProperty(OutputKeys.DOCTYPE_PUBLIC,
					"-//W3C//DTD HTML 4.01 Transitional//EN");
				outputProperties.setProperty(OutputKeys.DOCTYPE_SYSTEM,
						"http://www.w3.org/TR/html4/loose.dtd");				
			}
			else if (outputType.equals(XHTML))
			{
                outputProperties.setProperty(OutputKeys.ENCODING, "iso-8859-1"); 
				outputProperties.setProperty(OutputKeys.DOCTYPE_PUBLIC,
					"-//W3C//DTD XHTML 1.0 Transitional//EN");
				outputProperties.setProperty(OutputKeys.DOCTYPE_SYSTEM,
					"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
			}
			else if (outputType.equals(XML))
			{
                outputProperties.setProperty(OutputKeys.ENCODING, "UTF-8"); 
			}
			else if (outputType.equals(TEXT))
			{
                outputProperties.setProperty(OutputKeys.ENCODING, "iso-8859-1"); 
			}
            
			if (doctypePublic != null)
				outputProperties.setProperty(OutputKeys.DOCTYPE_PUBLIC,
					doctypePublic);
			if (doctypeSystem != null)
				outputProperties.setProperty(OutputKeys.DOCTYPE_SYSTEM,
					doctypeSystem);
			if (encoding != null)
				outputProperties.setProperty(OutputKeys.ENCODING,
					encoding);
                    
			trans.setOutputProperties(outputProperties);
			
			boolean isHtml = outputType.equals(HTML);
				
			sax = new ContentHandlerFixer(th, !isHtml, isHtml);
		}
		catch (TransformerConfigurationException e)
		{
			throw new SAXException(e.getMessage());
		}
					
		sax.startDocument();
		thePage.execute(sax, lspParams, null);
		sax.endDocument();
    }
	

	/**
	 * Get the {@link nu.staldal.lsp.LSPPage} instance for a given page name.
	 *
	 * @param pageName  the name of the LSP page
	 *
	 * @return <code>null</code> if the given page name is not found
	 */
	public LSPPage getPage(String pageName)
        throws InstantiationException, IllegalAccessException 
	{
		LSPPage page = (LSPPage)lspPages.get(pageName);
		
		if (page == null)
		{
			page = loadPage(pageName);
		}
		
		return page;
	}
	
	
	/**
	 * @return <code>null</code> if not found.
	 */
	private LSPPage loadPage(String pageName)
        throws InstantiationException, IllegalAccessException 
	{
		try {
			Class pageClass = Class.forName("_LSP_"+pageName);

			LSPPage page = (LSPPage)pageClass.newInstance();
			
			lspPages.put(pageName, page);			
			
		  	return page;
		}
		catch (ClassNotFoundException e)
		{
			return null;
		}				
	}

    
    public static void main(String[] args)
        throws InstantiationException, IllegalAccessException, 
            SAXException, IOException 
    {
        if (args.length < 2)
        {
            System.err.println("LSP runtime version " + LSPPage.LSP_VERSION_NAME);
            System.err.println("Syntax: LSPRunner <pageName> <outFile>");
            System.err.println("Use \"-\" as <outFile> for standard output");
            return;
        }
        
        LSPRunner runner = new LSPRunner();
        
        LSPPage page = runner.getPage(args[0]);
        if (page == null)
        {
            System.err.println("LSP page " + args[0] + " not found");
            return;
        }
        
        OutputStream out = args[1].equals("-") 
            ? (OutputStream)System.out 
            : (OutputStream)(new FileOutputStream(args[1]));
        
        runner.executePage(page, new HashMap(0), 
						   out, XML, null, null, null);
                           
        out.close();
    }

}
