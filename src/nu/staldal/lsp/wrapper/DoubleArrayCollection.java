/*
 * Copyright (c) 2005 Mikael Ståldal
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
 * Wrap an double[] as a Collection
 */
public class DoubleArrayCollection implements Collection
{
    private final double[] arr;
    
    public DoubleArrayCollection(double[] arr)
    {
        this.arr = arr;    
    }
	

    public Iterator iterator()
    {
        return new DoubleArrayIterator(arr);        
    }

    
    public int size()
	{
        return arr.length;
	}

    public boolean isEmpty()
    {
        return size() == 0;    
    }    

    
    public boolean contains(Object o)
    {
        throw new UnsupportedOperationException();    
    }

    public Object[] toArray()
    {
        throw new UnsupportedOperationException();    
    }

    public Object[] toArray(Object a[])
    {
        throw new UnsupportedOperationException();    
    }

    public boolean add(Object o)
    {
        throw new UnsupportedOperationException();    
    }

    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException();    
    }

    public boolean containsAll(Collection c)
    {
        throw new UnsupportedOperationException();    
    }

    public boolean addAll(Collection c)
    {
        throw new UnsupportedOperationException();    
    }

    public boolean addAll(int index, Collection c)
    {
        throw new UnsupportedOperationException();    
    }

    public boolean removeAll(Collection c)
    {
        throw new UnsupportedOperationException();    
    }

    public boolean retainAll(Collection c)
    {
        throw new UnsupportedOperationException();    
    }

    public void clear()
    {
        throw new UnsupportedOperationException();    
    }

    public boolean equals(Object o)
    {
        throw new UnsupportedOperationException();    
    }

    public int hashCode()
    {
        throw new UnsupportedOperationException();    
    }


    static class DoubleArrayIterator implements Iterator
    {
        private final double[] arr;        
        private int index;
        
        
        DoubleArrayIterator(double[] arr)
        {
            this.arr = arr;
            this.index = 0;
        }
        
        public boolean hasNext()
        {
            return index < arr.length;
        }
    
        public Object next() throws NoSuchElementException
        {
            try {
                // Should use Double.valueOf() in Java 1.5
                return new Double(arr[index++]);
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                throw new NoSuchElementException();
            }
        }
    
        public void remove()
        {
            throw new UnsupportedOperationException();    
        }
    }
        	
}
