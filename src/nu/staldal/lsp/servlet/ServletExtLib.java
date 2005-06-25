/*
 * Copyright (c) 2004-2005, Mikael Ståldal
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

import javax.servlet.*;

import org.xml.sax.*;

import nu.staldal.lsp.*;


/**
 * LSP extension library for Servlet environment.
 */
public class ServletExtLib extends SimpleExtLib
{
		
    public String handleElement(String localName, Attributes atts)
		throws SAXException
    {
        if (localName.equals("lang"))
        {
            String key = atts.getValue("", "key");
            if (key == null || key.length() == 0)
            {
                throw new LSPException(
                    "<s:lang> element missing \'key\' attribute");
            }
            
            String x = getLocalizedString(pageName, key);
            if (x == null)
            {
                return '[' + key + ']';
            }
            else
            {
                return x;
            }
        }
        else
        {
            throw new LSPException("Unknown element: " + localName);	
        }
	}
		

	/**
	 * Extension function <code>lang(key)</code>.
	 */
	public Object _lang(Object _key)
		throws SAXException
	{
		if (!(_key instanceof String))
			throw new LSPException(
				"Argument to s:lang(key) function must be a string"); 
		String key = (String)_key;

		if (key == null || key.length() == 0) return "";
		String x = getLocalizedString(pageName, key);
		if (x == null)
			return '[' + key + ']';
		else
			return x;			
	}
	

    /**
     * Return <code>null</code> if not found.
     */
    private String getLocalizedString(String pageName, String key)
        throws SAXException
    {
        LSPServletContext context = (LSPServletContext)extContext; 
        
        try {
            return context.getLSPManager().getLocalizedString(
                context.getServletRequest(), pageName, key);
        }
        catch (RuntimeException e)
        {
            throw e;
        }                                
        catch (SAXException e)
        {
            throw e;
        }            
        catch (Exception e)
        {
            throw new SAXException(e);    
        }
    }
        
}

