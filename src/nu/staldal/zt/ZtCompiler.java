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

package nu.staldal.zt;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import nu.staldal.lsp.LSPPage;
import nu.staldal.xmltree.Element;
import nu.staldal.xmltree.Node;
import nu.staldal.xmltree.ProcessingInstruction;
import nu.staldal.xmltree.Text;
import nu.staldal.xmltree.TreeBuilder;

/**
 * Compiles an Zt page.
 * 
 * <p>
 * An instance of this class may be reused, but is not thread safe.
 */
public class ZtCompiler {
    private static final boolean DEBUG = false;

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private TreeBuilder tb;

    private String pageName;

    private Element currentSourceElement;

    private ZtElement currentElement;

    private Properties outputProperties;

    private boolean html;

    private boolean acceptUnbound;

    /**
     * Create a new LSP compiler. The instance may be reused, but may
     * <em>not</em> be used from several threads concurrently. Create several
     * instances if multiple threads needs to compile concurrently.
     */
    public ZtCompiler() {
        tb = null;
        pageName = null;
        html = false;
        acceptUnbound = false;
    }

    /**
     * Set to <code>true</code> to use <code>html</code> as default output
     * type.
     * 
     * @param html
     */
    public void setHtml(boolean html) {
        this.html = html;
    }

    /**
     * Set to <code>true</code> to make the compiled page accept unbound
     * values without runtime error.
     * 
     * @param acceptUnbound
     */
    public void setAcceptUnbound(boolean acceptUnbound) {
        this.acceptUnbound = acceptUnbound;
    }

    /**
     * Start compilation of an LSP page.
     * 
     * @param pageName  page name
     * 
     * @return SAX2 ContentHandler to feed the LSP source into
     * 
     * @throws SAXException
     */
    public ContentHandler startCompile(String pageName) throws SAXException {
        if (!checkPageName(pageName))
            throw new SAXException("Illegal page name: " + pageName);

        this.pageName = pageName;
        tb = new TreeBuilder();

        return tb;
    }

    static boolean checkPageName(CharSequence pageName) {
        if (pageName.length() == 0)
            return false;

        if (!Character.isJavaIdentifierStart(pageName.charAt(0)))
            return false;

        for (int i = 1; i < pageName.length(); i++) {
            char ch = pageName.charAt(i);
            if (!Character.isJavaIdentifierPart(ch))
                return false;
        }
        return true;
    }

    /**
     * Finish the compilation.
     * 
     * @throws SAXException
     *             if any compilation error occurs
     */
    public LSPPage finishCompile() throws SAXException {
        if (tb == null)
            throw new IllegalStateException(
                    "startCompile() must be invoked before finishCompile()");

        Element tree = tb.getTree();

        long startTime = System.currentTimeMillis();
        if (DEBUG)
            System.out.println("ZeroTemplate compile...");

        currentElement = null;
        currentSourceElement = null;
        outputProperties = null;

        ZtElement compiledTree = compileNode(tree);

        if (outputProperties == null)
            outputProperties = new Properties();

        if (!outputProperties.containsKey("method")) {
            String method;

            if (tree.getName().equalsIgnoreCase("html")) {
                method = "html";
            }
            else if (tree.getLocalName().equals("html")
                    && tree.getNamespaceURI().equals(XHTML_NS)) {
                method = html ? "html" : "xhtml";
            }
            else {
                method = "xml";
            }

            outputProperties.setProperty("method", method);
        }

        LSPPage result = new ZtInterpreter(compiledTree, pageName, startTime,
                outputProperties);

        outputProperties = null;
        tb = null;
        pageName = null;

        long timeElapsed = System.currentTimeMillis() - startTime;
        if (DEBUG)
            System.out.println("in " + timeElapsed + " ms");

        return result;
    }

    static SAXException fixSourceException(Node node, String msg) {
        return new SAXParseException(msg, null, node.getSystemId(), node
                .getLineNumber(), node.getColumnNumber());
    }

/*    
    private static SAXException fixParseException(Node node, String expression,
            ParseException e) {
        return new SAXParseException("Illegal LSP expression:\n" + expression
                + "\n" + Utils.nChars(e.getColumn() - 1, ' ') + "^ "
                + e.getMessage(), null, node.getSystemId(), node
                .getLineNumber(), node.getColumnNumber());
    }
*/
    
    private Node compileNode(Node node) throws SAXException {
        if (node instanceof Element)
            return compileNode((Element)node);
        else if (node instanceof Text)
            return compileNode((Text)node);
        else if (node instanceof ProcessingInstruction)
            return compileNode((ProcessingInstruction)node);
        else
            throw new SAXException("Unrecognized XMLTree Node: "
                    + node.getClass().getName());
    }

