/*
 * Copyright (c) 2005-2006, Mikael Ståldal
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

import java.util.*;
import java.sql.*;
import java.text.*;


/**
 * Wrapper around a JDBC {@link java.sql.Connection} with convenience methods.
 */
public class DBConnection
{
    /**
     * Default formatter for SQL DATE type.
     */
    public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    
    /**
     * Default formatter for the SQL TIME type. 
     */
    public static final DateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    
    
    /**
     * Default formatter for the SQL TIMESTAMP type. 
     */
    public static final DateFormat DEFAULT_TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
    
    
    /**
     * The object to use when copying a ResultSet (row) and a 
     * JDBC NULL occurs.
     *<p>
     * Default is the empty string.
     */
    protected Object nullReplacement = "";

    /**
     * Set the object to use when copying a ResultSet (row) and a 
     * JDBC NULL occurs.
     *<p>
     * Default is the empty string.
     *<p>
     * Set to <code>null</code> to not do any replacement.
     * 
     * @param o the replacement object 
     */
    public void setNullReplacement(Object o)
    {
        nullReplacement = o;    
    }
    
    /**
     * Get the object to use when copying a ResultSet (row) and a 
     * JDBC NULL occurs.
     *<p>
     * Default is the empty string.
     * 
     * @return the object to use when copying a ResultSet (row) and a 
     * JDBC NULL occurs
     */
    public Object getNullReplacement()
    {
        return nullReplacement;    
    }
    
    
    /**
     * Formatter for the SQL DATE type.
     *<p>
     * Default is <code>SimpleDateFormat("yyyy-MM-dd")</code>.
     */
    protected DateFormat dateFormatter = DEFAULT_DATE_FORMAT;
    
    /**
     * Set formatter for the SQL DATE type.
     *<p>
     * Default is <code>SimpleDateFormat("yyyy-MM-dd")</code>.
     *<p>
     * Set to <code>null</code> to not do any formatting.
     * 
     * @param df the DateFormat 
     */
    public void setDateFormatter(DateFormat df)
    {
        dateFormatter = df;
    }
    
    /**
     * Get formatter for the SQL DATE type.
     * @return the DateFormat 
     */
    public DateFormat getDateFormatter()
    {
        return dateFormatter;    
    }
    
                
    /**
     * Formatter for the SQL TIMESTAMP type.
     *<p>
     * Default is <code>SimpleDateFormat("yyyy-MM-dd  HH:mm:ss")</code>.
     */
    protected DateFormat timestampFormatter = DEFAULT_TIMESTAMP_FORMAT;                
    
    /**
     * Set formatter for the SQL TIMESTAMP type.
     *<p>
     * Default is <code>SimpleDateFormat("yyyy-MM-dd  HH:mm:ss")</code>.
     *<p>
     * Set to <code>null</code> to not do any formatting.
     * 
     * @param df the DateFormat 
     */
    public void setTimestampFormatter(DateFormat df)
    {
        timestampFormatter = df;
    }
    
    /**
     * Get formatter for the SQL TIMESTAMP type.
     * 
     * @return the DateFormat 
     */
    public DateFormat getTimestampFormatter()
    {
        return timestampFormatter;    
    }

    
    /**
     * Formatter for the SQL TIME type.
     *<p>
     * Default is <code>SimpleDateFormat("HH:mm:ss")</code>.
     */
    protected DateFormat timeFormatter = DEFAULT_TIME_FORMAT;
                
    /**
     * Set formatter for the SQL TIME type.
     *<p>
     * Default is <code>SimpleDateFormat("HH:mm:ss")</code>.
     *<p>
     * Set to <code>null</code> to not do any formatting.
     * 
     * @param df the DateFormat 
     */
    public void setTimeFormatter(DateFormat df)
    {
        timeFormatter = df;
    }
    
    /**
     * Get formatter for the SQL TIME type.
     * 
     * @return the DateFormat 
     */
    public DateFormat getTimeFormatter()
    {
        return timeFormatter;    
    }

    
    /**
     * The wrapped {@link java.sql.Connection}.
     */
    protected final Connection dbConn;
    
    
    /**
     * Create a DBConnection.
     *
     * @param dbConn  the JDBC {@link java.sql.Connection} to wrap.
     */
    public DBConnection(Connection dbConn)
    {
        this.dbConn = dbConn;
    }

    
    /**
     * Return the wrapped {@link java.sql.Connection}.
     *
     * @return  the wrapped {@link java.sql.Connection}
     */
    public Connection getConnection()
    {
        return dbConn;    
    }


