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

import java.io.Serializable;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


public interface LSPPage extends Serializable
{
    public Enumeration getCompileDependentFiles();

    public Enumeration getExecuteDependentFiles();

    public boolean isCompileDynamic();

    public boolean isExecuteDynamic();

    public long getTimeCompiled();

    /**
     * Execute this LSP page and sends the output as SAX events to the
     * supplied ContentHandler. Does <em>not</em> output startDocument()
     * or endDocument() events.
	 *
	 * @param ch		  SAX ContentHandler to send output to
	 * @param resolver    Used to resolve included files
	 * @param params      Parameters to the LSP page
	 * @param extContext  external context which will be passed to ExtLibs
	 * @param targetURL   the current target URL, pseudo-absolute URL string,
	 *					  passed to ExtLibs
     */
    public void execute(ContentHandler ch, URLResolver resolver,
        	Map params, Object extContext, String targetURL)
        throws SAXException;
}

