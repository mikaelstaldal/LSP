/*
 * Copyright (c) 2002, Mikael Ståldal
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

package nu.staldal.lsp.extlib;

import org.xml.sax.*;

import nu.staldal.lsp.*;
import nu.staldal.lagoon.core.LagoonContext;
import nu.staldal.lagoon.core.Target;
import nu.staldal.lagoon.core.SourceManager;

public class TestExtension implements LSPExtLib, ContentHandler
{
	private ContentHandler sax;
	private int funcCalls;
	private int pageHits = 0;
	private String targetURL;

	public void init(String namespaceURI)
		throws LSPException
	{
		System.out.println("TestExtension.init(" + namespaceURI + ")");	
	}


	public void startPage(Object extContext, String targetURL)
		throws LSPException
	{
		this.targetURL = targetURL;
		funcCalls = 0;
		
		System.out.println("TestExtension.startPage(" 
			+ targetURL + ") for the " 
			+ (++pageHits) + " time");	
	}
	

	public void endPage()
		throws LSPException
	{
		System.out.println("TestExtension.endPage(" 
			+ targetURL + ")");	
	}


	public ContentHandler beforeElement(ContentHandler out)
		throws SAXException
	{
		System.out.println("TestExtension.beforeElement()");	
		this.sax = out;
		
		return this;
	}
	
	public String afterElement()
		throws SAXException
	{
		System.out.println("TestExtension.afterElement()");	
		return null;
	}
		

    // ContentHandler implementation

    public void setDocumentLocator(Locator locator)
    {
    }

    public void startDocument()
    {
    }

    public void endDocument()
    {
    }

    public void startElement(String namespaceURI, String localName,
                             String qname, Attributes atts)
        throws SAXException
    {
        sax.startElement(namespaceURI, localName+"foo", "", atts);
    }

    public void endElement(String namespaceURI, String localName,
                           String qname)
        throws SAXException
    {
        sax.endElement(namespaceURI, localName+"foo", "");
    }

    public void startPrefixMapping(String prefix, String uri)
        throws SAXException
    {
		sax.startPrefixMapping(prefix, uri);
    }

    public void endPrefixMapping(String prefix)
        throws SAXException
    {
		sax.endPrefixMapping(prefix);
    }

    public void characters(char[] chars, int start, int length)
        throws SAXException
    {
        sax.characters(chars, start, length);
    }

    public void ignorableWhitespace(char[] chars, int start, int length)
        throws SAXException
    {
        sax.ignorableWhitespace(chars, start, length);
    }

    public void processingInstruction(String target, String data)
        throws SAXException
    {
        sax.processingInstruction(target, data);
    }

    public void skippedEntity(String name)
        throws SAXException
    {
        sax.skippedEntity(name);
    }


	public Object function(String name, Object[] args)
	{
		System.out.println("TestExtension.function(" + name + ")");	
		return "[Function " + name + " invoked with " + args.length + " parameters for the " + (++funcCalls) + " time]";
	}

}

