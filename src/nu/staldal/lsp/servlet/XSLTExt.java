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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Access LSP functions from XSLT stylesheets.
 */
public class XSLTExt
{

    /**
     * Private default constructor to prevent instantiation.
     */
    private XSLTExt() 
    {
        // never invoked
    }

    
    /**
     * Get a localized resource for the user's locale.
     *
     * @param context  the {@link nu.staldal.lsp.servlet.LSPServletContext}
     * @param pageName LSP page name, 
     *                 or <code>null</code> for global resources only
     * @param key      the key
     *
     * @return [<var>key</var>] if not found.
     * 
     * @throws Exception  if the {@link nu.staldal.lsp.servlet.LocaleBundleFactory} throws any exception 
     */
    public static String lang(Object context, String pageName, String key)
        throws Exception
    {
        return ((LSPServletContext)context).lang(pageName, key);
    }


    /**
     * Get a localized resource for the user's locale.
     *<p>
     * Same as <code>lang(<var>context</var>,null,<var>key</var>)</code>.
     *
     * @param context  the {@link nu.staldal.lsp.servlet.LSPServletContext}
     * @param key      the key
     *
     * @return [<var>key</var>] if not found.
     * 
     * @throws Exception  if the {@link nu.staldal.lsp.servlet.LocaleBundleFactory} throws any exception 
     */
    public static String lang(Object context, String key)
        throws Exception
    {
        return ((LSPServletContext)context).lang(key);
    }
    
    
    /**
     * Encode an URL for Servlet session tracking.
     *
     * @param context  the {@link nu.staldal.lsp.servlet.LSPServletContext}
     * @param url the URL to encode
     *  
     * @return the encoded URL 
     * 
     * @see javax.servlet.http.HttpServletResponse#encodeURL
     */
    public static String encodeURL(Object context, String url)
    {
        return ((LSPServletContext)context).encodeURL(url);    
    }

    
    /**
     * Check if user is in role.
     * 
     * @param context  the {@link nu.staldal.lsp.servlet.LSPServletContext}
     * @param role  the role to check
     *  
     * @return <code>true</code> if user is in the given role
     *  
     * @see javax.servlet.http.HttpServletRequest#isUserInRole(String)
     */
    public static boolean isUserInRole(Object context, String role)
    {
        return ((LSPServletContext)context).isUserInRole(role);
    }

    
    /**
     * Format a java.util.Date as String using the user's locale.
     *
     * @param context  the {@link nu.staldal.lsp.servlet.LSPServletContext}
     * @param date     the date to format
     * 
     * @return the formatted date
     */
    public static String formatDate(Object context, Date date)
    {
        LSPServletContext ctx = (LSPServletContext)context;
        
        if (date == null) 
        {
            return "";
        }
        
        return DateFormat
            .getDateInstance(DateFormat.SHORT, ctx.getLSPManager().getUserLocale(ctx.getServletRequest()))
            .format(date);
    }    


    /**
     * Format a java.util.Date as String using the user's locale.
     *
     * @param context  the {@link nu.staldal.lsp.servlet.LSPServletContext}
     * @param date     the time to format
     * 
     * @return the formatted time
     */
    public static String formatTime(Object context, Date date)
    {
        LSPServletContext ctx = (LSPServletContext)context;
        
        if (date == null) 
        {
            return "";
        }
        
        return DateFormat
            .getTimeInstance(DateFormat.SHORT, ctx.getLSPManager().getUserLocale(ctx.getServletRequest()))
            .format(date);
    }


    /**
     * Format a java.util.Date as String using the user's locale.
     *
     * @param context  the {@link nu.staldal.lsp.servlet.LSPServletContext}
     * @param date     the date/time to format
     * 
     * @return the formatted date/time
     */
    public static String formatDateTime(Object context, Date date)
    {
        LSPServletContext ctx = (LSPServletContext)context;
        
        if (date == null) 
        {
            return "";
        }
        
        return DateFormat
            .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, ctx.getLSPManager().getUserLocale(ctx.getServletRequest()))
            .format(date);
    }


    /**
     * Format a java.util.Date as String using the user's locale with custom format.
     *
     * @param context  the {@link nu.staldal.lsp.servlet.LSPServletContext}
     * @param pattern  the formatting pattern
     * @param date     the date/time to format
     * 
     * @return the formatted date/time
     */
    public static String formatCustomDateTime(Object context, String pattern, Date date)
    {
        LSPServletContext ctx = (LSPServletContext)context;
        
        if (date == null) 
        {
            return "";
        }
        
        return new SimpleDateFormat(pattern,
                ctx.getLSPManager().getUserLocale(ctx.getServletRequest()))
            .format(date);
    }
}    