    /**
     * Commit transaction.
     *
     * @throws SQLException  if a database error occurs
     *
     * @see java.sql.Connection#commit()
     */
    public void commit()
        throws SQLException
    {
        dbConn.commit();    
    }
    

    /**
     * Rollback transaction.
     *
     * @throws SQLException  if a database error occurs
     *
     * @see java.sql.Connection#rollback()
     */
    public void rollback()
        throws SQLException
    {
        dbConn.rollback();    
    }
    
    
    /**
     * Close connection.
     *
     * @throws SQLException  if a database error occurs
     * 
     * @see java.sql.Connection#close()
     */
    public void close()
        throws SQLException
    {
        dbConn.close();    
    }

    
    private void setParams(PreparedStatement pstmt, Object[] params)
        throws SQLException
    {
        ParameterMetaData pMetaData = pstmt.getParameterMetaData();
        int i = 1;
        for (Object p : params)
        {
            if (p == null)
            {
                pstmt.setNull(i, pMetaData.getParameterType(i));                
            }
            else
            {
                pstmt.setObject(i, p);
            }
            i++;
        }        
    }
    
    
    /**
     * Execute a parameterized query.     
     *<p>
     * You should pass the returned ResultSet to {@link #closeResultSet}
     * when done (unless you pass it to {@link #copyResultSet}).    
     * 
     * @param query   the SQL query with '?' parameters
     * @param params  parameter values
     *
     * @return the {@link java.sql.ResultSet}
     *
     * @throws SQLException  if a database error occurs
     *
     * @see java.sql.PreparedStatement#executeQuery()
     * @see #closeResultSet
     */
    public ResultSet executeQuery(String query, Object... params)
        throws SQLException
    {
        PreparedStatement pstmt = dbConn.prepareStatement(query);        
        setParams(pstmt, params);        
        return pstmt.executeQuery();
    }
    
    
    /**
     * Execute a parameterized query, copy and close the ResultSet.     
     * 
     * @param query   the SQL query with '?' parameters
     * @param params  parameter values
     *
     * @return copy of the ResultSet
     *
     * @throws SQLException  if a database error occurs
     *
     * @see java.sql.PreparedStatement#executeQuery()
     * @see #setNullReplacement
     */
    public List<Map<String,Object>> executeQueryAndCopy(String query, Object... params)
        throws SQLException
    {
        ResultSet rs = null;
        try {
            rs = executeQuery(query, params);
            return _copyResultSet(rs);
        }
        finally {
            closeResultSet(rs);
        }        
    }
    
    
    /**
     * Close a {@link java.sql.ResultSet} and its {@link java.sql.Statement}.
     * Does nothing if <var>rs</var> is <code>null</code>.
     *
     * @param rs  the {@link java.sql.ResultSet} to close
     *
     * @throws SQLException  if a database error occurs
     *
     * @see #executeQuery
     */
    public void closeResultSet(ResultSet rs)
        throws SQLException
    {
        if (rs != null)
        {
            Statement stmt = rs.getStatement();            
            rs.close();
            if (stmt != null) stmt.close();
        }
    }
    
        
    /**
     * Copy a {@link java.sql.ResultSet} into a {@link java.util.List} of
     * {@link java.util.Map}s. Useful for passing a ResultSet to an LSP page.
     *<p>
     * The ResultSet is closed after copying.
     *
     * @param rs the ResultSet to copy
     *
     * @return copy of the ResultSet
     *
     * @throws SQLException  if a database error occurs
     * 
     * @see #setNullReplacement
     */
    public List<Map<String,Object>> copyResultSet(ResultSet rs)
        throws SQLException
    {
        try {
            return _copyResultSet(rs);
        }
        finally {
            closeResultSet(rs);
        }
    }

        
    private List<Map<String,Object>> _copyResultSet(ResultSet rs)
        throws SQLException
    {
        ResultSetMetaData rsmd = rs.getMetaData();
        
        List<Map<String,Object>> l = new ArrayList<Map<String,Object>>();        
        while (rs.next())
        {                 
            l.add(_copyResultSetRow(rs, rsmd));
        }
        return l;
    }
    

