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

package nu.staldal.lsp.ext;

import org.xml.sax.*;

import nu.staldal.lsp.*;
import nu.staldal.lagoon.core.Target;

public class TestExtension implements LSPExtLib, ContentHandler
{
	private ContentHandler sax;
	
	/**
	 * Invoked before the element is sent.
	 *
	 * @param out     where to write XML output.
	 * @param target  the current target
	 *
	 * @return  a ContentHandler to send input to.
	 */
	public ContentHandler beforeElement(ContentHandler out, Target target)
		throws SAXException
	{
		this.sax = out;
		
		return this;
	}
	
	
	/**
	 * Invoked after the element is sent.
	 *
	 * @return  a string output.
	 */
	public String afterElement()
		throws SAXException
	{
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

}

