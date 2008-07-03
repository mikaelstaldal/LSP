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

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Character content in an XML document. This class is immutable.
 */
public class Text extends Node {
    private static final long serialVersionUID = 4409847247255537558L;

    final String value;

    transient char[] charArrayCache;

    /**
     * Constructs a text node from a char[] buffer.
     * 
     * @param data
     *            a char[] buffer
     * @param start
     *            the offset to read from in the buffer
     * @param length
     *            the number of characters to read from the buffer
     * @param forceCopy
     *            force copying of the data, if false a reference to the buffer
     *            may be kept.
     */
    public Text(char[] data, int start, int length, boolean forceCopy) {
        value = new String(data, start, length);

        if (start == 0 && length == data.length && !forceCopy)
            charArrayCache = data;
    }

    /**
     * Constructs a text node from a String.
     * 
     * @param value
     *            the string
     */
    public Text(String value) {
        this.value = value;
    }

    /**
     * Get the charater content as a string
     * 
     * @return the character content
     */
    public String getValue() {
        return value;
    }

    private void obtainCharArray() {
        if (charArrayCache == null)
            charArrayCache = value.toCharArray();
    }

    /**
     * Get the charater content as a char[].
     * 
     * @return the char[]
     */
    public char[] asCharArray() {
        obtainCharArray();
        return charArrayCache;
    }

    @Override
    public void toSAX(ContentHandler sax) throws SAXException {
        obtainCharArray();
        sax.characters(charArrayCache, 0, charArrayCache.length);
    }

    @Override
    public boolean isWhitespaceNode() {
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) > ' ')
                return false;
        }
        return true;
    }
}
