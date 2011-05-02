/*
 * Copyright (c) 2008, Mikael Ståldal
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

package nu.staldal.xmltree;

import java.util.ArrayList;
import java.util.Map;
import java.net.URL;

import javax.xml.namespace.QName;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * An XML Element.
 */
public class Element extends NodeWithChildren {
    private static final long serialVersionUID = 8132648774624504877L;

    final QName name;

    URL baseURI = null;

    ArrayList<String> namespacePrefixes;

    ArrayList<String> namespaceURIs;
    
    final AttributeMap attributeMap;

    /**
     * Construct an element.
     * 
     * @param namespaceURI
     *            the namespace URI for this element, may be the empty string
     * @param localName
     *            the element name
     * 
     */
    public Element(String namespaceURI, String localName) {
        this(namespaceURI, localName, -1, -1);
    }

    /**
     * Construct an element.
     * 
     * @param namespaceURI
     *            the namespace URI for this element, may be the empty string
     * @param localName
     *            the element name
     * @param numberOfAttributes
     *            the number of attributes this element should have
     */
    public Element(String namespaceURI, String localName, int numberOfAttributes) {
        this(namespaceURI, localName, numberOfAttributes, -1);
    }

    /**
     * Construct an element.
     * 
     * @param namespaceURI
     *            the namespace URI for this element, may be the empty string
     * @param localName
     *            the name of this element (no namespace)
     * @param numberOfAttributes
     *            the number of attributes this element should have
     * @param numberOfChildren
     *            the number of children this element should have
     */
    public Element(String namespaceURI, String localName,
            int numberOfAttributes, int numberOfChildren) {
        super(numberOfChildren);

        if (namespaceURI == null)
            namespaceURI = "";
        if (localName == null)
            throw new NullPointerException("LocalName may not be null");

        namespaceURIs = new ArrayList<String>();
        namespacePrefixes = new ArrayList<String>();
        this.name = new QName(namespaceURI, localName);
        
        this.attributeMap = new AttributeMap(numberOfAttributes);
    }
    
    /**
     * Pseudo-copy constructor, do not copy the children, but allocate room for them.
     * 
     * @param element  the element to copy
     */
    public Element(Element element) {
        super(element);        
        this.name = element.name;
        this.baseURI = element.baseURI;
        this.namespacePrefixes = new ArrayList<String>(element.namespacePrefixes);
        this.namespaceURIs= new ArrayList<String>(element.namespaceURIs);
        this.attributeMap = new AttributeMap(element.attributeMap);
    }

    /**
     * Get the namespace URI for this element. May be the empty string.
     * 
     * @return the namespace URI for this element
     */
    public String getNamespaceURI() {
        return name.getNamespaceURI();
    }

    /**
     * Get the name of this element. The name does not include namespace URI or
     * prefix.
     * 
     * @return the name of this element
     */
    public String getLocalName() {
        return name.getLocalPart();
    }
        
    /**
     * Get the qualified name of this element. 
     * 
     * @return the qualified name of this element
     */
    public String getName() {
        return name.toString();
    }

    void setNamespaceMappings(ArrayList<String> prefixes, ArrayList<String> URIs) {
        namespacePrefixes = prefixes;
        namespaceURIs = URIs;
    }

    /**
     * Add a namespace mapping to this element.
     * 
     * @param prefix
     *            the prefix
     * @param URI
     *            the namespace URI
     */
    public void addNamespaceMapping(String prefix, String URI) {
        namespacePrefixes.add(prefix);
        namespaceURIs.add(URI);
    }

    /**
     * Return the number of namespace mappings for this element.
     * 
     * @return the number of namespace mappings
     */
    public int numberOfNamespaceMappings() {
        return namespacePrefixes.size();
    }

    /**
     * Return a namespace mapping at the specified index.
     * 
     * @param index
     *            the index
     * 
     * @return a String[] with [0] = prefix, [1] = namespace URI
     * @throws IndexOutOfBoundsException
     *             if no such mapping exist.
     */
    public String[] getNamespaceMapping(int index)
            throws IndexOutOfBoundsException {
        return new String[] { namespacePrefixes.get(index),
                namespaceURIs.get(index) };
    }

    @Override
    public String lookupNamespaceURI(String prefix) {
        int index = namespacePrefixes.indexOf(prefix);
        if (index == -1) {
            if (parent != null) {
                return parent.lookupNamespaceURI(prefix);
            }
            else {
                if (prefix.length() == 0) {
                    return "";
                }
                else {
                    return null;
                }
            }
        }
        else {
            return namespaceURIs.get(index);
        }
    }

    @Override
    public String lookupNamespacePrefix(String URI) {
        int index = name.getNamespaceURI().indexOf(URI);
        if (index == -1) {
            if (parent != null) {
                return parent.lookupNamespacePrefix(URI);
            }
            else {
                if (URI.length() == 0) {
                    return "";
                }
                else {
                    return null;
                }
            }
        }
        else {
            return namespacePrefixes.get(index);
        }
    }

    /**
     * Set the baseURI property of this element.
     * 
     * @param URI
     *            the base URI, must be absolute
     */
    public void setBaseURI(URL URI) {
        baseURI = URI;
    }

