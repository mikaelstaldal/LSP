/*
 * Copyright (c) 2001-2002, Mikael Ståldal
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

package nu.staldal.lsp;

import java.util.*;


/**
 * Interface for the LSP list data type.
 * <p>
 * All LSPList implementation must implement the {@link #hasNext}, 
 * {@link #next} and {@link #index} methods. It's mandatory to support the 
 * {@link #reset} method (as a no-op) on a newly constructed object, but
 * support for resetting efter some elements has been obtained is optional.
 * It's mandatory to support the {@link #length} method after all elements
 * has been obtains, but support for getting the length before the list has
 * been fully traversed is optional.  
 */
public interface LSPList
{

	/**
	 * Check if the list has more elements.
	 * This method must be idempotent, i.e. it must be possible to invoke 
	 * it several times without any side effects.
	 *
	 * @return <code>true</code> if there are more elements in the list.
	 */
	public boolean hasNext();


	/**
	 * Obtain the next element in the list.
	 *
	 * @return the next element in the list.
	 * @throws java.util.NoSuchElementException if there are no more 
	 *  elements in the list
	 */
	public Object next() throws java.util.NoSuchElementException;


	/**
	 * Get the number of elements obtained so far.
	 *
	 * @return the next element obtained so far.
	 */
	public int index();


	/**
	 * Obtain the length of the list.
	 *
	 * @return the length of the list.
	 * @throws java.lang.IllegalArgumentException if it's currently
	 *         not possible to check the number of elements in the list.
	 */
    public int length() throws java.lang.IllegalArgumentException;


	/**
	 * Reset the list and start over from the first element.
	 *
	 * @throws java.lang.IllegalArgumentException  if it's not possible to
	 *         reset this list.
	 */
	public void reset() throws java.lang.IllegalArgumentException;	
}


/**
 * Implementation of LSPList for Object[].
 */
class LSPArrayList implements LSPList
{
	private Object[] arr;
	private int curr;

	public LSPArrayList(Object[] arr)
	{
		this.arr = arr;
		curr = 0;
	}
	
	public boolean hasNext()
	{
		return (curr < arr.length);
	}

	public Object next() throws java.util.NoSuchElementException
	{
		try {
			return arr[curr++];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw new java.util.NoSuchElementException();
		}
	}

	public int index()
	{
		return curr;	
	}

    public int length()
	{
		return arr.length;	
	}

	public void reset()
	{
		curr = 0;	
	}	
	
}


/**
 * Implementation of LSPList for {@link java.util.Enumeration}.
 */
class LSPEnumerationList implements LSPList
{
    private int theLength = 0;
	private Enumeration enum;
	private boolean atEnd = false;

	public LSPEnumerationList(Enumeration enum)
	{
		this.enum = enum;
	}

	public boolean hasNext()
	{
		return enum.hasMoreElements();
	}

	public Object next() throws NoSuchElementException
	{
		try {
			Object o = enum.nextElement();
			theLength++;
			return o;
		}
		catch (NoSuchElementException e)
		{
			atEnd = true;
			throw e;
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
				"not possible to check length of an Enumeration");
	}

	public void reset() throws IllegalArgumentException
	{
		if (theLength > 0)
			throw new IllegalArgumentException(
				"not possible to reset an Enumeration");
	}
	
}


/**
 * Implementation of LSPList for {@link java.util.Iterator}.
 */
class LSPIteratorList implements LSPList
{
    private int theLength = 0;
	private Iterator iter;
	private boolean atEnd = false;

	public LSPIteratorList(Iterator iter)
	{
		this.iter = iter;
	}

	public boolean hasNext()
	{
		return iter.hasNext();
	}

	public Object next() throws NoSuchElementException
	{
		try {
			Object o = iter.next();
			theLength++;
			return o;
		}
		catch (NoSuchElementException e)
		{
			atEnd = true;
			throw e;
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
				"not possible to check length of an Enumeration");
	}

	public void reset() throws IllegalArgumentException
	{
		if (theLength > 0)
			throw new IllegalArgumentException(
				"not possible to reset an Enumeration");
	}
	
}

