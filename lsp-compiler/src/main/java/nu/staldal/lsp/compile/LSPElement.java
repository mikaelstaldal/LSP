/*
 * Copyright (c) 2001-2003, Mikael Ståldal
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

import java.util.ArrayList;

import org.xml.sax.Locator;

import nu.staldal.lsp.compiler.LSPExpr;

public class LSPElement extends LSPContainer
{
    String namespaceURI = null;
    String localName = null;
	
	LSPExpr namespaceURIExpr;
	LSPExpr localNameExpr;

	ArrayList<LSPExpr> attrNamespace;
    ArrayList<LSPExpr> attrName;
    ArrayList<LSPExpr> attrValue;
    ArrayList<String> attrType;

    ArrayList<String> namespacePrefixes;
    ArrayList<String> namespaceURIs;


    public LSPElement(String namespaceURI, String localName,
           		      int numberOfAttributes, int numberOfChildren,
                      Locator locator)
    {
        super(numberOfChildren, locator);
        if (numberOfAttributes >= 0)
        {
			attrNamespace = new ArrayList<LSPExpr>(numberOfAttributes);
            attrName = new ArrayList<LSPExpr>(numberOfAttributes);
            attrValue = new ArrayList<LSPExpr>(numberOfAttributes);
            attrType = new ArrayList<String>(numberOfAttributes);
        }
        else
        {
            attrNamespace = new ArrayList<LSPExpr>();
            attrName = new ArrayList<LSPExpr>();
            attrValue = new ArrayList<LSPExpr>();
            attrType = new ArrayList<String>();
        }
        namespaceURIs = new ArrayList<String>();
        namespacePrefixes = new ArrayList<String>();
        this.namespaceURI = namespaceURI;
        this.localName = localName;
    }


    public LSPElement(LSPExpr namespaceURI, LSPExpr localName,
           		      int numberOfAttributes, int numberOfChildren,
                      Locator locator)
    {
        super(numberOfChildren, locator);
        if (numberOfAttributes >= 0)
        {
            attrNamespace = new ArrayList<LSPExpr>(numberOfAttributes);
            attrName = new ArrayList<LSPExpr>(numberOfAttributes);
            attrValue = new ArrayList<LSPExpr>(numberOfAttributes);
            attrType = new ArrayList<String>(numberOfAttributes);
        }
        else
        {
            attrNamespace = new ArrayList<LSPExpr>();
            attrName = new ArrayList<LSPExpr>();
            attrValue = new ArrayList<LSPExpr>();
            attrType = new ArrayList<String>();
        }
        namespaceURIs = new ArrayList<String>();
        namespacePrefixes = new ArrayList<String>();
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
	
	

    public void addAttribute(LSPExpr namespaceURI, LSPExpr localName,
    						 String type, LSPExpr value)
    {
		attrNamespace.add(namespaceURI);
		attrName.add(localName);
		attrType.add(type);
		attrValue.add(value);
	}

	public int numberOfAttributes()
	{
		return attrName.size();
	}

	public LSPExpr getAttributeNamespaceURI(int index)
	{
        if (index == -1) return null;
		return attrNamespace.get(index);
	}

	public LSPExpr getAttributeLocalName(int index)
	{
        if (index == -1) return null;
		return attrName.get(index);
	}

	public String getAttributeType(int index)
	{
        if (index == -1) return null;
		return attrType.get(index);
	}

	public LSPExpr getAttributeValue(int index)
	{
        if (index == -1) return null;
		return attrValue.get(index);
	}


	public void addNamespaceMapping(String prefix, String URI)
	{
		namespacePrefixes.add(prefix);
		namespaceURIs.add(URI);
	}

	public int numberOfNamespaceMappings()
	{
		return namespacePrefixes.size();
	}

	public String[] getNamespaceMapping(int index)
		throws ArrayIndexOutOfBoundsException
	{
		return new String[] {
			namespacePrefixes.get(index),
			namespaceURIs.get(index) };
	}

}
