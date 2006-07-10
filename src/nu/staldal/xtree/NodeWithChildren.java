/*
 * Copyright (c) 2001, Mikael Ståldal
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

package nu.staldal.xtree;

import java.util.ArrayList;


/**
 * Base class for a node with children. The children are ordered. 
 */
public abstract class NodeWithChildren extends Node
{
    private ArrayList<Node> children;

	/**
	 * Construct a node which children.
	 *
	 * @param capacity  the number of children this node should have,
	 *                  use -1 if unknown
	 */
    public NodeWithChildren(int capacity)
    {
        if (capacity >= 0)
            children = new ArrayList<Node>(capacity);
        else
            children = new ArrayList<Node>();
    }


	/**
	 * Get the current number of children this node have.
	 */
    public int numberOfChildren()
    {
        return children.size();
    }


	/**
	 * Get a specific child of this node.
	 *
	 * @param index  index of the node to get, the first child is 0.
	 *
	 * @throws IndexOutOfBoundsException  if no such child exist.
	 */
    public Node getChild(int index)
        throws IndexOutOfBoundsException
    {
        return (Node)children.get(index);
    }

	
	/**
     * Add a new child to this node, last in sequence.
	 */
    public void addChild(Node newChild)
    {
        newChild.setParent(this);
        children.add(newChild);
    }
	

	/**
	 * Replace a child with another.
	 *
	 * @param newChild  the new child
	 * @param index  index of the child to replace
	 *
	 * @return  the replaced child
	 * @throws IndexOutOfBoundsException  if no such child exist
	 */
    public Node replaceChild(Node newChild, int index)
        throws IndexOutOfBoundsException
    {
        Node oldChild = (Node)children.get(index);
        oldChild.setParent(null);
        newChild.setParent(this);
        children.set(index, newChild);
        return oldChild;
    }
	
}

