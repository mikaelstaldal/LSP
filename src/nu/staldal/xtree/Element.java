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
import java.net.URL;

import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;


/**
 * An XML Element.
 */
@Deprecated
public class Element extends NodeWithChildren
{
	static final long serialVersionUID = -1804355746259349573L;

    final String namespaceURI;
    final String localName;

	URL baseURI = null;

    ArrayList<String> attrName;
    ArrayList<String> attrValue;
    ArrayList<String> attrType;

    ArrayList<String> namespacePrefixes;
    ArrayList<String> namespaceURIs;

    char xmlSpaceAttribute = ' ';

	/**
	 * Construct an element.
	 *
	 * @param namespaceURI  the namespace URI for this element,
	 *                      may be the empty string
	 * @param localName	the element name
	 *                       
	 */
    public Element(String namespaceURI, String localName)
    {
        this(namespaceURI, localName, -1, -1);
    }


	/**
	 * Construct an element.
	 *
	 * @param namespaceURI  the namespace URI for this element,
	 *                      may be the empty string
	 * @param localName	the element name
	 * @param numberOfAttributes  the number of attributes this element should have
	 */
    public Element(String namespaceURI, String localName,
                   int numberOfAttributes)
    {
        this(namespaceURI, localName, numberOfAttributes, -1);
    }


	/**
	 * Construct an element.
	 *
	 * @param namespaceURI  the namespace URI for this element,
	 *                      may be the empty string
	 * @param localName	the name of this element (no namespace)
	 * @param numberOfAttributes  the number of attributes this element should have
	 * @param numberOfChildren  the number of children this element should have
	 */
    public Element(String namespaceURI, String localName,
                   int numberOfAttributes, int numberOfChildren)
    {
        super(numberOfChildren);
        
        if (namespaceURI == null)
            namespaceURI = "";
        if (localName == null)
            throw new NullPointerException("LocalName may not be null");
        
        if (numberOfAttributes >= 0)
        {
            attrName = new ArrayList<String>(numberOfAttributes);
            attrValue = new ArrayList<String>(numberOfAttributes);
            attrType = new ArrayList<String>(numberOfAttributes);
        }
        else
        {
            attrName = new ArrayList<String>();
            attrValue = new ArrayList<String>();
            attrType = new ArrayList<String>();
        }
        namespaceURIs = new ArrayList<String>();
        namespacePrefixes = new ArrayList<String>();
        this.namespaceURI = namespaceURI;
        this.localName = localName;
    }


	/**
	 * Get the namespace URI for this element. May be the empty string.
     * 
	 * @return the namespace URI for this element
	 */
    public String getNamespaceURI()
    {
        return namespaceURI;
    }


	/**
	 * Get the name of this element. 
	 * The name does not include namespace URI or prefix.
     * 
	 * @return the name of this element
	 */
    public String getLocalName()
    {
        return localName;
    }


