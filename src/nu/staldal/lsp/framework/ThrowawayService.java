/*
 * Copyright (c) 2006, Mikael St�ldal
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;


/**
 * Abstract base class for Throwaway Service.
 *<p>
 * Services will be loaded and instantiated using <code>Class.forName</code> 
 * and <code>Class.newInstance</code>, so they must have a public no-arg 
 * constructor.
 *<p>
 * A new instance will be created for each request, and that instance of 
 * thrown away after the request. The {@link #execute execute}
 * method is only invoked once per instance, so there are no thread-safety 
 * issues. It's not a problem to use instance attributes.
 */
public abstract class ThrowawayService
{
    
    /**
     * Request type GET.
     */
    public static final int REQUEST_GET = 1;

    /**
     * Request type POST.
     */
    public static final int REQUEST_POST = 2;
    
    /**
     * Request type include.
     */
    public static final int REQUEST_INCLUDE = 3;
        
    /**
     * Prefix for request attributes for include attributes.
     */
    public static final String INCLUDE_ATTR_PREFIX = "nu.staldal.lsp.framework.INCLUDE.";
    
    /**
     * The ServletContext.
     */
    protected ServletContext context;
    
    /**
     * The HttpServletRequest.
     */
    protected HttpServletRequest request;
    
    /**
     * The HttpServletResponse.
     */
    protected HttpServletResponse response;

    /**
     * The {@link javax.sql.DataSource} to main database,
     * or <code>null</code> if no database is setup.
     */
    protected DataSource mainDB;

    /**
     * database connection to use for this request, 
     * or <code>null</code> if no database has been setup
     */
    protected DBConnection dbConn;    
    
    /**
     * The request type.
     *        {@link #REQUEST_GET}, {@link #REQUEST_POST} or {@link #REQUEST_INCLUDE}  
     */
    protected int requestType;

    final void init(ServletContext context, HttpServletRequest request, HttpServletResponse response, 
            int requestType)
    {
        this.context = context;
        this.request = request;
        this.response = response;        
        this.requestType = requestType;
    }
    
    final String _execute(Map<String,Object> pageParams)
        throws Exception
    {
        String dbName = context.getInitParameter("nu.staldal.lsp.servlet.framework.DB");
    
        if (dbName != null)
        {
            try {
                Context initCtx = new InitialContext();
                Context javaCtx = (Context)initCtx.lookup("java:comp/env");
                mainDB = (DataSource)javaCtx.lookup(dbName);
            }
            catch (NamingException e)
            {
                throw new ServletException(e);    
            }
        }

        try {            
            if (mainDB != null)
            {
                Connection conn = mainDB.getConnection();
                conn.setAutoCommit(false);
                dbConn = new DBConnection(conn);
            }
            else
            {
                dbConn = null;
            }
                    
            return execute(pageParams);
        }
        catch (Exception e)
        {
            try {
                if (dbConn != null) dbConn.rollback();
            }
            catch (SQLException ee)
            {
                throw new ServletException(ee);    
            }
            
            throw e;
        }
        finally
        {
            try {
                if (dbConn != null) dbConn.close();
            }
            catch (SQLException ee)
            {
                throw new ServletException(ee);    
            }                
        }                                           
    }
    
    /**
     * Invoked for a request to this Service.
     *<p>
     * Is invoked for GET, POST and HEAD requests. You should not treat
     * HEAD requests differently than GET requests, the framework will
     * automatically discard the body and only send the headers. The
     * <code>requestType</code> parameter indicate the type of request.
     * See the HTTP specification for differences between GET and POST 
     * requests.
     *<p>
     * There are three choices to create the response:
     *<ol>
     *<li>Return the name of a page to display, and fill 
     * <code>pageParams</code> with parameters to this page.
     * In this case, <code>response</code> should only be used
     * to set headers.</li>
     *<li>Send the whole response by using <code>response</code>  
     * and return <code>null</code>. In this case the framework will not 
     * touch <code>response</code> after this method returns, and will not
     * use <code>pageParams</code>. This can be used if you want
     * to use {@link javax.servlet.http.HttpServletResponse#sendError(int) sendError}
     * or {@link javax.servlet.http.HttpServletResponse#sendRedirect sendRedirect}.</li>
     *<li>Return the name of an other service to forward the request to,
     * prefixed by "*". Any parameters added to <code>pageParams</code> are 
     * retained. You may add attributes to <code>request</code> in order to
     * comnunicate with the other service.</li>
     *</ol>
     *<p>
     * If <code>requestType</code> is {@link #REQUEST_INCLUDE},
     * choice 2 and 3 may not be used, and <code>response</code> may not be
     * modified in any way. You may either return the name of page, or use 
     * the SAX2 {@link org.xml.sax.ContentHandler} passed as a request 
     * attribute with name "org.xml.sax.ContentHandler" and 
     * return <code>null</code>. <code>startDocument</code> and 
     * <code>endDocument</code> must not be invoked on the ContentHandler, use
     * {@link nu.staldal.lsp.ContentHandlerStartEndDocumentFilter} if this is 
     * a problem.
     *
     * @param pageParams  map for page parameters
     *
     * @return name of the page to view, or <code>null</code> to not 
     *         use any page, or the name of an other service to forward to
     *         prefixed by "*"
     *
     * @throws Exception  may throw any Exception
     */
    public abstract String execute(Map<String,Object> pageParams)
        throws Exception;

}
