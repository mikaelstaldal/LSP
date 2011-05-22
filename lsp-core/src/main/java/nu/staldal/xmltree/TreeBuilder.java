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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * Build an XMLTree from a SAX2 event stream, or by parsing an XML document.
 * 
 * The TreeBuilder ensures that the tree will not have two adjacent Text nodes.
 */
public class TreeBuilder implements ContentHandler, ErrorHandler {
    public static final boolean DEBUG = false;

    private Element rootElement = null;

    private Stack<Element> elementStack;

    private ArrayList<String> nsPrefix = null;

    private ArrayList<String> nsURI = null;

    private StringBuffer textBuffer = null;

    private String textSystemId = null;

    private int textLine = -1;

    private int textColumn = -1;

    private Locator locator = null;

    private URL baseURI;

    /**
     * Create an SAX InputSource from a File object.
     * 
     * @param file
     *            the file
     * 
     * @return an InputSource
     * @throws FileNotFoundException
     *             if the file doesn't exist
     * @throws IOException
     *             if some I/O error occurs
     */
    public static InputSource fileToInputSource(File file)
            throws FileNotFoundException, IOException {
        if (!file.isFile())
            throw new FileNotFoundException(file.toURL().toExternalForm());

        return new InputSource(file.toURL().toExternalForm());
    }

