/*
 * Copyright (c) 2005-2006, Mikael Ståldal
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

package nu.staldal.xodus;

import java.io.*;

import org.xml.sax.*;

import javax.xml.transform.stream.StreamResult;


public class TextSerializer extends Serializer
{    
    
    TextSerializer(StreamResult result, OutputConfig outputConfig)
        throws IllegalArgumentException, IOException, 
            UnsupportedEncodingException
    {
        super(result, outputConfig);
    }
        
    
    // ContentHandler implementation
    
    /**
     * Does nothing.
     */
    public void setDocumentLocator(Locator locator)
    {
        // nothing to do    
    }

    
    public void startDocument()
	    throws SAXException
    {
        // nothing to do    
    }

    
    public void startPrefixMapping(String prefix, String uri)
	    throws SAXException
    {
        // nothing to do    
    }


    public void endPrefixMapping(String prefix)
	    throws SAXException
    {
        // nothing to do    
    }


    public void startElement(String uri, String localName,
			      String qName, Attributes atts)
	    throws SAXException
    {
        // nothing to do    
    }
    

    public void endElement(String uri, String localName, String qName)
	    throws SAXException
    {
        // nothing to do    
    }


    @Override
    public void characters(CharSequence cs)
	    throws SAXException
    {
        try {
            out.append(cs);
        }
        catch (IOException e)
        {
            throw new SAXException(e);    
        }
    }


    public void characters(char ch[], int start, int length)
	    throws SAXException
    {
        try {
            out.write(ch, start, length);
        }
        catch (IOException e)
        {
            throw new SAXException(e);    
        }
    }


    @Override
    public void ignorableWhitespace(CharSequence cs)
	    throws SAXException
    {
        characters(cs);
    }


    public void ignorableWhitespace(char ch[], int start, int length)
	    throws SAXException
    {
        characters(ch, start, length);
    }


    public void processingInstruction(String target, String data)
	    throws SAXException
    {
        // nothing to do    
    }

    
    public void skippedEntity(String name)
	    throws SAXException
    {
        // nothing to do    
    }
    

    public void endDocument()
	    throws SAXException
    {
        try {
            finishOutput();
        }
        catch (IOException e)
        {
            throw new SAXException(e);    
        }
    }
    
    
    // LexicalHandler implementation

    
    public void startDTD(String name, String publicId, String systemId)
	    throws SAXException
    {
        // nothing to do    
    }


    public void endDTD()
	    throws SAXException
    {
        // nothing to do    
    }
    
    
    public void startEntity(String name)
	    throws SAXException
    {
        // nothing to do    
    }


    public void endEntity(String name)
	    throws SAXException
    {
        // nothing to do    
    }


    public void startCDATA()
	    throws SAXException
    {
        // nothing to do    
    }


    public void endCDATA()
	    throws SAXException
    {        
        // nothing to do    
    }


    @Override
    public void comment(CharSequence cs)
	    throws SAXException
    {
        // nothing to do    
    }


    public void comment(char ch[], int start, int length)
	    throws SAXException
    {
        // nothing to do    
    }
    

    // DTDHandler implementation

    public void notationDecl(String name, String publicId, String systemId)
	    throws SAXException
    {
        // nothing to do    
    }
    
    
    public void unparsedEntityDecl(String name, String publicId,
					               String systemId, String notationName)
        throws SAXException
    {        
        // nothing to do    
    }
    

    // DeclHandler implementation
      
    public void elementDecl(String name, String model)
	    throws SAXException
    {        
        // nothing to do    
    }


    public void attributeDecl(String eName, String aName,
					          String type, String mode, String value)
        throws SAXException
    {        
        // nothing to do    
    }


    public void internalEntityDecl(String name, String value)
	    throws SAXException
    {        
        // nothing to do    
    }


    public void externalEntityDecl(String name, String publicId,
					               String systemId)
        throws SAXException
    {        
        // nothing to do    
    }


    // Appendable
    
    public Appendable append(CharSequence cs)
        throws IOException
    {
        out.append(cs);
        return this;
    }


    public Appendable append(char c) 
        throws IOException
    {
        out.append(c);
        return this;
    }


    public Appendable append(CharSequence cs, int start, int end) 
        throws IOException
    {
        out.append(cs, start, end);
        return this;
    }
    
}
