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

package nu.staldal.lsp.compile;

import java.util.Vector;

import nu.staldal.lsp.LSPExpr;

public class LSPElement extends LSPContainer
{
	static final long serialVersionUID = -18043557462593495L;

    String namespaceURI = null;
    String localName = null;
	
	LSPExpr namespaceURIExpr;
	LSPExpr localNameExpr;

    Vector attrName;
    Vector attrValue;
    Vector attrType;

    Vector namespacePrefixes;
    Vector namespaceURIs;


    public LSPElement(String namespaceURI, String localName,
           		      int numberOfAttributes, int numberOfChildren)
    {
        super(numberOfChildren);
        if (numberOfAttributes >= 0)
        {
            attrName = new Vector(numberOfAttributes);
            attrValue = new Vector(numberOfAttributes);
            attrType = new Vector(numberOfAttributes);
        }
        else
        {
            attrName = new Vector();
            attrValue = new Vector();
            attrType = new Vector();
        }
        namespaceURIs = new Vector();
        namespacePrefixes = new Vector();
        this.namespaceURI = namespaceURI;
        this.localName = localName;
    }


    public LSPElement(LSPExpr namespaceURI, LSPExpr localName,
           		      int numberOfAttributes, int numberOfChildren)
    {
        super(numberOfChildren);
        if (numberOfAttributes >= 0)
        {
            attrName = new Vector(numberOfAttributes);
            attrValue = new Vector(numberOfAttributes);
            attrType = new Vector(numberOfAttributes);
        }
        else
        {
            attrName = new Vector();
            attrValue = new Vector();
            attrType = new Vector();
        }
        namespaceURIs = new Vector();
        namespacePrefixes = new Vector();
        this.namespaceURIExpr = namespaceURI;
        this.localNameExpr = localName;
    }


    public String getNamespaceURI()
    {
        return namespaceURI;
    }

    public String getLocalName()
    {
        return localName;
    }

    public LSPExpr getNamespaceURIExpr()
    {
        return namespaceURIExpr;
    }

    public LSPExpr getLocalNameExpr()
    {
        return localNameExpr;
    }
	
	
    /**
     * @return -1 if not found
     */
    public int lookupAttribute(String namespaceURI, String localName)
    {
		return attrName.indexOf(localName + '^' + namespaceURI);
	}

    public void addAttribute(String namespaceURI, String localName,
    						 String type, LSPExpr value)
    {
		attrName.addElement(localName + '^' + namespaceURI);
		attrType.addElement(type);
		attrValue.addElement(value);
	}

    public void removeAttribute(int index)
        throws ArrayIndexOutOfBoundsException
    {
        attrName.removeElementAt(index);
        attrType.removeElementAt(index);
        attrValue.removeElementAt(index);
    }

	public int numberOfAttributes()
	{
		return attrName.size();
	}

	public String getAttributeNamespaceURI(int index)
	{
        if (index == -1) return null;
		String s = (String)attrName.elementAt(index);
		return s.substring(s.indexOf('^')+1);
	}

	public String getAttributeLocalName(int index)
	{
        if (index == -1) return null;
		String s = (String)attrName.elementAt(index);
		return s.substring(0, s.indexOf('^'));
	}

	public String getAttributeType(int index)
	{
        if (index == -1) return null;
		return (String)attrType.elementAt(index);
	}

	public LSPExpr getAttributeValue(int index)
	{
        if (index == -1) return null;
		return (LSPExpr)attrValue.elementAt(index);
	}


	public void addNamespaceMapping(String prefix, String URI)
	{
		namespacePrefixes.addElement(prefix);
		namespaceURIs.addElement(URI);
	}

	public int numberOfNamespaceMappings()
	{
		return namespacePrefixes.size();
	}

	public String[] getNamespaceMapping(int index)
		throws ArrayIndexOutOfBoundsException
	{
		return new String[] {
			(String)namespacePrefixes.elementAt(index),
			(String)namespaceURIs.elementAt(index) };
	}

}
