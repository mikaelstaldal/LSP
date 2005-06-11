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
import java.sql.*;
import javax.sql.DataSource;
import javax.naming.*;
import javax.servlet.ServletContext;


/**
 * Factory for loading localization data from SQL database.
 */
class SQLLocaleBundleFactory implements LocaleBundleFactory
{
    public static final String LOCALE_DB = "nu.staldal.lsp.servlet.LOCALE_DB";
    public static final String LOCALE_TABLE = "nu.staldal.lsp.servlet.LOCALE_TABLE";
    
    private DataSource dataSource;
    private String localeTable;
    
    
    public void init(ClassLoader classLoader, ServletContext servletContext)
        throws NamingException
    {
        String localeDB = servletContext.getInitParameter(LOCALE_DB);
        if (localeDB == null)            
            throw new RuntimeException(LOCALE_DB + " parameter is not set");

        localeTable = servletContext.getInitParameter(LOCALE_TABLE);
        if (localeTable == null)            
            throw new RuntimeException(LOCALE_TABLE + " parameter is not set");
        
        Context initCtx = new InitialContext();
        Context envCtx = (Context)initCtx.lookup("java:comp/env");                
        dataSource = (DataSource)envCtx.lookup(localeDB);
    }
    
    
    public Map loadBundle(Locale locale)
        throws SQLException
    {        
        Map m = new HashMap();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(
                "SELECT LSPPAGE,THEKEY,VALUE FROM "+localeTable+" WHERE LOCALE=?");        
            if (locale == null)
            {            
                pstmt.setString(1, "*");
            }
            else
            {
                pstmt.setString(1, locale.toString());
            }
            rs = pstmt.executeQuery();
            while (rs.next())
            {
                String page = rs.getString("LSPPAGE");
                if (page.equals("*"))
                {
                    m.put(rs.getString("THEKEY"), rs.getString("VALUE"));    
                }
                else
                {
                    m.put(page+'$'+rs.getString("THEKEY"), rs.getString("VALUE"));    
                }
            }            
        }
        finally
        {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
        
        return m;
    }
}

