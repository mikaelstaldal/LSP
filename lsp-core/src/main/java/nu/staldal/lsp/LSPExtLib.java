/*
 * Copyright (c) 2001-2005, Mikael Ståldal
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

package nu.staldal.lsp;

import org.xml.sax.*;


/**
 * LSP Extension Library.
 * <p>
 * After instance creation, the init method will be invoked with the 
 * namespace URI. This allows the same class to be used for several
 * namespaces.
 * <p>An LSPExtLib may be reused, but will not be executed concurrently 
 * by several threads. 
 * <p>For each LSP page to process, the startPage method will be invoked 
 * first. Then for each extension element on the LSP page:
 * <ol>
 * <li>beforeElement() is invoked, which returns a ContentHandler "in"
 * <li>the extension element is sent to the ContentHandler "in"
 * 	   (startDocument() and endDocument() will not be invoked).
 * <li>afterElement() is invoked, which may return a String.
 * </ol>
 * 
 * LSP will not use the "in" ContentHandler after invoking the 
 * afterElement() method.
 * <p>
 * The extlib can return data in one of two ways, write to the supplied 
 * ContentHandler "out" (it must not invoke startDocument() or endDocument()), 
 * or return a string from the afterElement() method. It may not do both.
 * <p>
 * For each call to an extension function, a method with the function name
 * prefixed with '_' is invoked. All arguments to this method must be of type
 * Object, and the return type must be Object, it may throw SAXException.
 * Functions may be overloaded based on number of arguments. 
 * <p>
 * After the LSP page is finished, the endPage method is invoked.
 * <p>
 * An easier, but not as flexible, way to implement an extension library is to extend the 
 * {@link nu.staldal.lsp.SimpleExtLib} class.
 */
public interface LSPExtLib
{
	
	/**
	 * Initialize this Extension Library.
	 *
	 * @param namespaceURI  the namespace URI to serve
     * 
	 * @throws SAXException may throw SAXException 
	 */
	public void init(String namespaceURI)
		throws SAXException;


	/**
	 * Indicate the start of an LSP page.
	 *
	 * @param extContext  external context passed to LSP execution engine
	 * @param pageName    name of the LSP page
     * 
     * @throws SAXException may throw SAXException 
	 */
	public void startPage(Object extContext, 
						  String pageName)
		throws SAXException;
	

	/**
	 * Invoked before the element is sent.
	 *
	 * @param out     where to write XML output.
     *
	 * @return  a ContentHandler to send input to.
     * 
     * @throws SAXException may throw SAXException 
	 */
	public ContentHandler beforeElement(ContentHandler out)			
		throws SAXException;
	
	
	/**
	 * Invoked after the element is sent.
	 *
	 * @return  a string output.
     * 
     * @throws SAXException may throw SAXException 
	 */
	public String afterElement()
		throws SAXException;
		

	/**
	 * Indicate the end of an LSP page.
     * 
     * @throws SAXException may throw SAXException 
	 */
	public void endPage()
		throws SAXException;

}