    /**
	 * Lookup the index of an attribute to this element. The returned index
	 * may be used as argument to other methods in this class.
	 *
	 * @param namespaceURI  the namespace URI, may be the empty string
	 * @param localName  the name
     * @return the index of the attribute, or -1 if no such attribute exists
	 *
	 * @see #getAttributeValue
	 * @see #getAttributeType
     */
    public int lookupAttribute(String namespaceURI, String localName)
    {
		return attrName.indexOf(localName + '^' + namespaceURI);
	}

	
	/**
	 * Add an attribute to this element.
	 *
	 * The attribute type is one of the strings 
	 * "CDATA", "ID", "IDREF", "IDREFS", "NMTOKEN", "NMTOKENS", 
	 * "ENTITY", "ENTITIES", or "NOTATION" (always in upper case).
	 *
	 * @param namespaceURI  the namespace URI, may be the empty string
	 * @param localName  the name
	 * @param type  the type (use "CDATA" if the type is irrelevant)
	 * @param value  the value
	 */
    public void addAttribute(String namespaceURI, String localName,
    						 String type, String value)
    {
		attrName.add(localName + '^' + namespaceURI);
		attrType.add(type);
		attrValue.add(value);
        
        if (namespaceURI.equals(XML_NS) && localName.equals("space"))
        {
            if (value.equals("preserve"))
                xmlSpaceAttribute = 'p';
            else if (value.equals("default"))
                xmlSpaceAttribute = 'd';
        }
	}
   
	
	/**
	 * Return the number of attributes this element have.
     * 
	 * @return the number of attributes 
	 */
	public int numberOfAttributes()
	{
		return attrName.size();
	}

	
	/**
	 * Get the namespace URI for the attribute at the specified index.
	 *
	 * @param index  the index as returned from {@link #lookupAttribute}
     *
	 * @return the namespace URI, may be (and is usually) the empty string,
	 *         or <code>null</code> if index is -1
	 * @throws IndexOutOfBoundsException  if no such attribute exist.
	 */
	public String getAttributeNamespaceURI(int index)
        throws IndexOutOfBoundsException
	{
        if (index == -1) return null;
		String s = attrName.get(index);
		return s.substring(s.indexOf('^')+1);
	}

	
	/**
	 * Get the name of the attribute at the specified index.
	 *
	 * @param index  the index as returned from {@link #lookupAttribute}
	 *
	 * @return the localName,
	 *         or <code>null</code> if index is -1
	 * @throws IndexOutOfBoundsException  if no such attribute exist.
	 */
	public String getAttributeLocalName(int index)
        throws IndexOutOfBoundsException
	{
        if (index == -1) return null;
		String s = attrName.get(index);
		return s.substring(0, s.indexOf('^'));
	}


	/**
	 * Get the type of the attribute at the specified index.
	 *
	 * The attribute type is one of the strings 
	 * "CDATA", "ID", "IDREF", "IDREFS", "NMTOKEN", "NMTOKENS", 
	 * "ENTITY", "ENTITIES", or "NOTATION" (always in upper case).
	 *
	 * @return the attribute type,
	 *         or <code>null</code> if index is -1
	 * @param index  the index as returned from {@link #lookupAttribute}
	 * @throws IndexOutOfBoundsException  if no such attribute exist.
	 */
	public String getAttributeType(int index)
        throws IndexOutOfBoundsException
	{
        if (index == -1) return null;
		return attrType.get(index);
	}


	/**
	 * Get the value of the attribute at the specified index.
	 *
	 * @return the attribute value,
	 *         or <code>null</code> if index is -1
	 * @param index  the index as returned from {@link #lookupAttribute}
	 * @throws IndexOutOfBoundsException  if no such attribute exist.
	 */
	public String getAttributeValue(int index)
        throws IndexOutOfBoundsException
	{
        if (index == -1) return null;
		return attrValue.get(index);
	}


	void setNamespaceMappings(ArrayList<String> prefixes, ArrayList<String> URIs)
	{
		namespacePrefixes = prefixes;
		namespaceURIs = URIs;
	}


	/**
	 * Add a namespace mapping to this element.
	 *
	 * @param prefix  the prefix
	 * @param URI  the namespace URI
	 */
	public void addNamespaceMapping(String prefix, String URI)
	{
		namespacePrefixes.add(prefix);
		namespaceURIs.add(URI);
	}


	/**
	 * Return the number of namespace mappings for this element.
     * 
	 * @return the number of namespace mappings 
	 */
	public int numberOfNamespaceMappings()
	{
		return namespacePrefixes.size();
	}
	
	/**
	 * Return a namespace mapping at the specified index.
     * 
	 * @param index  the index 
	 *
	 * @return a String[] with [0] = prefix, [1] = namespace URI
	 * @throws IndexOutOfBoundsException  if no such mapping exist.	 
	 */
	public String[] getNamespaceMapping(int index)
		throws IndexOutOfBoundsException
	{
		return new String[] {
			namespacePrefixes.get(index),
			namespaceURIs.get(index) };
	}


	@Override
    public String lookupNamespaceURI(String prefix)
	{
		int index = namespacePrefixes.indexOf(prefix);
		if (index == -1)
		{
			if (parent != null)
			{
				return parent.lookupNamespaceURI(prefix);
			}
			else
			{
				if (prefix.length() == 0)
				{
					return "";
				}
				else
				{
					return null;
				}
			}
		}
		else
		{
			return namespaceURIs.get(index);
		}
	}


