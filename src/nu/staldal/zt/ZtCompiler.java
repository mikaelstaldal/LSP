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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import nu.staldal.lsp.LSPPage;
import nu.staldal.lsp.URLResolver;
import nu.staldal.xmltree.Element;
import nu.staldal.xmltree.Node;
import nu.staldal.xmltree.ProcessingInstruction;
import nu.staldal.xmltree.Text;
import nu.staldal.xmltree.TreeBuilder;

/**
 * Compiles an ZeroTemplate page.
 * 
 * <p>
 * An instance of this class may be reused, but is not thread safe.
 */
public class ZtCompiler {
    private static final boolean DEBUG = false;

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
    
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private boolean html;
    private boolean acceptUnbound;
    
    private String pageName;
    private URLResolver resolver;

    private TreeBuilder tb;
    private HashMap<String,String> importedFiles;    
    private Properties outputProperties;    

    /**
     * Create a new ZeroTemplate compiler. The instance may be reused, but may
     * <em>not</em> be used from several threads concurrently. Create several
     * instances if multiple threads needs to compile concurrently.
     */
    public ZtCompiler() {
        html = false;
        acceptUnbound = false;
        
        reset();
    }
    
    private void reset() {        
        pageName = null;        
        resolver = null;
        
        tb = null;
        importedFiles = null;
        outputProperties = null;
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
     * Start compilation of an Zt page.
     * 
     * @param pageName  page name
     * @param resolver  {@link nu.staldal.lsp.URLResolver} to use for resolving 
     *                   <code>include</code> and enclose
     * 
     * @return SAX2 ContentHandler to feed the ZeroTemplate source into
     * 
     * @throws SAXException
     */
    public ContentHandler startCompile(String pageName, URLResolver resolver) throws SAXException {
        if (!checkPageName(pageName))
            throw new SAXException("Illegal page name: " + pageName);

        this.pageName = pageName;
        this.resolver = resolver;
        
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
     * @throws IOException if any I/O error occurs when reading 
     *             included files, or when writing compiled code
     */
    public LSPPage finishCompile() throws SAXException, IOException {
        if (tb == null)
            throw new IllegalStateException(
                    "startCompile() must be invoked before finishCompile()");

        Element tree = tb.getTree();

        long startTime = System.currentTimeMillis();
        if (DEBUG)
            System.out.println("ZeroTemplate compile...");

        tree = processEnclose(tree);
        
        importedFiles = new HashMap<String,String>();        
        processImports(tree);

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

        LSPPage result = new ZtInterpreter(compiledTree, pageName, importedFiles.keySet().toArray(EMPTY_STRING_ARRAY), 
                startTime, outputProperties);

        reset();
        
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
    
    private static boolean containsSystemId(Node node, String systemId) {
        if (systemId.equals(node.getSystemId())) 
            return true;
        else if (node.getParent() != null)
            return containsSystemId(node.getParent(), systemId);
        else
            return false;
    }
    
    private Element processEnclose(Element content) throws SAXException, IOException {
        String classes = content.getAttributeOrNull("class");
        String encloseUrl = null;
        if (classes != null) {
            for (StringTokenizer st = new StringTokenizer(classes); st.hasMoreTokens(); ) {
                String cls = st.nextToken();

                if (cls.startsWith("ZtEnclose-")) {
                    encloseUrl = cls.substring(10);
                    break;
                }
            }
        }
        
        if (encloseUrl != null) {            
            TreeBuilder encloseTb = new TreeBuilder();
            resolver.resolve(encloseUrl, encloseTb);
            Element enclose = encloseTb.getTree();
            encloseTb = null;
            
            processEncloseInclude(enclose, content);
            return enclose;
        } else {
            return content;
        }        
    }    

    private void processEncloseInclude(Element el, Element content) throws SAXException, IOException {   
        for (int i = 0; i<el.size(); i++) {
            Node _child = el.get(i);
            if (!(_child instanceof Element)) {
                continue;
            }
            Element child = (Element)_child;
            
            String classes = child.getAttributeOrNull("class");
            boolean found = false;
            if (classes != null) {
                for (StringTokenizer st = new StringTokenizer(classes); st.hasMoreTokens(); ) {
                    String cls = st.nextToken();
    
                    if (cls.startsWith("ZtContent-")) {
                        found = true;
                        break;
                    }
                }
            }
                
            if (found) {
                el.set(i, content);
            } else {
                processEncloseInclude(child, content);
            }
        }
    }    
    
    private void processImports(Element el) throws SAXException, IOException {
        for (int i = 0; i<el.size(); i++) {
            Node _child = el.get(i);
            if (!(_child instanceof Element)) {
                continue;
            }
            Element child = (Element)_child;
            
            String classes = child.getAttributeOrNull("class");
            String url = null;
            if (classes != null) {
                for (StringTokenizer st = new StringTokenizer(classes); st.hasMoreTokens(); ) {
                    String cls = st.nextToken();
    
                    if (cls.startsWith("ZtInclude-")) {
                        url = cls.substring(10);
                        break;
                    }
                }
            }
                
            if (url != null) {
                boolean duplicate = false;
                if (importedFiles.put(url, url) != null)
                    duplicate = true;
    
                TreeBuilder tb = new TreeBuilder();
                resolver.resolve(url, tb);
                Element importedDoc = tb.getTree();
                importedFiles.put(url, importedDoc.getSystemId());
                
                if (duplicate) {
                    if (containsSystemId(child, importedDoc.getSystemId()))
                        throw fixSourceException(child, "circular import");
                }
    
                el.set(i, importedDoc);
                processImports(importedDoc);
            } else {
                processImports(child);
            }
        }
    }
    
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
        
        String ztText = null;
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
            if (cls.startsWith("ZtText-")) {
                if (ztText != null) {
                    throw fixSourceException(el, "ZtText may not be repeted"); 
                }
                ztText = cls.substring(7);
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
            } else if (cls.startsWith("ZtInclude-")) {
                throw fixSourceException(el, "Internal error: non-processed ZtInclude found!"); 
            } else if (cls.startsWith("ZtEnclose-")) {
                // just ignore 
            } else {
                normalClasses.add(cls);
            }
        }
        
        boolean listIsOddEven = (ztList != null) && normalClasses.contains("odd");
       
        if (ztText != null && ztLiteral != null) {
            throw fixSourceException(el, "ZtText may not be combined with ZtLiteral");             
        }
        String ztString = null;
        boolean stringIsLiteral = false;
        if (ztText != null) {
            ztString = ztText;
            stringIsLiteral = false;
        } else if (ztLiteral != null) {
            ztString = ztLiteral;
            stringIsLiteral = true;
        }
        
        ZtElement newEl;
        
        if (ztRemove) {
            if (ztIf != null || ztIfNot != null || !ztAttr.isEmpty()
                    || ztText != null || ztLiteral != null || ztExpand != null || ztList != null) {
                throw fixSourceException(el, "ZtRemove may not be combined with other Zt commands"); 
            } else {
                return new ZtRemoveElement(el);
            }
        } else if (ztIf != null) {
            if (ztIfNot != null) {
                throw fixSourceException(el, "ZtIf may not be combined with ZtItNot"); 
            } else {
                newEl = new ZtIfElement(el, ztAttr, ztString,
                                        stringIsLiteral, ztExpand, ztList, listIsOddEven, ztIf);
            }
        } else if (ztIfNot != null) {
            newEl = new ZtIfNotElement(el, ztAttr, ztString,
                                       stringIsLiteral, ztExpand, ztList, listIsOddEven, ztIfNot);
        } else {
            newEl = new ZtElement(el, ztAttr, ztString, stringIsLiteral, ztExpand, ztList, listIsOddEven);
        }               
        
        if (listIsOddEven) {
            normalClasses.remove("odd");
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