    @Override
    public URL getBaseURI() {
        if (baseURI != null) {
            return baseURI;
        }
        else {
            if (parent != null) {
                return parent.getBaseURI();
            }
            else {
                return null;
            }
        }
    }

    @Override
    public boolean getPreserveSpace() {
        switch (attributeMap.xmlSpaceAttribute) {
        case 'p':
            return true;

        case 'd':
            return false;

        default:
            if (parent != null) {
                return parent.getPreserveSpace();
            }
            else {
                return false;
            }
        }
    }

    @Override
    public String getInheritedAttribute(String name) {
        String val = attributeMap.get(name);
        if (val != null)
            return val;
        else if (parent == null)
            return null;
        else
            return parent.getInheritedAttribute(name);
    }

    /**
     * Fire the startElement event to the given SAX2 ContentHandler. Will also
     * fire startPrefixMapping events.
     * 
     * @param sax
     *            the ContentHandler
     * 
     * @throws SAXException
     *             if any of the ContentHandler methods throw it
     */
    public void outputStartElement(ContentHandler sax) throws SAXException {
        outputStartElement(sax, attributeMap);
    }

    /**
     * Fire the startElement event to the given SAX2 ContentHandler. Will also
     * fire startPrefixMapping events. Override attributes.
     * 
     * @param sax
     *            the ContentHandler
     * @param attributes
     *            the attributes to use instead of the normal ones            
     * 
     * @throws SAXException
     *             if any of the ContentHandler methods throw it
     */
    public void outputStartElement(ContentHandler sax, Map<String, String> attributes) 
            throws SAXException {
        for (int i = 0; i < namespacePrefixes.size(); i++) {
            sax.startPrefixMapping(namespacePrefixes.get(i), namespaceURIs
                    .get(i));
        }

        AttributesImpl atts = new AttributesImpl();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            QName qname = QName.valueOf(entry.getKey());

            atts.addAttribute(qname.getNamespaceURI(), qname.getLocalPart(),
                    "", "CDATA", entry.getValue());
        }
        sax.startElement(name.getNamespaceURI(), name.getLocalPart(), "", atts);
    }        
    
    /**
     * Fire the endElement event to the given SAX2 ContentHandler. Will also
     * fire endPrefixMapping events.
     * 
     * @param sax
     *            the ContentHandler
     * 
     * @throws SAXException
     *             if any of the ContentHandler methods throw it
     */
    public void outputEndElement(ContentHandler sax) throws SAXException {
        sax.endElement(name.getNamespaceURI(), name.getLocalPart(), "");

        for (int i = 0; i < namespacePrefixes.size(); i++) {
            sax.endPrefixMapping(namespacePrefixes.get(i));
        }
    }

    @Override
    public void toSAX(ContentHandler sax) throws SAXException {
        outputStartElement(sax);

        for (Node node : this) {
            node.toSAX(sax);
        }

        outputEndElement(sax);
    }

    /**
     * Shortcut method for getting the text content of an Element.
     * 
     * @return if there is a single Text child, return its value, if there is no
     *         children, return "", or <code>null</code> if there are more
     *         than one children or one non-Text child
     */
    public String getTextContentOrNull() {
        if (isEmpty()) {
            return "";
        }
        else if (size() > 1) {
            return null;
        }
        else {
            Node node = get(0);
            if (!(node instanceof Text))
                return null;

            return ((Text)node).getValue();
        }
    }

    /**
     * Shortcut method for getting the text content of an Element.
     * 
     * @return if there is a single Text child, return its value, if there is no
     *         children, return "", never <code>null</code>.
     * @throws SAXParseException
     *             if there are more than one children or one non-Text child
     */
    public String getTextContent() throws SAXParseException {
        String s = getTextContentOrNull();

        if (s == null)
            throw new SAXParseException("No text content", this);
        else
            return s;
    }

    /**
     * Add an attribute to this element.
     * 
     * @param name  attribute name
     * @param value attribute value
     */
    public void addAttribute(String name, String value) {
        attributeMap.put(name, value);        
    }

    /**
     * Shortcut method for getting the value of an attribute.
     * 
     * @param name
     *            the (qualified) name of the attribute
     * 
     * @return the attrubute value, or <code>null</code> if the attribute doesn't exist
     */
    public String getAttributeOrNull(String name) {
        return attributeMap.get(name);
    }

    /**
     * Shortcut method for getting the value of an attribute.
     * 
     * @param name
     *            the (qualified) name of the attribute
     * 
     * @return the attrubute value, never <code>null</code>
     * 
     * @throws SAXParseException
     *             if the attribute doesn't exist
     */
    public String getAttribute(String name) throws SAXParseException {
        String v = getAttributeOrNull(name);
        if (v == null)
            throw new SAXParseException("Attribute " + name + " expected", this);
        else
            return v;
    }
    
    /**
     * Get the Map with attributes for this element.
     * 
     * @return the Map with attributes for this element
     */
    public Map<String,String> getAttributes() {
        return attributeMap;
    }
}