	@Override
    public String lookupNamespacePrefix(String URI)
	{
		int index = namespaceURI.indexOf(URI);
		if (index == -1)
		{
			if (parent != null)
			{
				return parent.lookupNamespacePrefix(URI);
			}
			else
			{
				if (URI.length() == 0)
				{
					return "";
				}
				else
				{
					return null;
				}
			}
		}
		else
		{
			return namespacePrefixes.get(index);
		}
	}


	/**
	 * Set the baseURI property of this element.
	 *
	 * @param URI  the base URI, must be absolute
	 */
	public void setBaseURI(URL URI)
	{
		baseURI = URI;
	}

	
	@Override
    public URL getBaseURI()
	{
		if (baseURI != null)
		{
			return baseURI;
		}
		else
		{
			if (parent != null)
			{
				return parent.getBaseURI();
			}
			else
			{
				return null;
			}
		}
	}


	@Override
    public boolean getPreserveSpace()
	{
        switch (xmlSpaceAttribute)
        {
        case 'p':
            return true;
            
        case 'd':
            return false;
            
        default:
			if (parent != null)
			{
				return parent.getPreserveSpace();
			}
			else
			{
				return false;
			}
		}
	}
    

	@Override
    public String getInheritedAttribute(String namespaceURI, 
                                             String localName)
	{
		String val = getAttrValueOrNull(namespaceURI, localName);
        if (val != null)
            return val;        
        else if (parent == null)
			return null;
		else
			return parent.getInheritedAttribute(namespaceURI, localName);
	}
    

	/**
	 * Fire the startElement event to the given SAX2 ContentHandler.
	 * Will also fire startPrefixMapping events.
     * 
	 * @param sax the ContentHandler 
     *  
     * @throws SAXException if any of the ContentHandler methods throw it 
	 */
	public void outputStartElement(ContentHandler sax)
		throws SAXException
	{
		for (int i = 0; i < namespacePrefixes.size(); i++)
		{
			sax.startPrefixMapping(namespacePrefixes.get(i),
								   namespaceURIs.get(i));
		}

		AttributesImpl atts = new AttributesImpl();
		for (int i = 0; i < attrName.size(); i++)
		{
			String s = attrName.get(i);
			String URI = s.substring(s.indexOf('^')+1);
			String local = s.substring(0, s.indexOf('^'));

			atts.addAttribute(URI, local, "", attrType.get(i),
				attrValue.get(i));
		}
		sax.startElement(namespaceURI, localName, "", atts);
	}

	
	/**
	 * Fire the endElement event to the given SAX2 ContentHandler.
	 * Will also fire endPrefixMapping events.
     * 
     * @param sax the ContentHandler 
     *  
     * @throws SAXException if any of the ContentHandler methods throw it 
	 */
	public void outputEndElement(ContentHandler sax)
		throws SAXException
	{
		sax.endElement(namespaceURI, localName, "");

		for (int i = 0; i < namespacePrefixes.size(); i++)
		{
			sax.endPrefixMapping(namespacePrefixes.get(i));
		}
	}

	
	@Override
    public void toSAX(ContentHandler sax)
		throws SAXException
	{
		outputStartElement(sax);

		for (int i = 0; i < numberOfChildren(); i++)
		{
			getChild(i).toSAX(sax);
		}

		outputEndElement(sax);
	}

	
    /**
     * Shortcut method for getting the value of an attribute without 
	 * namespace.
     * 
     * @param localName  the local name of the attribute 
     *
     * @return the attrubute value, or <code>null</code>
	 * 		if the attribute doesn't exist
     */
	public String getAttrValueOrNull(String localName)
	{
        return getAttributeValue(lookupAttribute("", localName));
	}


    /**
     * Shortcut method for getting the value of an attribute without 
	 * namespace.
     *
     * @param localName  the local name of the attribute 
     *
     * @return the attrubute value, never <code>null</code>
     * 
	 * @throws SAXParseException if the attribute doesn't exist
     */
	public String getAttrValue(String localName)
		throws SAXParseException
	{
        String v = getAttrValueOrNull(localName);
		if (v == null)
			throw new SAXParseException("Attribute " + localName + " expected", this);
		else
			return v;
	}


