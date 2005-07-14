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
import org.xml.sax.helpers.AttributesImpl;

import nu.staldal.lsp.*;
import nu.staldal.lsp.framework.*;


/**
 * LSP extension library for Servlet environment.
 */
public class ServletExtLib extends SimpleExtLib
{
	private static final String myNamespaceURI = 
		"http://staldal.nu/LSP/ExtLib/Servlet";
			
	
    public String handleElement(String localName, Attributes atts,
                                ContentHandler out)
        throws SAXException
    {
        LSPServletContext context = (LSPServletContext)extContext; 
        
        if (localName.equals("lang"))
        {
            String key = atts.getValue("", "key");
            if (key == null)
                throw new LSPException(
                    "<s:lang> element missing \'key\' attribute");

            return lang(pageName, key, context);
        }
        else if (localName.equals("include"))
        {
            String name = atts.getValue("", "name");
            if (name == null || name.length() == 0)
                throw new LSPException(
                    "<s:include> element missing \'name\' attribute");

            DispatcherServlet dispatcher = 
                (DispatcherServlet)context.getServletContext().getAttribute(
                    DispatcherServlet.class.getName());
                
            if (dispatcher == null)
            {
                throw new LSPException(
                    "<s:include> can only be used within the LSP framework");
            }
            
            Service service;
            try {
                service = dispatcher.lookupService(name);
            }
            catch (InstantiationException e)
            {
                throw new SAXException("Unable to create service", e);    
            }
            catch (IllegalAccessException e)
            {
                throw new SAXException("Unable to create service", e);    
            }
            catch (ServletException e)
            {
                throw new SAXException("Unable to initialize service", e);    
            }
            
            if (service == null)
            {
                throw new LSPException( 
                    "Included service \'"+name+"\' not found");    
            }
    
            Map lspParams = new HashMap();
            String templateName;
            for (int i = 0; i < atts.getLength(); i++)
            {
                String aName = atts.getLocalName(i);
                String aValue = atts.getValue(i);
                
                if (!aName.equals("name"))
                {
                    context.getServletRequest().setAttribute(Service.INCLUDE_ATTR_PREFIX+aName, aValue);    
                }                    
            }
            try {
                templateName = 
                    service.execute(context.getServletContext(), 
                                    context.getServletRequest(), 
                                    context.getServletResponse(),
                                    lspParams, 
                                    Service.REQUEST_INCLUDE);
            }
            catch (ServletException e)
            {
                Throwable ee = e.getRootCause();
                if (ee == null)
                    throw new SAXException(e);
                else if (ee instanceof SAXException)
                    throw (SAXException)ee;
                else if (ee instanceof RuntimeException)
                    throw (RuntimeException)ee;
                else if (ee instanceof Error)
                    throw (Error)ee;
                else
                    throw new SAXException((Exception)ee);
            }
            catch (java.io.IOException e)
            {
                throw new SAXException(e);
            }
            
            if (templateName == null || templateName.length() == 0)
            {
                throw new LSPException( 
                    "Included service \'"+name+"\' didn\'t return any page");                    
            }
            else if (templateName.charAt(0) == '*')
            {
                throw new LSPException( 
                    "Included service \'"+name+"\' attempt to forward");                    
            }
            else
            {
                LSPPage lspPage = context.getLSPManager().getPage(templateName);
                if (lspPage == null)
                {
                    throw new LSPException("Included template \'"+templateName+"\' not found");
                }           
                    
                lspPage.execute(out, lspParams, context);
                                    
                return null;
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
        LSPServletContext context = (LSPServletContext)extContext;
        
		if (!(_key instanceof String))
			throw new LSPException(
				"Argument to s:lang(key) function must be a string"); 
		String key = (String)_key;

		return lang(pageName, key, context);
	}
	

	/**
	 * Extension function <code>encodeURL(url)</code>.
	 */
	public Object _encodeURL(Object _url)
		throws SAXException
	{
        LSPServletContext context = (LSPServletContext)extContext; 
        
		if (!(_url instanceof String))
			throw new LSPException(
				"Argument to s:encodeURL(url) function must be a string"); 
		String url = (String)_url;

        return context.encodeURL(url);			
	}
    

    private String lang(String pageName, String key,
                LSPServletContext context)
        throws SAXException
    {
        try {        
            if (key == null || key.length() == 0) return "";
            String x = context.lang(pageName, key);
            if (x == null)
                return '[' + key + ']';
            else
                return x;			
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

