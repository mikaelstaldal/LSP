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

import org.xml.sax.*;

class IncludeHandler implements ContentHandler, ErrorHandler
{
    private static final String LSP_CORE_NS = "http://staldal.nu/LSP/core";

    private String lspPrefix = null;
    private ContentHandler sax;

    IncludeHandler(ContentHandler ch)
    {
        sax = ch;
    }

    // SAX ContentHandler implementation

    public void setDocumentLocator(Locator locator)
    {
        // nothing to do
    }

    public final void startDocument()
        throws SAXException
    {
        // nothing to do
    }

    public final void endDocument()
        throws SAXException
    {
        // nothing to do
    }

    public void startElement(String namespaceURI, String localName,
                             String qname, Attributes atts)
        throws SAXException
    {
        if (!namespaceURI.equals(LSP_CORE_NS))
            sax.startElement(namespaceURI, localName, qname, atts);
    }

    public void endElement(String namespaceURI, String localName,
                           String qname)
        throws SAXException
    {
        if (!namespaceURI.equals(LSP_CORE_NS))
            sax.endElement(namespaceURI, localName, qname);
    }

    public void startPrefixMapping(String prefix, String uri)
        throws SAXException
    {
        if (uri.equals(LSP_CORE_NS))
        {
            lspPrefix = prefix;
        }
        else
        {
            sax.startPrefixMapping(prefix, uri);
        }
    }

    public void endPrefixMapping(String prefix)
        throws SAXException
    {
        if (prefix.equals(lspPrefix))
        {
            lspPrefix = null;
        }
        else
        {
            sax.endPrefixMapping(prefix);
        }
    }

    public void characters(char ch[], int start, int length)
        throws SAXException
    {
        sax.characters(ch, start, length);
    }

    public void ignorableWhitespace(char ch[], int start, int length)
        throws SAXException
    {
        sax.ignorableWhitespace(ch, start, length);
    }

    public void processingInstruction (String target, String data)
        throws SAXException
    {
        sax.processingInstruction(target, data);
    }

    public void skippedEntity(String name)
        throws SAXException
    {
        sax.skippedEntity(name);
    }


    // SAX ErrorHandler implementation

    public final void warning(SAXParseException e)
    {
        // ignore warning
    }

    public final void error(SAXParseException e)
        throws SAXException
    {
        throw e;
    }

    public final void fatalError(SAXParseException e)
        throws SAXException
    {
        throw e;
    }
}

