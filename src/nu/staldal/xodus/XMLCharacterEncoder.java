/*
 * Copyright (c) 2005-2006, Mikael Ståldal
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

package nu.staldal.xodus;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.nio.charset.*;


/**
 * Encodes output to an XML stream.
 *<p>
 * Inserts numeric character entities for characters which cannot be 
 * encoded in the specified encoding. 
 *<p>
 * <em>Note:</em> Does <em>not</em> insert the gt, lt, qout, amp and apos 
 * entites.
 *<p>
 * The <code>write</code> and <code>append</code> methods will throw 
 * {@link java.io.CharConversionException} if character encoding or escaping 
 * fails.
 *<p>
 * This class is <em>not</em> thread safe.
 *<p>
 * This class implements all <code>write</code> methods of {@link java.io.Writer},
 * but does not extend {@link java.io.Writer} due to technical reasons.
 */
public class XMLCharacterEncoder implements Appendable, Closeable
{
    private static final int BUFFER_SIZE = 1024;
    private static final int BUFFER_TRESHOLD = 64;
    
    private final Charset charset;
    private final CharsetEncoder encoder;
    private final CharBuffer encBuffer;
    
    private final Appendable appendable;
    private final Writer writer;
    private final OutputStream os;
    
    private boolean hasFinished = false;
        
    /**
     * Constructs an XMLCharacterEncoder which writes to the given
     * {@link java.io.OutputStream}.
     *
     * @param os  the {@link java.io.OutputStream} to write to.
     * @param encoding  the encoding to use.
     *
     * @throws java.io.UnsupportedEncodingException If the given encoding 
     *         name is illegal or not available. 
     */
    public XMLCharacterEncoder(OutputStream os, String encoding)
        throws UnsupportedEncodingException
    {        
        try {
            charset = Charset.forName(encoding);
            encoder = charset.newEncoder();
            encoder.onMalformedInput(CodingErrorAction.REPORT);             
            encoder.onUnmappableCharacter(CodingErrorAction.REPORT);             
            encoder.reset();
            encBuffer = CharBuffer.allocate(BUFFER_SIZE);
            
            writer = null;
            appendable = null;
        }
        catch (IllegalCharsetNameException e)
        {
            UnsupportedEncodingException ee = 
                new UnsupportedEncodingException("encoding name is illegal: "+ encoding);
            ee.initCause(e);
            throw ee;
        }
        catch (UnsupportedCharsetException e)
        {
            UnsupportedEncodingException ee = 
                new UnsupportedEncodingException("encoding is not available: "+ encoding);
            ee.initCause(e);
            throw ee;
        }        
        catch (UnsupportedOperationException e)
        {
            UnsupportedEncodingException ee = 
                new UnsupportedEncodingException("encoding is not available: "+ encoding);
            ee.initCause(e);
            throw ee;
        }        
     
        this.os = os;
    }
    

    /**
     * Constructs an XMLCharacterEncoder which writes to the given
     * {@link java.io.Writer}. Does not encode or escape.
     *
     * @param writer  the {@link java.io.Writer} to write to.
     */
    public XMLCharacterEncoder(Writer writer)
    {
        charset = null;
        encoder = null;
        encBuffer = null;

        this.writer = writer;
        appendable = null;
        os = null;
    }

    
    /**
     * Constructs an XMLCharacterEncoder which writes to the given
     * {@link java.lang.Appendable}. Does not encode or escape.
     *
     * @param a  the {@link java.lang.Appendable} to write to.
     * @param isAppendable  dummy parameter to avoid overloading problems
     */
    public XMLCharacterEncoder(Appendable a, boolean isAppendable)
    {
        charset = null;
        encoder = null;
        encBuffer = null;

        this.appendable = a;
        writer = null;
        os = null;
    }

    
    public Appendable append(char c)
        throws IOException 
    {
        if (appendable != null)
        {
            appendable.append(c);    
        }
        else if (writer != null)
        {
            writer.append(c);    
        }
        else
        {
            if (1 <= encBuffer.remaining())
            {
                encBuffer.put(c);
            }
            else
            {
                _flush();
                encBuffer.put(c);                
            }            
        }
        return this;
    }


    public Appendable append(CharSequence cs) 
        throws IOException 
    {
        if (appendable != null)
        {
            appendable.append(cs);    
        }
        else if (writer != null)
        {
            writer.append(cs);    
        }
        else
        {
            if (cs.length() <= encBuffer.remaining())
            {
                encBuffer.append(cs);
            }
            else
            {
                _flush();
                if (cs.length() < BUFFER_TRESHOLD)
                {
                    encBuffer.append(cs);                    
                }
                else
                {
                    encodeWrite(CharBuffer.wrap(cs));
                }
            }
        }        
        return this;
    }


    public Appendable append(CharSequence cs, int start, int end) 
        throws IOException 
    {
        if (appendable != null)
        {
            appendable.append(cs, start, end);    
        }
        else if (writer != null)
        {
            writer.append(cs, start, end);    
        }
        else
        {
            if ((end-start) <= encBuffer.remaining())
            {
                encBuffer.append(cs, start, end);
            }
            else
            {
                _flush();
                if (cs.length() < BUFFER_TRESHOLD)
                {
                    encBuffer.append(cs, start, end);                    
                }
                else
                {
                    encodeWrite(CharBuffer.wrap(cs, start, end));
                }
            }
        }        
        return this;
    }


