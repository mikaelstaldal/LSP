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

package nu.staldal.xodus;

import java.util.*;

import javax.xml.transform.OutputKeys;


/**
 * Output config.
 */
public class OutputConfig
{
    public String method;
    public boolean isXhtml = false;
    public boolean isText = false;
    public boolean isHtml = false;

    public String version;
    
    public String encoding;
    
    public int omit_xml_declaration;
    
    public boolean standalone;
    
    public String doctype_public;
    public String doctype_system;
    
    public Set<String> cdata_section_elements;
    
    public boolean indent;
    
    public String media_type;
    
    
    /**
     * Factory method.
     * 
     * @param outputProps output properties
     *  
     * @return a new OutputConfig 
     */
    public static OutputConfig createOutputConfig(Properties outputProps)
    {
        return new OutputConfig(outputProps);
    }
    
    
    private OutputConfig(Properties outputProps)
        throws IllegalArgumentException
    {
        method = outputProps.getProperty(OutputKeys.METHOD);
        if (method == null)
            throw new IllegalArgumentException("Output method must be specified");
        else if (method.equals("xml"))
        {
            // nothing to do
        }
        else if (method.equals("xhtml"))
            isXhtml = true;
        else if (method.equals("text"))
            isText = true;
        else if (method.equals("html"))
            isHtml = true;
        else
            throw new IllegalArgumentException("Unknown output method: " + method);
        
        version = outputProps.getProperty(OutputKeys.VERSION);
        if (version == null)
            version = isHtml ? "4.0" : "1.0";
        
        encoding = outputProps.getProperty(OutputKeys.ENCODING);
        if (encoding == null)
            encoding = (isText || isHtml) ? "iso-8859-1" : "UTF-8";
        
        String omit = outputProps.getProperty(OutputKeys.OMIT_XML_DECLARATION);
        if (omit == null)
            omit_xml_declaration = 0;
        else if (omit.equals("yes") || omit.equals("omit_xml_declaration"))
            omit_xml_declaration = 1;
        else if (omit.equals("no"))
            omit_xml_declaration = -1;
        else
            throw new IllegalArgumentException("Illegal omit_xml_declaration value: " + omit);                        
        
        String _standalone = outputProps.getProperty(OutputKeys.STANDALONE);
        if (_standalone == null)
            standalone = false;
        else if (_standalone.equals("yes") || _standalone.equals("standalone"))
            standalone = true;
        else if (_standalone.equals("no"))
            standalone = false;
        else
            throw new IllegalArgumentException("Illegal standalone value: " + _standalone);                        

        String _doctype_public = outputProps.getProperty(OutputKeys.DOCTYPE_PUBLIC);
        if (_doctype_public != null)
            doctype_public = _doctype_public;
        String _doctype_system = outputProps.getProperty(OutputKeys.DOCTYPE_SYSTEM);
        if (_doctype_system != null)
            doctype_system = _doctype_system;
        
        String cdata = outputProps.getProperty(OutputKeys.CDATA_SECTION_ELEMENTS);         
        if (cdata == null)
        {
            cdata_section_elements = Collections.emptySet();
        }
        else
        {
            cdata_section_elements = new HashSet<String>();
            for (StringTokenizer st = new StringTokenizer(cdata);
                 st.hasMoreTokens(); )
            {
                cdata_section_elements.add(st.nextToken());
            }
        }
        
        String _indent = outputProps.getProperty(OutputKeys.INDENT);
        if (_indent == null)
            indent = false;
        else if (_indent.equals("yes") || _indent.equals("indent"))
            indent = true;
        else if (_indent.equals("no"))
            indent = false;
        else
            throw new IllegalArgumentException("Illegal indent value: " + _indent);
        
        media_type = outputProps.getProperty(OutputKeys.MEDIA_TYPE);
        if (media_type == null)
        {
            if (isHtml)
                media_type = "text/html";
            else if (isXhtml)
                media_type = "text/html";
            else if (isText)
                media_type = "text/plain";
            else
                media_type = "text/xml";
        }               
    }
    
}