    /**
     * Shortcut method for getting the value of an attribute with
     * namespace.
     * 
     * @param namespaceURI  the namespace URI of the attribute
     * @param localName  the local name of the attribute 
     *
     * @return the attrubute value, or <code>null</code>
	 * 		if the attribute doesn't exist
     */
	public String getAttrValueOrNull(String namespaceURI, String localName)
	{
        return getAttributeValue(lookupAttribute(namespaceURI, localName));
	}


    /**
     * Shortcut method for getting the value of an attribute with
     * namespace.
     *
     * @param namespaceURI  the namespace URI of the attribute
     * @param localName  the local name of the attribute 
     *
     * @return the attrubute value, never <code>null</code>
	 * @throws SAXParseException if the attribute doesn't exist
     */
	public String getAttrValue(String namespaceURI, String localName)
		throws SAXParseException
	{
        String v = getAttrValueOrNull(namespaceURI, localName);
		if (v == null)
			throw new SAXParseException("Attribute {" + namespaceURI + "}" + localName + " expected", this);
		else
			return v;
	}


    /**
     * Shortcut method for getting the text content of an Element.
     *
     * @return if there is a single Text child, return its value,
     *         if there is no children, return "",
     *         or <code>null</code> 
	 *         if there are more than one children or one non-Text child
     */
    public String getTextContentOrNull()
    {
        if (numberOfChildren() == 0)
        {
            return "";
        }
        else if (numberOfChildren() > 1)
        {
			return null;
        }
        else
        {
            Node node = getChild(0);
            if (!(node instanceof Text))
				return null;

            return ((Text)node).getValue();
        }
    }


    /**
     * Shortcut method for getting the text content of an Element.
     *
     * @return if there is a single Text child, return its value,
     *         if there is no children, return "",
     *         never <code>null</code>.
	 * @throws SAXParseException 
	 *         if there are more than one children or one non-Text child
     */
    public String getTextContent()
		throws SAXParseException
    {
		String s = getTextContentOrNull();
		
		if (s == null)
			throw new SAXParseException("No text content", this);
		else
			return s;
    }


    /**
     * Shortcut method for getting the first Element child with a
     * specified name.
     *
     * @param namespaceURI  the namespace URI of the element
     * @param localName  the local name of the element
     *
     * @return  the first child Element with the specified name,
     *          or <code>null</code> if there is no such child.
     */
    public Element getFirstChildElementOrNull(String namespaceURI, String localName)
    {
		for (int i = 0; i < numberOfChildren(); i++)
		if (getChild(i) instanceof Element)
		{
			Element e = (Element)getChild(i);
			if (e.getNamespaceURI().equals(namespaceURI)
					&& e.getLocalName().equals(localName))
			{
				return e;
			}
		}

		return null;
	}


    /**
     * Shortcut method for getting the first Element child with a
     * specified name.
     * 
     * @param namespaceURI  the namespace URI of the element
     * @param localName  the local name of the element
     *
     * @return  the first child Element with the specified name,
     *          never <code>null</code>.
	 * @throws SAXParseException 
	 *         if there is no such child.
     */
    public Element getFirstChildElement(String namespaceURI, String localName)
		throws SAXParseException
    {
		Element e = getFirstChildElementOrNull(namespaceURI, localName);

		if (e == null)
			throw new SAXParseException(
				"Element {" + namespaceURI + "}" + localName + " expected", this);
		else
			return e;
	}

	
    /**
     * Shortcut method for getting the first Element children with any name.
     *
     * @return  the first child Element
     *          or <code>null</code> if there are no Element children.
     */
    public Element getFirstChildElementOrNull()
    {
		for (int i = 0; i < numberOfChildren(); i++)
		if (getChild(i) instanceof Element)
		{
			return (Element)getChild(i);
		}

		return null;
    }
	

    /**
     * Shortcut method for getting the first Element children with any name.
     *
     * @return  the first child Element
     *          never <code>null</code>.
	 * @throws SAXParseException 
	 *         if there are no Element children.
     */
    public Element getFirstChildElement()
		throws SAXParseException
    {
		Element e = getFirstChildElementOrNull();
		
		if (e == null)
			throw new SAXParseException(
				"Element expected", this);
		else
			return e;
    }
}

