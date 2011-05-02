/*
 * Copyright (c) 2005, Mikael Ståldal
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

package nu.staldal.lsp.framework;

import java.util.*;


/**
 * Miscellaneous static utility methods.
 */
public final class Utils
{       
    
    /**
     * Private default constructor to prevent instantiation.
     */
    private Utils() 
    { 
        // never invoked
    }


    /**
     * Parse a string as an integer. Does not throw any exception, not
     * even if the string is <code>null</code>.
     *
     * @param s    the string to parse
     * @param err  the integer to return if the string does not contain
     *             any valid integer
     * 
     * @return err if the string cannot be parsed as an integer.
     */
    public static int parseInteger(String s, int err)
    {
        try {
            return Integer.parseInt(s);
        }
        catch (NullPointerException e)
        {
            return err;    
        }
        catch (NumberFormatException e)
        {
            return err;    
        }
    }

    
    /**
     * Split a string into a list of substrings using a specified 
     * character as delimiter. Will return empty strings in the list
     * if the there are several adjacent delimiter characters. 
     *
     * @param s      the string to split
     * @param delim  the delimiter character
     * 
     * @return the splitted string 
     */
    public static List<String> splitString(CharSequence s, char delim)
    {
        StringBuffer sb = new StringBuffer(s.length());
        List<String> list = new ArrayList<String>();
        for (int i = 0; i<s.length(); i++)
        {
            char ch = s.charAt(i);
            if (ch == delim)
            {
                list.add(sb.toString());
                sb.setLength(0);
            }
            else
            {
                sb.append(ch);
            }
        }
        list.add(sb.toString());
        return list;
    }
    

    /**
     * Escape a string for a Javascript literal. Uses backslash escapes 
     * like Java. Escapes \ " ' and newline (\n), CR (\r) is ignored.
     *
     * @param s  the string to escape
     * 
     * @return the escaped string 
     */
    public static String escapeJavascriptString(CharSequence s)
    {
        StringBuffer sb = new StringBuffer(s.length());
        for (int i = 0; i<s.length(); i++)
        {
            char ch = s.charAt(i);
            switch (ch)
            {
            case '\\':
                sb.append("\\\\");
                break;
                
            case '\"':
                sb.append("\\\"");
                break;
                
            case '\'':
                sb.append("\\\'");
                break;

            case '\r':
                break;
                
            case '\n':
                sb.append("\\n");
                break;

            default:
                sb.append(ch);
            }            
        }
        return sb.toString();
    }
}
