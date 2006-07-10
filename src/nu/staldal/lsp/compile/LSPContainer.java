/*
 * Copyright (c) 2001-2004, Mikael Ståldal
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

package nu.staldal.lsp.compile;

import java.util.Vector;

import org.xml.sax.Locator;


public abstract class LSPContainer extends LSPNode
{
    private Vector<LSPNode> children;

    public LSPContainer(int capacity, Locator locator)
    {
        super(locator);
        
        if (capacity >= 0)
            children = new Vector<LSPNode>(capacity);
        else
            children = new Vector<LSPNode>();
    }

    public int numberOfChildren()
    {
        return children.size();
    }

    public LSPNode getChild(int index)
        throws ArrayIndexOutOfBoundsException
    {
        return (LSPNode)children.elementAt(index);
    }

    public void addChild(LSPNode newChild)
    {
        children.addElement(newChild);
    }

    public LSPNode replaceChild(LSPNode newChild, int index)
        throws ArrayIndexOutOfBoundsException
    {
        LSPNode oldChild = (LSPNode)children.elementAt(index);
        children.setElementAt(newChild, index);
        return oldChild;
    }

    /**
     * Inefficient
     */
    public LSPNode removeChild(int index)
        throws ArrayIndexOutOfBoundsException
    {
        LSPNode child = (LSPNode)children.elementAt(index);
        children.removeElementAt(index);
        return child;
    }

    /**
     * Inefficient
     */
    public void insertChild(LSPNode newChild, int index)
        throws ArrayIndexOutOfBoundsException
    {
        children.insertElementAt(newChild, index);
    }
}