    private ZtElement compileNode(Element el) throws SAXException {
        String classes = el.getAttributeOrNull("class");
        if (classes == null) {
            classes = "";
        }
        List<String> normalClasses = new ArrayList<String>();
        
        String zt = null;
        String ztLiteral = null;
        List<ZtAttr> ztAttr = new ArrayList<ZtAttr>();
        String ztExpand = null;
        String ztList = null;
        String ztIf = null;
        String ztIfNot = null;
        boolean ztRemove = false;
        
        for (StringTokenizer st = new StringTokenizer(classes); st.hasMoreTokens(); ) {
            String cls = st.nextToken();

            // Dispatch Zt command
            if (cls.startsWith("Zt-")) {
                if (zt != null) {
                    throw fixSourceException(el, "Zt may not be repeted"); 
                }
                zt = cls.substring(3);
            } else if (cls.startsWith("ZtLiteral-")) {
                if (ztLiteral != null) {
                    throw fixSourceException(el, "ZtLiteral may not be repeted"); 
                }
                ztLiteral = cls.substring(10);
            } else if (cls.startsWith("ZtAttr-")) {
                ztAttr.add(process_attr(el, cls.substring(7)));
            } else if (cls.startsWith("ZtIf-")) {
                if (ztIf != null) {
                    throw fixSourceException(el, "ZtIf may not be repeted"); 
                }
                ztIf = cls.substring(5);
            } else if (cls.startsWith("ZtIfNot-")) {
                if (ztIfNot != null) {
                    throw fixSourceException(el, "ZtIfNot may not be repeted"); 
                }
                ztIfNot = cls.substring(8);
            } else if (cls.startsWith("ZtExpand-")) {
                if (ztExpand != null) {
                    throw fixSourceException(el, "ZtExpand may not be repeted"); 
                }
                ztExpand = cls.substring(9);
            } else if (cls.startsWith("ZtList-")) {
                if (ztList != null) {
                    throw fixSourceException(el, "ZtList may not be repeted"); 
                }
                ztList = cls.substring(7);                
            } else if (cls.startsWith("ZtRemove-")) {
                if (ztRemove) {
                    throw fixSourceException(el, "ZtRemove may not be repeted"); 
                }
                ztRemove = true;
            } else {
                normalClasses.add(cls);
            }
        }
       
        if (zt != null && ztLiteral != null) {
            throw fixSourceException(el, "Zt may not be combined with ZtLiteral");             
        }
        String ztString = null;
        boolean stringIsLiteral = false;
        if (zt != null) {
            ztString = zt;
            stringIsLiteral = false;
        } else if (ztLiteral != null) {
            ztString = ztLiteral;
            stringIsLiteral = true;
        }
        
        ZtElement newEl;
        
        if (ztRemove) {
            if (ztIf != null || ztIfNot != null || !ztAttr.isEmpty()
                    || zt != null || ztLiteral != null || ztExpand != null || ztList != null) {
                throw fixSourceException(el, "ZtRemove may not be combined with other Zt commands"); 
            } else {
                return new ZtRemoveElement(el, ztAttr, ztString,
                        stringIsLiteral, ztExpand, ztList);
            }
        } else if (ztIf != null) {
            if (ztIfNot != null) {
                throw fixSourceException(el, "ZtIf may not be combined with ZtItNot"); 
            } else {
                newEl = new ZtIfElement(el, ztAttr, ztString,
                                        stringIsLiteral, ztExpand, ztList, ztIf);
            }
        } else if (ztIfNot != null) {
            newEl = new ZtIfNotElement(el, ztAttr, ztString,
                                       stringIsLiteral, ztExpand, ztList, ztIfNot);
        } else {
            newEl = new ZtElement(el, ztAttr, ztString,
                                  stringIsLiteral, ztExpand, ztList);
        }
        
        if (normalClasses.isEmpty()) {
            newEl.getAttributes().remove("class");
        } else {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String cls : normalClasses) {
                if (!first) {
                    sb.append(' ');
                }
                sb.append(cls);
                first = false;
            }
            newEl.getAttributes().put("class", sb.toString());
        }            
        
        currentElement = newEl;
        currentSourceElement = el;
        compileChildren(el, newEl);

        return newEl;
    }

    private Node compileNode(Text text) {
        return text;
    }

    private Node compileNode(ProcessingInstruction pi) {
        return pi;
    }

    private void compileChildren(Element el, ZtElement container)
            throws SAXException {
        for (Node child : el) {
            Node compiledNode = compileNode(child);
            container.add(compiledNode);
        }
    }

    private ZtAttr process_attr(Element el, String args) throws SAXException {
        int colon = args.indexOf('-');
        if (colon < 1) {
            throw fixSourceException(el, "Invalid syntax of ZtAttr: ZtAttr-" + args);
        }
        String attrName = args.substring(0, colon);
        String str = args.substring(colon+1);
        return new ZtAttr(attrName, str);
    }    
}
