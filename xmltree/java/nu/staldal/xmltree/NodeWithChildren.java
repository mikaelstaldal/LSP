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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXParseException;

/**
 * Base class for a node with children. The children are ordered.
 */
public abstract class NodeWithChildren extends Node {
    private final NodeList children;

    /**
     * Construct a node which children.
     */
    public NodeWithChildren() {
        this(-1);
    }

    /**
     * Construct a node which children.
     * 
     * @param capacity
     *            the number of children this node should have, use -1 if
     *            unknown
     */
    public NodeWithChildren(int capacity) {
        children = new NodeList(this, capacity);
    }

    /**
     * Return all children.
     * 
     * @return all children.
     */
    public List<Node> getChildren() {
        return children;
    }

    /**
     * Shortcut method for getting the first Element child with a specified
     * name.
     * 
     * @param name  the qualified name of the element
     * 
     * @return the first child Element with the specified name, or
     *         <code>null</code> if there is no such child.
     */
    public Element getFirstChildElementOrNull(String name) {
        for (Node node : getChildren()) {
            if (node instanceof Element) {
                Element e = (Element)node;
                if (e.getName().equals(name)) {
                    return e;
                }
            }
        }

        return null;
    }

    /**
     * Shortcut method for getting the first Element child with a specified
     * name.
     * 
     * @param name  the qualified name of the element
     * 
     * @return the first child Element with the specified name, never
     *         <code>null</code>.
     * @throws SAXParseException
     *             if there is no such child.
     */
    public Element getFirstChildElement(String name)
            throws SAXParseException {
        Element e = getFirstChildElementOrNull(name);

        if (e == null)
            throw new SAXParseException("Element " + name + " expected", this);
        else
            return e;
    }

    /**
     * Shortcut method for getting the first Element children with any name.
     * 
     * @return the first child Element or <code>null</code> if there are no
     *         Element children.
     */
    public Element getFirstChildElementOrNull() {
        for (Node node : getChildren()) {
            if (node instanceof Element) {
                return (Element)node;
            }
        }

        return null;
    }

    /**
     * Shortcut method for getting the first Element children with any name.
     * 
     * @return the first child Element never <code>null</code>.
     * @throws SAXParseException
     *             if there are no Element children.
     */
    public Element getFirstChildElement() throws SAXParseException {
        Element e = getFirstChildElementOrNull();

        if (e == null)
            throw new SAXParseException("Element expected", this);
        else
            return e;
    }
        
    /**
     * Returns an read-only Collection of all children which are Elements.
     * 
     * @return an read-only Collection of all children which are Elements.
     */
    public Collection<Element> getChildElements() {
        return new Collection<Element>() {
            private int theSize = -1;

            public boolean contains(Object o) {
                return (o instanceof Element) && getChildren().contains(o);
            }

            public boolean containsAll(Collection<?> c) {
                boolean ret = true;
                for (Object o : c) {
                    if (!contains(o)) {
                        ret = false;
                    }
                }
                return ret;
            }

            public Iterator<Element> iterator() {
                return new Iterator<Element>() {
                    private Iterator<Node> nodes = getChildren().iterator();

                    private Element nextElement = null;

                    public boolean hasNext() {
                        while (nextElement == null) {
                            if (!nodes.hasNext()) {
                                return false;
                            }

                            Node node = nodes.next();
                            if (node instanceof Element) {
                                nextElement = (Element)node;
                            }
                        }
                        return true;
                    }

                    public Element next() {
                        if (!hasNext()) {
                            throw new IndexOutOfBoundsException();
                        }
                        Element _nextElement = nextElement;
                        nextElement = null;
                        return _nextElement;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            private void calcSize() {
                theSize = 0;
                for (Node node : getChildren()) {
                    if (node instanceof Element) {
                        theSize++;
                    }
                }
            }

            public int size() {
                if (theSize < 0) {
                    calcSize();
                }

                return theSize;
            }

            public boolean isEmpty() {
                return size() == 0;
            }

            public Object[] toArray() {
                throw new UnsupportedOperationException();
            }

            public <T> T[] toArray(T[] a) {
                throw new UnsupportedOperationException();
            }

            public boolean add(Element o) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends Element> c) {
                throw new UnsupportedOperationException();
            }

            public boolean remove(Object o) {
                throw new UnsupportedOperationException();
            }

            public boolean removeAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            public boolean retainAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            public void clear() {
                throw new UnsupportedOperationException();
            }

        };
    }

}
