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

package nu.staldal.lsp;

import java.util.Map;


/**
 * A special implementation of {@link java.util.Map} which pretends to have
 * all possible keys. The {@link #get} method return <code>Void.TYPE</code> for
 * all keys. The {@link #containsKey} method return <code>true</code> for
 * all keys. All other methods throws
 * {@link java.lang.UnsupportedOperationException}.
 *<p>
 * Implements the Singleton pattern.
 */
public class FullMap implements Map<String,Object>
{
    private static final FullMap theInstance = new FullMap();
       
    /**
     * Singleton.
     * 
     * @return the instance 
     */
    public static Map<String,Object> getInstance()
    {                
        return theInstance;
    }
    
    private FullMap() 
    {
        // nothing to do
    }
    
    /**
     * @return <code>Void.TYPE</code> for all keys
     */
    public Object get(Object key)
    {
        return Void.TYPE; 
    }
    
    public boolean containsKey(Object key)
    {
        return true;    
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
    
    public Object put(String key, Object value)
    {
        throw new UnsupportedOperationException();    
    }
        
    public Object remove(Object key)
    {
        throw new UnsupportedOperationException();    
    }

    public void putAll(Map<? extends String,?> t)
    {
        throw new UnsupportedOperationException();    
    }

    public void clear()
    {
        throw new UnsupportedOperationException();    
    }

    public java.util.Set<String> keySet()
    {
        throw new UnsupportedOperationException();    
    }

    public java.util.Collection<Object> values()
    {
        throw new UnsupportedOperationException();    
    }

    public java.util.Set<Map.Entry<String, Object>> entrySet()
    {
        throw new UnsupportedOperationException();    
    }    
}
