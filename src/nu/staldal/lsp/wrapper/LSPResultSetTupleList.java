/*
 * Copyright (c) 2003, Mikael Ståldal
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

package nu.staldal.lsp.wrapper;

import java.util.NoSuchElementException;
import java.util.Map;
import java.sql.*;

import nu.staldal.lsp.*;


/**
 * Implementation of LSPList and Map for {@link java.sql.ResultSet}.
 * <p>
 * The ResultSet can be traversed once only, and will be closed after the 
 * last element has been read.
 */
public class LSPResultSetTupleList implements LSPList
{
    private int theLength = 0;
	private ResultSet rs;
	private boolean atEnd = false;
	private boolean nextFetched = false;

	private void getNext()
		throws SQLException
	{
		if (!nextFetched && !atEnd)
		{
			nextFetched = true;
			if (!rs.next())
			{
				atEnd = true;
				rs.close();
			}
		}					
	}
	

	public LSPResultSetTupleList(ResultSet rs)
	{
		this.rs = rs;
	}

	public boolean hasNext()
	{
		try {
			getNext();
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e.toString());
		}

		return !atEnd;
	}

	public Object next() throws NoSuchElementException
	{
		try {
			getNext();
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e.toString());
		}
		
		nextFetched = false;
		
		if (atEnd)
		{
			throw new NoSuchElementException();
		}
		else
		{	
			theLength++;
			
			return new ResultSetTuple(rs);
		}
	}

	public int index()
	{
		return theLength;	
	}

    public int length() throws IllegalArgumentException
	{
		if (atEnd)
			return theLength;
		else
			throw new IllegalArgumentException(
				"not possible to check length of a ResultSet");
	}

	public void reset() throws IllegalArgumentException
	{
		if (theLength > 0)
			throw new IllegalArgumentException(
				"not possible to reset a ResultSet");
	}


	static class ResultSetTuple implements Map
	{
		ResultSet rs;
		
		ResultSetTuple(ResultSet rs)
		{
			this.rs = rs;	
		}
		
		
		public Object get(Object key)
		{
			try {
				Object o = rs.getObject((String)key);

				if (o == null) 
					return "";
				else
					return o;
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e.toString());
			}
		}
        
        public boolean containsKey(Object key)
        {
            throw new UnsupportedOperationException();    
        }
    
        public int size()
        {   
            throw new UnsupportedOperationException();    
        }
    
        public boolean isEmpty()
        {
            return size() == 0;    
        }
    
        public boolean containsValue(Object value)
        {
            throw new UnsupportedOperationException();    
        }
        
        public Object put(Object key, Object value)
        {
            throw new UnsupportedOperationException();    
        }
        
    
        public Object remove(Object key)
        {
            throw new UnsupportedOperationException();    
        }
    
        public void putAll(Map t)
        {
            throw new UnsupportedOperationException();    
        }
    
        public void clear()
        {
            throw new UnsupportedOperationException();    
        }
    
        public java.util.Set keySet()
        {
            throw new UnsupportedOperationException();    
        }
    
        public java.util.Collection values()
        {
            throw new UnsupportedOperationException();    
        }
    
        public java.util.Set entrySet()
        {
            throw new UnsupportedOperationException();    
        }
	}
	
}