    /**
     * Parse an XML document into an XTree. Uses JAXP to find a parser. Will not
     * support xml:base.
     * 
     * @param xmlInput
     *            the input to parse
     * @param validateDTD
     *            validate using DTD
     * 
     * @return an XTree representation of the XML data
     * 
     * @throws SAXParseException
     *             if the XML data is not valid
     * @throws SAXException
     *             if any other error occurs while parsing the XML data
     * @throws IOException
     *             if there was some I/O error while reading the input.
     */
    public static Element parseXML(InputSource xmlInput, boolean validateDTD)
            throws SAXParseException, SAXException, IOException {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            parserFactory.setValidating(validateDTD);
            parserFactory.setFeature("http://xml.org/sax/features/namespaces",
                    true);
            parserFactory.setFeature(
                    "http://xml.org/sax/features/namespace-prefixes", false);
            parserFactory.setFeature("http://xml.org/sax/features/validation",
                    validateDTD);

            XMLReader xmlReader = parserFactory.newSAXParser().getXMLReader();
            TreeBuilder tb = new TreeBuilder();

            xmlReader.setContentHandler(tb);
            xmlReader.setErrorHandler(tb);
            xmlReader.parse(xmlInput);
            return tb.getTree();
        }
        catch (javax.xml.parsers.ParserConfigurationException e) {
            throw new Error("XML parser configuration error: " + e.getMessage());
        }
    }

    /**
     * Constructs a TreeBuilder, ready to receive SAX events. Will not support
     * xml:base.
     */
    public TreeBuilder() {
        this(null);
    }

    /**
     * Constructs a TreeBuilder, ready to receive SAX events.
     * 
     * @param base
     *            base URL for the document, to support xml:base.
     */
    public TreeBuilder(URL base) {
        elementStack = new Stack<Element>();
        baseURI = base;
    }

    /**
     * Obtain the XTree built from SAX events.
     * 
     * @return the XTree
     * 
     * @throws IllegalStateException
     *             if the SAX events received so far doesn't constitues a
     *             well-formed XML document.
     */
    public Element getTree() throws IllegalStateException {
        if (!elementStack.isEmpty())
            throw new IllegalStateException("All elements are not yet ended");

        if (rootElement == null)
            throw new IllegalStateException("No root element");

        return rootElement;
    }

    void reset() {
        rootElement = null;
        elementStack = new Stack<Element>();
        nsPrefix = null;
        nsURI = null;
        textBuffer = null;
        textSystemId = null;
        textLine = -1;
        textColumn = -1;
    }

    private void addCharacters() {
        if ((textBuffer != null) && (textBuffer.length() > 0)) {
            Element parent = elementStack.peek();
            Node node = new Text(textBuffer.toString());
            node.setSystemId(textSystemId);
            node.setLine(textLine);
            node.setColumn(textColumn);
            parent.add(node);
        }

        textBuffer = null;
    }

    // ContentHandler implementation

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    public void startDocument() throws SAXException {
        // nothing to do
    }

    public void endDocument() throws SAXException {
        // nothing to do
    }

    public void startElement(String namespaceURI, String localName,
            String qname, Attributes atts) throws SAXException {
        addCharacters();

        if (DEBUG)
            System.out.println("startElement(" + namespaceURI + ',' + localName
                    + ',' + qname + ')');

        Element el = new Element(namespaceURI, localName, atts.getLength());
        if (locator != null) {
            el.setSystemId(locator.getSystemId());
            el.setLine(locator.getLineNumber());
            el.setColumn(locator.getColumnNumber());
        }
        if (rootElement == null) {
            rootElement = el;
            if (baseURI != null)
                rootElement.setBaseURI(baseURI);
        }
        else {
            Element parent = elementStack.peek();
            parent.add(el);
        }

        for (int i = 0; i < atts.getLength(); i++) {
            QName theQName = new QName(atts.getURI(i), atts.getLocalName(i));

            el.addAttribute(theQName.toString(), atts.getValue(i));

            if (atts.getURI(i).equals(XMLConstants.XML_NS_URI)
                    && atts.getLocalName(i).equals("base")) {
                try {
                    URL url = new URL(el.getBaseURI(), atts.getValue(i));
                    el.setBaseURI(url);
                }
                catch (java.net.MalformedURLException e) {
                    throw new SAXException(e);
                }

            }
        }

        if (nsPrefix != null) {
            el.setNamespaceMappings(nsPrefix, nsURI);
        }

        elementStack.push(el);

        nsPrefix = null;
        nsURI = null;
    }

    public void endElement(String namespaceURI, String localName, String qname)
            throws SAXException {
        addCharacters();

        if (DEBUG)
            System.out.println("endElement(" + namespaceURI + ',' + localName
                    + ',' + qname + ')');

        elementStack.pop();
    }

    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        if (DEBUG)
            System.out.println("startPrefixMapping("
                    + ((prefix.length() == 0) ? "<default>" : prefix) + ','
                    + uri + ')');

        if (nsPrefix == null) {
            nsPrefix = new ArrayList<String>();
            nsURI = new ArrayList<String>();
        }
        nsPrefix.add(prefix);
        nsURI.add(uri);
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        if (DEBUG)
            System.out.println("endPrefixMapping("
                    + ((prefix.length() == 0) ? "<default>" : prefix) + ')');

        // nothing to do
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        if (textBuffer == null) {
            textBuffer = new StringBuffer(length);
        }

        textBuffer.append(ch, start, length);
        if (locator != null) {
            textSystemId = locator.getSystemId();
            textLine = locator.getLineNumber();
            textColumn = locator.getColumnNumber();
        }
    }

    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
        // nothing to do
    }

    public void processingInstruction(String target, String data)
            throws SAXException {
        addCharacters();

        if (DEBUG)
            System.out.println("processingInstruction(" + target + ',' + data
                    + ')');

        Element parent = elementStack.peek();
        Node node = new ProcessingInstruction(target, data);
        if (locator != null) {
            node.setSystemId(locator.getSystemId());
            node.setLine(locator.getLineNumber());
            node.setColumn(locator.getColumnNumber());
        }
        parent.add(node);
    }

    public void skippedEntity(String name) throws SAXException {
        // nothing to do
    }

    // ErrorHandler implementation

    public void fatalError(SAXParseException e) throws SAXParseException {
        throw e;
    }

    public void error(SAXParseException e) throws SAXParseException {
        throw e;
    }

    public void warning(SAXParseException e) {
        // do nothing
    }

}