    public void write(int c)
        throws IOException 
    {
        if (appendable != null)
        {
            appendable.append((char)c);    
        }
        else if (writer != null)
        {
            writer.write(c);    
        }
        else
        {
            if (1 <= encBuffer.remaining())
            {
                encBuffer.put((char)c);
            }
            else
            {
                _flush();
                encBuffer.put((char)c);                
            }            
        }
    }


    public void write(char cbuf[]) 
        throws IOException 
    {
        if (appendable != null)
        {
            appendable.append(CharSequenceWrapper.valueOf(cbuf));     
        }
        else if (writer != null)
        {
            writer.write(cbuf);    
        }
        else
        {
            if (cbuf.length <= encBuffer.remaining())
            {
                encBuffer.put(cbuf);
            }
            else
            {
                _flush();
                if (cbuf.length < BUFFER_TRESHOLD)
                {
                    encBuffer.put(cbuf);
                }
                else
                {
                    encodeWrite(CharBuffer.wrap(cbuf));
                }
            }
        }
    }


    public void write(char cbuf[], int off, int len)
        throws IOException 
    {
        if (appendable != null)
        {
            appendable.append(CharSequenceWrapper.valueOf(cbuf, off, off+len));     
        }
        else if (writer != null)
        {
            writer.write(cbuf, off, len);    
        }
        else
        {
            if (len <= encBuffer.remaining())
            {
                encBuffer.put(cbuf, off, len);
            }
            else
            {
                _flush();
                if (cbuf.length < BUFFER_TRESHOLD)
                {
                    encBuffer.put(cbuf, off, len);
                }
                else
                {
                    encodeWrite(CharBuffer.wrap(cbuf, off, len));
                }
            }
        }        
    }
    

    public void write(String str) 
        throws IOException 
    {
        if (appendable != null)
        {
            appendable.append(str);     
        }
        else if (writer != null)
        {
            writer.write(str);    
        }
        else
        {
            if (str.length() <= encBuffer.remaining())
            {
                encBuffer.put(str);
            }
            else
            {
                _flush();
                if (str.length() < BUFFER_TRESHOLD)
                {
                    encBuffer.put(str);
                }
                else
                {
                    encodeWrite(CharBuffer.wrap(str));
                }
            }
        }        
    }


    public void write(String str, int off, int len) 
        throws IOException 
    {
        if (appendable != null)
        {
            appendable.append(str, off, off+len);     
        }
        else if (writer != null)
        {
            writer.write(str, off, len);    
        }
        else
        {            
            if (len <= encBuffer.remaining())
            {
                encBuffer.put(str, off, off+len);
            }
            else
            {
                _flush();
                if (str.length() < BUFFER_TRESHOLD)
                {
                    encBuffer.put(str, off, off+len);
                }
                else
                {
                    encodeWrite(CharBuffer.wrap(str, off, off+len));
                }
            }
        }        
    }
    
    private void _flush() 
        throws IOException
    {
        if (encBuffer != null)
        {
            encBuffer.flip();
            encodeWrite(encBuffer);
            encBuffer.clear();        
        }
    }    
    
    private void encodeWrite(CharBuffer in)
        throws IOException
    {
        int size = (int)(in.remaining()*encoder.averageBytesPerChar()*1.1);
        if (size % 2 > 0) size++; // make size even
        
        byte[] buf = new byte[size];
        ByteBuffer out = ByteBuffer.wrap(buf);
        
        CoderResult cr;
        while (true)
        {                
            cr = encoder.encode(in, out, false);
            if (cr.isUnderflow())
            {
                if (in.hasRemaining())
                    throw new CharConversionException(
                        "Malformed Unicode character: remaining input at underflow");
                else
                    break;
            }
            else if (cr.isOverflow())
            {
                os.write(buf, 0, out.position());
                out.clear();
            }
            else if (cr.isUnmappable())
            {
                os.write(buf, 0, out.position());
                out.clear();
                for (int i = 0; i<cr.length(); i++)
                {
                    String entity = "&#x" + Integer.toHexString(in.get()) + ";";
                    encodeWrite(CharBuffer.wrap(entity));
                }
            }
            else // if (cr.isMalformed())
            {
                throw new CharConversionException(
                    "Malformed Unicode character: \\u" + Integer.toHexString(in.get()));
            }
        }
        os.write(buf, 0, out.position());
    }
    
    public void finish() 
        throws IOException
    {
        if (hasFinished) return;
        
        if (encBuffer != null)
        {
            encBuffer.flip();
            encodeWrite(encBuffer);
            encBuffer.clear();
            encBuffer.flip();
            
            byte[] buf = new byte[16];
            ByteBuffer out = ByteBuffer.wrap(buf);
            
            while (true)
            {                
                CoderResult cr = encoder.encode(encBuffer, out, true);
                if (cr.isUnderflow())
                {
                    break;
                }
                else if (cr.isOverflow())
                {
                    os.write(buf, 0, out.position());
                    out.clear();
                }
            }
            if (out.position() > 0) os.write(buf, 0, out.position());
            out.clear();
            
            while (true)
            {                
                CoderResult cr = encoder.flush(out);
                if (cr.isUnderflow())
                {
                    break;
                }
                else if (cr.isOverflow())
                {
                    os.write(buf, 0, out.position());
                    out.clear();
                }
            }
            if (out.position() > 0) os.write(buf, 0, out.position());
        }
        
        hasFinished = true;
    }

    public void close() 
        throws IOException
    {
        finish();
            
        if (writer != null)
        {
            writer.close();    
        }
        else if (os != null)
        {
            os.close();
        }
    }
    
}
