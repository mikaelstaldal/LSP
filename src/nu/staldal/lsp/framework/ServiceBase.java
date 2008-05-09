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

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.sql.DataSource;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Abstract base class for Service. See {@link Service}.
 */
public abstract class ServiceBase implements Service
{
    /**
     * The {@link javax.servlet.ServletContext}.
     */
    protected ServletContext context;
    
    /**
     * The {@link javax.sql.DataSource} to main database,
     * or <code>null</code> if no database is setup.
     */
    protected DataSource mainDB;

    
    /**
     * Get the {@link javax.servlet.ServletContext}.
     *
     * @return the {@link javax.servlet.ServletContext}
     */   
    public ServletContext getServletContext()
    {
        return context;    
    }
    
    
    /**
     * Invoked once directly after instantiation, before first use.
     *
     * @param dbConn  database connection, 
     *                or <code>null</code> if no database has been setup
     *
     * @throws Exception  may throw any Exception
     */
    public void init(DBConnection dbConn)
        throws Exception
    {
        // nothing to do here, may be overridden
    }
    

    public final void init(ServletContext context)
        throws ServletException
    {
        this.context = context;

        String dbName = context.getInitParameter(
            "nu.staldal.lsp.servlet.framework.DB");
        
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
        
        Connection conn = null;
        try {
            DBConnection dbConn = null;
            if (mainDB != null)
            {
                conn = mainDB.getConnection();
                conn.setAutoCommit(false);
                dbConn = new DBConnection(conn);
            }
                    
            init(dbConn);
        }
        catch (Exception e)
        {
            try {
                if (conn != null) conn.rollback();
            }
            catch (SQLException ee)
            {
                throw new ServletException(ee);    
            }                
            
            if (e instanceof ServletException)                
                throw (ServletException)e;
            else
                throw new ServletException(e);   
        }
        finally
        {
            try {
                if (conn != null) conn.close();
            }
            catch (SQLException ee)
            {
                throw new ServletException(ee);    
            }                
        }                    
    }
    
    
    /**
     * Dump all request parameters to the Servlet log.
     * Useful for debugging.
     *
     * @param request     the {@link javax.servlet.http.HttpServletRequest}
     */
    public void dumpRequestParameters(HttpServletRequest request)
    {
        context.log("--- Request parameters ---");    
        for (Enumeration<?> e = request.getParameterNames(); e.hasMoreElements(); )
        {
            String name = (String)e.nextElement();
            context.log(name + "=" + request.getParameter(name));    
        }
        context.log("+++ Request parameters +++");
    }
    
    
    /**
     * Invoked for each request to this Service. See {@link Service#execute}.
     *
     * @param request     the {@link javax.servlet.http.HttpServletRequest}
     * @param response    the {@link javax.servlet.http.HttpServletResponse}
     * @param pageParams  map for page parameters
     * @param requestType the type of request:
     *        {@link #REQUEST_GET}, {@link #REQUEST_POST}, {@link #REQUEST_PUT}, {@link #REQUEST_DELETE} 
     *        or {@link #REQUEST_INCLUDE}  
     * @param dbConn  database connection to use for this request, 
     *                or <code>null</code> if no database has been setup
     *
     * @return name of the page to view, or <code>null</code> to not 
     *         use any page, or the name of an other service to forward to
     *         prefixed by "*"
     *
     * @throws Exception  may throw any Exception
     */
    public abstract String execute( 
                HttpServletRequest request, HttpServletResponse response,
                Map<String,Object> pageParams, int requestType,
                DBConnection dbConn)
        throws Exception;
        
    
    public final String execute( 
                HttpServletRequest request, HttpServletResponse response,
                Map<String,Object> pageParams, int requestType)
        throws ServletException, IOException
    {
        Connection conn = null;
        try {
            DBConnection dbConn = null;
            if (mainDB != null)
            {
                conn = mainDB.getConnection();
                conn.setAutoCommit(false);
                dbConn = new DBConnection(conn);
            }
                    
            return execute(request, response, pageParams, 
                requestType, dbConn);
        }
        catch (Exception e)
        {
            try {
                if (conn != null) conn.rollback();
            }
            catch (SQLException ee)
            {
                throw new ServletException(ee);    
            }                
            
            if (e instanceof ServletException)                
                throw (ServletException)e;
            else if (e instanceof IOException)                
                throw (IOException)e;
            else if (e instanceof RuntimeException)                
                throw (RuntimeException)e;
            else
                throw new ServletException(e);   
        }
        finally
        {
            try {
                if (conn != null) conn.close();
            }
            catch (SQLException ee)
            {
                throw new ServletException(ee);    
            }                
        }                    
    }
    
    
    /**
     * Invoked once by the {@link javax.servlet.Servlet#destroy} method.
     */
    public void destroy()
    {
        // nothing to do here, may be overridden
    }
}

