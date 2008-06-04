/*
 * Copyright (c) 2004-2007, Mikael St�ldal
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.*;

import org.xml.sax.*;

import nu.staldal.lsp.*;
import nu.staldal.lsp.framework.*;


/**
 * LSP extension library for Servlet environment.
 * 
 * Namespace URI: "http://staldal.nu/LSP/ExtLib/Servlet"
 */
public class ServletExtLib extends SimpleExtLib
{
    @Override
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
            
            Object service;
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
    
            Map<String,Object> lspParams = new HashMap<String,Object>();
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
            context.getServletRequest().setAttribute(
                ContentHandler.class.getName(), out); 
            try {
                templateName = 
                    dispatcher.executeService(service, context.getServletRequest(), 
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
            context.getServletRequest().removeAttribute(
                ContentHandler.class.getName()); 
            
            if (templateName == null || templateName.length() == 0)
            {
                return null;
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
     * 
     * @param _key      the key (String)
     *
     * @return [<var>key</var>] if not found (String)
     *  
	 * @throws SAXException
     * 
     * @see nu.staldal.lsp.servlet.LSPServletContext#lang(String, String)
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
     * 
     * @param _url  the URL to encode (String)
     *  
     * @return the encoded URL (String)
     *  
	 * @throws SAXException
     *  
     * @see nu.staldal.lsp.servlet.LSPServletContext#encodeURL(String)
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

    
    /**
     * Extension function <code>isUserInRole(role)</code>.
     * 
     * @param _role  the role to check (String)
     *  
     * @return <code>true</code> if user is in the given role
     * 
     * @throws SAXException
     *  
     * @see nu.staldal.lsp.servlet.LSPServletContext#isUserInRole(String)
     */
    public Object _isUserInRole(Object _role)
        throws SAXException
    {
        LSPServletContext context = (LSPServletContext)extContext; 
        
        if (!(_role instanceof String))
            throw new LSPException(
                "Argument to s:isUserInRole(role) function must be a string");
        
        String role = (String)_role;
        
        return context.isUserInRole(role);         
    }    

    
    private Date parseDate(Object _date) throws LSPException 
    {
        if (_date instanceof Date)
        {
            return (Date)_date;        
        } 
        else if (_date instanceof Long)
        {
            return new Date(((Number)_date).longValue());
        }
        else if (_date instanceof String) 
        {
            return new Date(Long.parseLong((String)_date));
        }
        else
        {
            throw new LSPException(                    
                    "Argument must be a Date, Long or String");
        }        
    }
    
    
    /**
     * Extension function <code>formatDate(date)</code>.
     * 
     * @param _date  the date to format (java.util.Date)
     * 
     * @return the date formatted as String
     * 
     * @throws LSPException
     */
    public Object _formatDate(Object _date) throws LSPException
    {
        LSPServletContext context = (LSPServletContext)extContext;
        
        if (_date == null) 
        {
            return "";
        }

        return DateFormat
            .getDateInstance(DateFormat.SHORT, context.getLSPManager().getUserLocale(context.getServletRequest()))
            .format(parseDate(_date));
    }

        
    /**
     * Extension function <code>formatTime(date)</code>.
     * 
     * @param _date  the time to format (java.util.Date)
     * 
     * @return the time formatted as String
     * 
     * @throws LSPException
     */
    public Object _formatTime(Object _date) throws LSPException
    {
        LSPServletContext context = (LSPServletContext)extContext;
        
        if (_date == null) 
        {
            return "";
        }
        
        return DateFormat
            .getTimeInstance(DateFormat.SHORT, context.getLSPManager().getUserLocale(context.getServletRequest()))
            .format(parseDate(_date));
    }


    /**
     * Extension function <code>formatDateTime(date)</code>.
     * 
     * @param _date  the date/time to format (java.util.Date)
     * 
     * @return the date/time formatted as String
     * 
     * @throws LSPException
     */
    public Object _formatDateTime(Object _date) throws LSPException
    {
        LSPServletContext context = (LSPServletContext)extContext;
        
        if (_date == null) 
        {
            return "";
        }
        
        return DateFormat
            .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, 
                    context.getLSPManager().getUserLocale(context.getServletRequest()))
            .format(parseDate(_date));
    }

    
    /**
     * Extension function <code>formatCustomDateTime(date)</code>.
     * 
     * @param _pattern  the formatting pattern
     * @param _date     the date/time to format (java.util.Date)
     * 
     * @return the date/time formatted as String
     * 
     * @throws LSPException
     */
    public Object _formatCustomDateTime(Object _pattern, Object _date) throws LSPException
    {
        LSPServletContext context = (LSPServletContext)extContext;
        
        if (_date == null) 
        {
            return "";
        }
        
        if (!(_pattern instanceof String))
        {
            throw new LSPException(                    
                    "First argument to formatCustomDateTime(pattern, date) function must be a string");
        }

        return new SimpleDateFormat((String)_pattern,  
                    context.getLSPManager().getUserLocale(context.getServletRequest()))
            .format(parseDate(_date));
    }


    /**
     * Extension function <code>formatXMLDateTime(date)</code>.
     * 
     * @param _date     the date/time to format (java.util.Date)
     * 
     * @return the date/time formatted as String
     * 
     * @throws LSPException
     */
    public Object _formatXMLDateTime(Object _date) throws LSPException
    {
        if (_date == null) 
        {
            return "";
        }
        
        DateFormat df = new SimpleDateFormat(
                    "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'",  
                    Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));                
        return df.format(parseDate(_date));
    }
        
    
    private String lang(String pageName, String key,
                LSPServletContext context)
        throws SAXException
    {
        try {        
            return context.lang(pageName, key);
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
