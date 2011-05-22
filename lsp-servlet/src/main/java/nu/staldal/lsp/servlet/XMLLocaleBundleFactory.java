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

package nu.staldal.lsp.servlet;

import java.util.*;
import java.io.*;

import org.xml.sax.*;
import javax.xml.parsers.*;

import javax.servlet.ServletContext;


/**
 * Factory for loading localization data from XML files.
 */
class XMLLocaleBundleFactory implements LocaleBundleFactory
{
    private ClassLoader classLoader;
	private SAXParserFactory spf;    

    public void init(ClassLoader classLoader, ServletContext servletContext)
    {
        this.classLoader = classLoader;    

		try {
			spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			spf.setValidating(false);
			spf.setFeature("http://xml.org/sax/features/namespaces", true);
			spf.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
			spf.setFeature("http://xml.org/sax/features/validation", false);
		}
		catch (ParserConfigurationException e)
		{
			throw new Error("Unable to configure XML parser");	
		}
		catch (SAXException e)
		{
			throw new Error("Unable to configure XML parser");	
		}
    }

    
    public Map<String,String> loadBundle(Locale locale)
        throws IOException, SAXException
    {        
        String name = (locale != null)
            ? "LSPLocale_"+locale.toString()+".xml"
            : "LSPLocale.xml";
        
        InputStream is = classLoader.getResourceAsStream(name);            
        if (is == null) return null;
        
        try {            
            XMLReader parser = spf.newSAXParser().getXMLReader();
            LocaleBuilder lb = new LocaleBuilder();
            parser.setContentHandler(lb);
            parser.parse(new InputSource(is));               
            is.close();            
            return lb.getRes();        
        }
		catch (ParserConfigurationException e)
		{
			throw new Error("Unable to configure XML parser");	
		}
    }
    
    
    class LocaleBuilder implements ContentHandler
    {        
        private final Map<String,String> res;
        private int elementDepth;
        private String currentPage;
        private String currentKey;
        private StringBuffer currentValue;
        
        LocaleBuilder() 
        {
            res = new HashMap<String,String>();
        }
        
        Map<String,String> getRes()
        {
            return res;    
        }
        
        public void setDocumentLocator(Locator locator)
        {
            // nothing to do    
        }
    
        
        public void startDocument()
            throws SAXException
        {
            elementDepth = 0;
            currentPage = null;
            currentKey = null;
            currentValue = null;
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
           
        
        public void startElement(String namespaceURI, String localName,
                      String qName, Attributes atts)
            throws SAXException
        {
            if (localName.equals("string"))
            {
                String key = atts.getValue("", "key");
                if (key == null)
                {
                    throw new SAXException("<string> element without \'key\' attribute");
                }
                
                currentKey = key;
                currentValue = new StringBuffer(256);
            }
            else if (elementDepth == 1 && localName.equals("page"))
            {
                String name = atts.getValue("", "name");
                if (name == null)
                {
                    throw new SAXException("<page> element without \'name\' attribute");
                }
                
                currentPage = name;
            }
            
            elementDepth++;
        }
    
    
        public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException
        {        
            elementDepth--;

            if (localName.equals("string"))
            {
                if (currentPage != null)
                {
                    res.put(currentPage+'$'+currentKey, currentValue.toString());                    
                }
                else
                {
                    res.put(currentKey, currentValue.toString());
                }                    
                
                currentKey = null;
                currentValue = null;
            }
            else if (elementDepth == 1 && localName.equals("page"))
            {
                currentPage = null;
            }
        }
    
    
        public void characters(char ch[], int start, int length)
            throws SAXException
        {
            if (currentValue != null)
            {
                currentValue.append(ch, start, length);    
            }
        }
    
    
        public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException
        {
            // nothing to do
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
            // nothing to do
        }                                
        
    }
}

