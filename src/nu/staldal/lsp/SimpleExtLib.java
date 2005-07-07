/*
 * Copyright (c) 2005, Mikael Ståldal
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

import java.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;


/**
 * Convenience class for implementing LSP extension libraries with
 * only empty elements.
 *<p>
 * You only need to implement the {@link #handleElement handleElement} 
 * method and methods for any extension functions.
 * <p>
 * The extlib can return data in one of two ways, write to the supplied 
 * ContentHandler "out" (it must not invoke startDocument() or endDocument()), 
 * or return a string from the {@link #handleElement handleElement} method. 
 * It may not do both. 
 */
public abstract class SimpleExtLib implements LSPExtLib, ContentHandler
{		
    /**
     * Namespace URI for this extension library.
     */
    protected String myNamespaceURI;
    
    /**
     * External context, or <code>null</code> if not currently
     * executing a page.
     */
    protected Object extContext;
    
    /**
     * Current page name, or <code>null</code> if not currently
     * executing a page.
     */
    protected String pageName;

	private ContentHandler sax;

	private String currentElement;
    private Attributes currentAttributes;

    
	public void init(String namespaceURI)
		throws LSPException
	{
		this.myNamespaceURI = namespaceURI;			

		this.extContext = null;
        this.pageName = null;
	}


	public void startPage(Object extContext, String pageName)
		throws LSPException
	{
		this.extContext = extContext;
        this.pageName = pageName;
		
		sax = null;

		currentElement = null;
        currentAttributes = null;
	}
	

	public ContentHandler beforeElement(ContentHandler out)			
		throws SAXException
	{
		currentElement = null;
        currentAttributes = null;
		sax = out;
		
		return this;
	}
	
	
	public String afterElement()
		throws SAXException
	{
		try {
			if (currentElement != null)
			{
                return handleElement(currentElement, currentAttributes, sax);
			}
			else
            {
				return null;
            }
		}
		finally  {
			currentElement = null;
            currentAttributes = null;
			sax = null;
		}
	}
			

	public void endPage()
	{
		extContext = null;
        pageName = null;
	}
	
    
    /**
     * Handle an extension element.
     * 
     * @param localName  the local name of the element
     * @param atts       the attributes of the element
	 * @param out        where to write XML output
     *
     * @return  the String to replace the extension element with,
     *          or <code>null</code> if <var>out</var> was used
     *
     * @throws SAXException  may throw SAXException 
     */
    public abstract String handleElement(String localName, Attributes atts,
            ContentHandler out)
        throws SAXException;
    
	
    // ContentHandler implementation - START

    public void setDocumentLocator(Locator locator)
    {
		// ignore
    }

    public void startDocument()
        throws SAXException
    {
		throw new SAXException("Unexpected startDocument");
    }

    public void endDocument()
        throws SAXException
    {
		throw new SAXException("Unexpected endDocument");
    }

    public void startElement(String namespaceURI, String localName,
                             String qname, Attributes atts)
        throws SAXException
    {
		if (myNamespaceURI.equals(namespaceURI))
		{				
			currentElement = localName;
            currentAttributes = new AttributesImpl(atts);            
		}
		else
		{
			if (sax != null) sax.startElement(namespaceURI, localName, qname, atts);
		}
    }

    public void endElement(String namespaceURI, String localName,
                           String qname)
        throws SAXException
    {
		if (myNamespaceURI.equals(namespaceURI))
		{
			// ignore
		}
		else
		{
			if (sax != null) sax.endElement(namespaceURI, localName, qname);
		}
    }

    public void startPrefixMapping(String prefix, String uri)
        throws SAXException
    {
		if (sax != null) sax.startPrefixMapping(prefix, uri);
    }

    public void endPrefixMapping(String prefix)
        throws SAXException
    {
		if (sax != null) sax.endPrefixMapping(prefix);
    }

    public void characters(char[] chars, int start, int length)
        throws SAXException
    {
		if (sax != null) sax.characters(chars, start, length);
    }

    public void ignorableWhitespace(char[] chars, int start, int length)
        throws SAXException
    {
		if (sax != null) sax.ignorableWhitespace(chars, start, length);
    }

    public void processingInstruction(String target, String data)
        throws SAXException
    {
		if (sax != null) sax.processingInstruction(target, data);
    }

    public void skippedEntity(String name)
        throws SAXException
    {
		if (sax != null) sax.skippedEntity(name);
    }

    // ContentHandler implementation - END
            
}

