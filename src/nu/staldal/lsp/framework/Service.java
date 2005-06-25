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

package nu.staldal.lsp.framework;

import java.io.IOException;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Interface for Service.
 *<p>
 * Services will be loaded and instantiated using <code>Class.forName</code> 
 * and <code>Class.newInstance</code>, so they must have a public no-arg 
 * constructor.
 *<p>
 * There will be only one instance of each Service per web application per 
 * JVM (in case of a distributed application). The {@link #execute execute}
 * method may be invoked concurrently by several threads.
 *<p>
 * It's not recommended to have any instance attributes in the Service class
 * which can be changed by the {@link #execute execute} method. Instance attributes
 * which are initialized by the {@link #init init} method and then only read by
 * the {@link #execute execute} method are OK. If you have instance attributes which
 * can be changed by the {@link #execute execute} method, access to them need to be
 * synchronized.
 *<p>
 * The life cycle and instance management of Services are very similar to 
 * {@link javax.servlet.Servlet}, see the Servlet specification for details.
 */
public interface Service
{
    
    /**
     * Invoked once directly after instantiation, before first use.
     *
     * @throws ServletException  may throw ServletException
     */
    public void init()
        throws ServletException;
    
    
    /**
     * Invoked for each request to this Service.
     *<p>
     * May be invoked concurrently by several threads.
     *<p>
     * Is invoked for GET, POST and HEAD requests. You should not treat
     * HEAD requests differently than GET requests, the framework will
     * automatically discard the body and only send the headers. The
     * <code>isPost</code> parameter will be set for POST requests.
     * See the HTTP specification for differences between GET and POST 
     * requests.
     *<p>
     * There are two choices to create the response:
     *<ol>
     *<li>Return the name of a page to display, and fill the 
     * <code>pageParams</code> object with parameters to this page.
     * In that case, the <code>response</code> object should only be used
     * to set headers.</li>
     *<li>Send the whole response by using the <code>response</code> object 
     * and return <code>null</code>. In that case the framework will not 
     * touch the <code>response</code> object after this method returns, and will not
     * use the <code>pageParams</code> object. This can be used if you want
     * to use {@link javax.servlet.http.HttpServletResponse#sendError sendError}
     * or {@link javax.servlet.http.HttpServletResponse#sendRedirect sendRedirect}</li>
     *</ol>
     *
     * @param context     the {@link javax.servlet.ServletContext}
     * @param request     the {@link javax.servlet.http.HttpServletRequest}
     * @param response    the {@link javax.servlet.http.HttpServletResponse}
     * @param pageParams  map for page parameters
     * @param isPost      <code>true</code> if this request is a POST request,
     *                    <code>false</code> if it's a GET or HEAD request 
     *
     * @return name of the page to view, or <code>null</code> to not 
     *         use any page.
     *
     * @throws ServletException  may throw ServletException
     * @throws IOException  may throw IOException
     */
    public String execute(ServletContext context, 
                HttpServletRequest request, HttpServletResponse response,
                Map pageParams, boolean isPost)
        throws ServletException, IOException;


    /**
     * Invoked once by the {@link javax.servlet.Servlet#destroy} method.
     */
    public void destroy();

}