    /**
     * Copy a {@link java.sql.ResultSet} row into a {@link java.util.Map}.
     * The current row of the ResultSet is copied, and the ResultSet is not
     * advanced.
     *
     * @param rs the ResultSet to copy a row from
     *
     * @return copy of the ResultSet row
     *
     * @throws SQLException  if a database error occurs
     *
     * @see #setNullReplacement
     */
    public Map<String,Object> copyResultSetRow(ResultSet rs)
        throws SQLException
    {
        ResultSetMetaData rsmd = rs.getMetaData();
    
        return _copyResultSetRow(rs, rsmd);         
    }
    
    
    private Map<String,Object> _copyResultSetRow(ResultSet rs, ResultSetMetaData rsmd)
        throws SQLException
    {
        Map<String,Object> m = new HashMap<String,Object>(
            rsmd.getColumnCount());
            
        for (int i = 1; i<=rsmd.getColumnCount(); i++)
        {
            Object o = rs.getObject(i);
            if (rs.wasNull())
            {
                o = nullReplacement;                        
            }
            else
            {
                switch (rsmd.getColumnType(i))
                {
                case Types.DATE:     
                    if (dateFormatter != null) 
                        o = dateFormatter.format((java.util.Date)o);
                    break;
                    
                case Types.TIME:     
                    if (timeFormatter != null) 
                        o = timeFormatter.format((java.util.Date)o);
                    break;
                    
                case Types.TIMESTAMP:
                    if (timestampFormatter != null) 
                        o = timestampFormatter.format((java.util.Date)o);
                    break;
                }
            }
                
            m.put(rsmd.getColumnName(i), o);
        }
        
        return m;        
    }
    
    
    /**
     * Execute a parameterized query which and return a copy of the first 
     * row of the ResultSet. Any subsequent rows are ignored.
     *<p>
     * Will do the same translation of dates and null as 
     * {@link #copyResultSetRow(ResultSet)}.     
     * 
     * @param query   the SQL query with '?' parameters
     * @param params  parameter values
     *
     * @return a copy of the first row of the ResultSet
     *
     * @throws SQLException  if a database error occurs
     * @throws NoSuchElementException  if the query rerurns no rows
     *
     * @see java.sql.PreparedStatement#executeQuery()
     * @see #copyResultSetRow(ResultSet)
     */
    public Map<String,Object> lookupRow(String query, Object... params)
        throws SQLException, NoSuchElementException
    {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConn.prepareStatement(query);        
            setParams(pstmt, params);        
            rs = pstmt.executeQuery();
            if (!rs.next())
            {
                throw new NoSuchElementException("No rows found");    
            }
            return copyResultSetRow(rs);
        }
        finally {
            if (rs != null) rs.close();            
            if (pstmt != null) pstmt.close();            
        }           
    }

    
    /**
     * Execute a parameterized query which and return the first object
     * in the first row of the ResultSet. Any subsequent rows are ignored.     
     *<p>
     * Will <em>not</em> do any translation of dates and null. 
     * 
     * @param query   the SQL query with '?' parameters
     * @param params  parameter values
     *
     * @return the first object in the first row of the ResultSet
     *
     * @throws SQLException  if a database error occurs
     * @throws NoSuchElementException  if the query rerurns no rows
     *
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public Object lookupObject(String query, Object... params)
        throws SQLException, NoSuchElementException
    {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConn.prepareStatement(query);        
            setParams(pstmt, params);        
            rs = pstmt.executeQuery();
            if (!rs.next())
            {
                throw new NoSuchElementException("No rows found");    
            }
            return rs.getObject(1);
        }
        finally {
            if (rs != null) rs.close();            
            if (pstmt != null) pstmt.close();            
        }           
    }
    
    
    /**
     * Execute a parameterized query which and return the first object
     * in the first row of the ResultSet as a string.
     * Any subsequent rows are ignored.     
     * 
     * @param query   the SQL query with '?' parameters
     * @param params  parameter values
     *
     * @return the first object in the first row of the ResultSet as a String
     *
     * @throws SQLException  if a database error occurs
     * @throws NoSuchElementException  if the query rerurns no rows
     *
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public String lookupString(String query, Object... params)
        throws SQLException, NoSuchElementException
    {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConn.prepareStatement(query);        
            setParams(pstmt, params);        
            rs = pstmt.executeQuery();
            if (!rs.next())
            {
                throw new NoSuchElementException("No rows found");    
            }
            return rs.getString(1);
        }
        finally {
            if (rs != null) rs.close();            
            if (pstmt != null) pstmt.close();            
        }           
    }
    
    
    /**
     * Execute a parameterized query which and return the first object
     * in the first row of the ResultSet as an integer.
     * Any subsequent rows are ignored.     
     * 
     * @param query   the SQL query with '?' parameters
     * @param params  parameter values
     *
     * @return the first object in the first row of the ResultSet as an integer.
     *
     * @throws SQLException  if a database error occurs
     * @throws NoSuchElementException  if the query rerurns no rows
     *
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public int lookupInt(String query, Object... params)
        throws SQLException, NoSuchElementException
    {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConn.prepareStatement(query);        
            setParams(pstmt, params);        
            rs = pstmt.executeQuery();
            if (!rs.next())
            {
                throw new NoSuchElementException("No rows found");    
            }
            return rs.getInt(1);
        }
        finally {
            if (rs != null) rs.close();            
            if (pstmt != null) pstmt.close();            
        }           
    }

    
    /**
     * Execute a parameterized query which and return the first object
     * in the first row of the ResultSet as a boolean.
     * Any subsequent rows are ignored.     
     * 
     * @param query   the SQL query with '?' parameters
     * @param params  parameter values
     *
     * @return the first object in the first row of the ResultSet as a boolean
     *         (an integer equals to 1)
     *
     * @throws SQLException  if a database error occurs
     * @throws NoSuchElementException  if the query rerurns no rows
     *
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public boolean lookupBoolean(String query, Object... params)
        throws SQLException, NoSuchElementException
    {
        return lookupInt(query, params) == 1;
    }
    

    /**
     * Execute a parameterized query which and check whether it returns any 
     * rows.
     * 
     * @param query   the SQL query with '?' parameters
     * @param params  parameter values
     *
     * @return <code>true</code> if the query returns any rows,
     *         <code>false</code> otherwise
     *
     * @throws SQLException  if a database error occurs
     *
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public boolean exists(String query, Object... params)
        throws SQLException
    {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConn.prepareStatement(query);
            setParams(pstmt, params);        
            rs = pstmt.executeQuery();            
            return rs.next();
        }
        finally {
            if (rs != null) rs.close();            
            if (pstmt != null) pstmt.close();            
        }
    }
    
    
    /**
     * Execute a parameterized update query.     
     * 
     * @param query   the SQL query with '?' parameters
     * @param params  parameters
     *
     * @return the row count
     *
     * @throws SQLException  if a database error occurs
     *
     * @see java.sql.PreparedStatement#executeUpdate()
     */
    public int executeUpdate(String query, Object... params)
        throws SQLException
    {
        PreparedStatement pstmt = null;
        
        try {
            pstmt = dbConn.prepareStatement(query);
            setParams(pstmt, params);        
            return pstmt.executeUpdate();        
        }
        finally {
            if (pstmt != null) pstmt.close();            
        }
    }
    
    
    /**
     * Insert a row into a table     
     * 
     * @param table   name of the table
     * @param colList list of column names, separated with ','
     * @param params  values to insert into the columns
     *
     * @return number of rows inserted
     *
     * @throws SQLException  if a database error occurs
     *
     * @see #executeUpdate
     */
    public int insertRow(String table, String colList, Object... params)
        throws SQLException
    {        
        boolean first = true;
        StringBuffer sb = new StringBuffer();
        for (StringTokenizer st = new StringTokenizer(colList, ",");
             st.hasMoreTokens(); )
        {
            st.nextToken();
            if (!first) sb.append(',');
            sb.append('?');
            first = false;
        }
        
        return executeUpdate("INSERT INTO "+table+"("+colList+") VALUES ("
            + sb.toString() + ")", params);            
    }

    
    /**
     * Delete rows from a table.     
     * 
     * @param table        name of the table
     * @param whereClause  WHERE clause (without "WHERE") for the DELETE query,
     *                     with with '?' parameters
     * @param params       parameter values to the WHERE clause 
     *
     * @return number of rows deleted, or -1 if FOREIGN KEY contraint 
     *         violation (SQLState "23") occurs
     *
     * @throws SQLException  if a database error occurs
     *
     * @see #executeUpdate
     */
    public int deleteRow(String table, String whereClause, Object... params)
        throws SQLException
    {                
        try {            
            return executeUpdate("DELETE FROM "+table+" WHERE "+whereClause, 
                params);
        }
        catch (SQLException e)
        {
            if (e.getSQLState().startsWith("23"))
            {
                return -1; 
            }
            else
            {
                throw e;
            }
        }
    }
        
}
