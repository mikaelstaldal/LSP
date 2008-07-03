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

import java.net.URL;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


/**
 * Base class for a node in an XMLTree.
 */
public abstract class Node implements java.io.Serializable, Locator {
    private String publicId = null;

    private String systemId = null;

    private int line = -1;

    private int column = -1;

    protected NodeWithChildren parent = null;

    void setParent(NodeWithChildren n) {
        parent = n;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Default constructor.
     */
    public Node() {
        // nothing to do
    }
    
    /**
     * Copy constructor.
     * 
     * @param node  the node to copy
     */
    protected Node(Node node) {
        publicId = node.publicId;
        systemId = node.systemId;
        line = node.line;
        column = node.column;
        parent = node.parent;
    }
    
    /**
     * Get the parent of this node.
     * 
     * @return the parent of this node, or <code>null</code> if this node has
     *         no parent.
     */
    public NodeWithChildren getParent() {
        return parent;
    }

    /**
     * Serialize this node, and recursively the (sub)tree beneath, into SAX2
     * events.
     * 
     * @param sax
     *            the SAX2 ContentHander to fire events on.
     * 
     * @throws SAXException
     *             if any of the ContentHandler methods throw it
     */
    public abstract void toSAX(ContentHandler sax) throws SAXException;

    /**
     * Return the public identifier for this node. Useful for error reporting.
     * 
     * The return value is the public identifier of the document entity or of
     * the external parsed entity.
     * 
     * @return A string containing the public identifier, or null if none is
     *         available.
     */
    public String getPublicId() {
        return publicId;
    }

    /**
     * Return the system identifier for this node. Useful for error reporting.
     * 
     * The return value is the system identifier of the document entity or of
     * the external parsed entity.
     * 
     * @return A string containing the system identifier, or null if none is
     *         available.
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * Return the line number where this node ends. Useful for error reporting.
     * 
     * The return value is an approximation of the line number in the document
     * entity or external parsed entity.
     * 
     * The first line in the document is line 1.
     * 
     * @return The line number, or -1 if none is available.
     * @see #getColumnNumber()
     */
    public int getLineNumber() {
        return line;
    }

    /**
     * Return the column number where this node ends. Useful for error
     * reporting.
     * 
     * The return value is an approximation of the column number in the document
     * entity or external parsed entity.
     * 
     * The first column in each line is column 1.
     * 
     * @return The column number, or -1 if none is available.
     * @see #getLineNumber()
     */
    public int getColumnNumber() {
        return column;
    }

    /**
     * Lookup the namespace URI which has been mapped to a prefix.
     * 
     * @param prefix
     *            the prefix, may be the empty string which denotes the default
     *            namespace.
     * 
     * @return the namespace URI, or <code>null</code> if the prefix is not
     *         mapped to any namespace URI, or the empty string of prefix is the
     *         empty string and there is no default namespace mapping.
     */
    public String lookupNamespaceURI(String prefix) {
        if (parent == null)
            return null;
        else
            return parent.lookupNamespaceURI(prefix);
    }

    /**
     * Lookup a prefix which has been mapped to a namespace URI.
     * 
     * @param URI
     *            the namespace URI
     * 
     * @return any of the prefixes which has been mapped to the namespace URI,
     *         or <code>null</code> if no prefix is mapped to the namespace
     *         URI.
     */
    public String lookupNamespacePrefix(String URI) {
        if (parent == null)
            return null;
        else
            return parent.lookupNamespacePrefix(URI);
    }

    /**
     * Returns the absolute base URI of this node.
     * 
     * @return the absolute base URI of this node, or <code>null</code> if
     *         unknown.
     */
    public URL getBaseURI() {
        if (parent == null)
            return null;
        else
            return parent.getBaseURI();
    }

    /**
     * Return the value of any xml:space attribute in force for this node.
     * 
     * @return <code>true</code> if an xml:space="preserve" is in effect
     */
    public boolean getPreserveSpace() {
        if (parent == null)
            return false;
        else
            return parent.getPreserveSpace();
    }

    /**
     * Return the value of an inherited attribute. If the given attribute occurs
     * on this node, return its value, otherwise recursivley search the parent
     * of this node (return <code>null</code> if the root is reached without
     * finding the attribute). Useful for e.g. xml:lang.
     * 
     * @param name
     *            the attribute name
     * 
     * @return <code>null</code> if no such attribute is found
     */
    public String getInheritedAttribute(String name) {
        if (parent == null)
            return null;
        else
            return parent.getInheritedAttribute(name);
    }

    /**
     * Check if this node consist of whitespace only.
     * 
     * @return <code>true</code> if and only if this is a Text node which
     *         contains no other characters than whitespace.
     */
    public boolean isWhitespaceNode() {
        return false;
    }

}
