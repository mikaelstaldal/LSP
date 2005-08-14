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

import javax.servlet.*;
import javax.servlet.http.*;

import nu.staldal.lsp.*;


/**
 * Access LSP functions from XSLT stylesheets.
 */
public class XSLTExt
{

    /**
     * Private default constructor to prevent instantiation.
     */
    private XSLTExt() { }

    
    /**
     * Get a localized resource for the user's locale.
     *
     * @param pageName LSP page name, 
     *                 or <code>null</code> for global resources only
     * @param key      the key
     *
     * @return [<var>key</var>] if not found.
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
     * @param key      the key
     *
     * @return [<var>key</var>] if not found.
     */
    public static String lang(Object context, String key)
        throws Exception
    {
        return ((LSPServletContext)context).lang(key);
    }
    
    
    /**
     * Encode an URL for Servlet session tracking.
     * 
     * @see javax.servlet.http.HttpServletResponse#encodeURL
     */
    public static String encodeURL(Object context, String url)
    {
        return ((LSPServletContext)context).encodeURL(url);    
    }
}    

