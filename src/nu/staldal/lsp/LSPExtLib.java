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

package nu.staldal.lsp;

import java.io.*;
import java.util.*;

import org.xml.sax.*;

import nu.staldal.xtree.*;
import nu.staldal.syntax.ParseException;

import nu.staldal.lagoon.core.Target;

/**
 * LSP Extension Library.
 *
 * Will be invoked as follows:
 * <ol>
 * <li>beforeElement() is invoked, which returns a ContentHandler "in"
 * <li>the extension element is sent to the ContentHandler "in"
 * 	   (startDocument() and endDocument() will not be invoked).
 * <li>afterElement() is invoked, which may return a String.
 * </ol>
 *
 * LSP will not use the "in" ContentHandler after invoking the 
 * afterElement() method.
 *
 * The extlib can return data in one of two ways, write to the supplied 
 * ContentHandler "out" (it must not invoke startDocument() or endDocument()), 
 * or return a string from the afterElement() method. It may not do both.
 */
public interface LSPExtLib
{
	/**
	 * Invoked before the element is sent.
	 *
	 * @param out     where to write XML output.
	 * @param target  the current target
	 *
	 * @return  a ContentHandler to send input to.
	 */
	public ContentHandler beforeElement(ContentHandler out, Target target)
		throws SAXException;
	
	
	/**
	 * Invoked after the element is sent.
	 *
	 * @return  a string output.
	 */
	public String afterElement()
		throws SAXException;
	
}

