/*
 * Copyright (c) 2002-2003, Mikael St�ldal
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

package nu.staldal.util;

import java.util.*;

/**
 * An Environment is used to store bindings from keys to values.
 * <p>
 * Keys and values are non-null Objects.
 * <p>
 * An environment consists of several <em>frames</em> in a stack.
 * All modifications are made in the <em>current frame</em> 
 * (top of the stack), but a lookup will search all other frames if
 * a key is not found in the current frame. When a frame is poped,
 * all modifications made since the last push is discarded and the original
 * state of the Environment is restored. An Environment always has at least
 * one frame, which cannot be popped.
 * <p>
 * This class is useful for handling variable bindings in an interpreter
 * for language with scoped access to variables.
 * <p>
 * This class is not thread-safe, you need to synchronize concurrent access.
 */
public class Environment 
{
	private Frame currentFrame;
	
	/**
	 * Create a new Environment with one empty frame.
	 */
	public Environment()
	{
		currentFrame = new Frame();
	}

	/**
	 * Create a new Environment initialized with the binding in a Map.
     *
     * @param initial  the Map with initial bindings
	 */
	public Environment(Map initial)
	{
		currentFrame = new Frame(initial);
	}

	/**
	 * Lookup the value bound to the given key.
	 *
	 * @param key  the key
	 *
	 * @return the value bound to the given key, 
	 * or <code>null</code> if no value is bound to the given key.
	 */
	public Object lookup(Object key)
	{
		if (key == null) 
			throw new NullPointerException("Key may not be null");
		
		return currentFrame.lookup(key);
	}
	
	/**
	 * Bind a value to the given key. Will replace any previous value
	 * for the given key in the current frame, or shadow any value for the
	 * current key in any parent frame.
	 *
	 * @param key    the key, may not be <code>null</code>
	 * @param value  the value, may not be <code>null</code>
	 *
	 * @return the previous value for the given key, 
	 *         or <code>null</code> if the given key has no value
	 *         <em>in the current frame</em>
	 */
	public Object bind(Object key, Object value)
	{
		if (key == null) 
			throw new NullPointerException("Key may not be null");
		if (value == null) 
			throw new NullPointerException("Value may not be null");
		
		return currentFrame.bind(key, value);
	}

	/**
	 * Unbind any value from the given key. Will only unbind values in the
	 * current frame, has no effect if the key has a value in any parent
	 * frame.
	 *
	 * @param key    the key, may not be <code>null</code>
	 *
	 * @return the previous value for the given key, 
	 *         or <code>null</code> if the given key has no value
	 *         <em>in the current frame</em>
	 */
	public Object unbind(Object key)
	{
		if (key == null) 
			throw new NullPointerException("Key may not be null");
		
		return currentFrame.unbind(key);
	}
	
	/**
	 * Push a new frame on the frame stack.
	 */
	public void pushFrame()
	{
		currentFrame = new Frame(currentFrame);	
	}


	/**
	 * Pop a frame from the frame stack. Any bindings in the current frame
	 * will be discarded.
	 *
	 * @throws java.util.EmptyStackException if an attempt is made to
	 * pop the last frame.
	 */
	public void popFrame()
	{
		Frame parentFrame = currentFrame.getParent();
		if (parentFrame == null)
			throw new EmptyStackException();
		else
			currentFrame = parentFrame;	
	}


	static class Frame
	{
		private Frame parent;
		private Map map;
		
		Frame(Frame p)
		{
			parent = p;
			map = new HashMap();
		}
	
		Frame()
		{
			parent = null;
			map = new HashMap();
		}	

		Frame(Map initial)
		{
			parent = null;
            map = initial;
		}	

		Frame getParent()
		{
			return parent;	
		}
		
		Object lookup(Object key)
		{
			Object obj = map.get(key);
			if (obj == null && parent != null)
			{
				obj = parent.lookup(key);	
			}
			return obj;
		}
		
		Object bind(Object key, Object value)
		{
			return map.put(key, value);
		}
	
		Object unbind(Object key)
		{
			return map.remove(key);
		}			
	}
}

