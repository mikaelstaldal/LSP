/*
 * Copyright (c) 2004, Mikael Ståldal
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


/**
 * Some utility methods. All methods in this class are static.
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
	 * Encode a path name or URL into a filename.
	 *
	 * The encoding function is not intended to be inversible.
     *  
     * @param path  the path to encode 
     * @return   the encoded path
	 */
    public static String encodePath(String path)
    {
        StringBuffer sb = new StringBuffer(path.length());

        for (int i = 0; i < path.length(); i++)
        {
            char c = path.charAt(i);
            switch (c)
            {
                case '-':
                    sb.append("--");
                    break;
                case '_':
                    sb.append("__");
                    break;
                case '$':
                    sb.append("$$");
                    break;
                case '~':
                    sb.append("~~");
                    break;
					
                case '/':
                    sb.append('-');
                    break;
                case '\\':
                    sb.append('-');
                    break;
                case '*':
                    sb.append('_');
                    break;
                case '?':
                    sb.append('$');
                    break;
                case ':':
                    sb.append('~');
                    break;
                default:
                    sb.append(c);
            }
        }

        return sb.toString();
    }


 	/**
	 * Encode a path name or URL into a Java identifier.
	 *
	 * The encoding function is not intended to be inversible.
     *  
 	 * @param path  the path to encode 
 	 * @return   the encoded path
	 */
    public static String encodePathAsIdentifier(String path)
    {
        StringBuffer sb = new StringBuffer(path.length());
		
        char c = path.charAt(0);
		if (Character.isJavaIdentifierStart(c))
			sb.append(c);
		else
			sb.append("_"+((int)c)+"_");

		for (int i = 1; i < path.length(); i++)
        {
            c = path.charAt(i);
			if (Character.isJavaIdentifierPart(c))
				sb.append(c);
			else
				sb.append("_"+((int)c)+"_");
		}
		
		return sb.toString();
	}



    /**
     * Check whether an URL is absolute.
     * 
     * @param url the URL to check 
     * @return <code>true</code> if the URL contains at least one colon, and
     * the first colon is before the first slash (if any).
     */
    public static boolean absoluteURL(String url)
    {
        int colon = url.indexOf(':');
        if (colon < 0) return false;

        int slash = url.indexOf('/');
        if (slash < 0) return true;

        return colon < slash;
    }

 
    /**
     * Check whether an URL is pseudo-absolute.
     * 
     * @param url the URL to check 
     * 
     * @return  <code>true</code> if the URL start with a slash.  
     */
    public static boolean pseudoAbsoluteURL(String url)
    {
        return (url.length() > 0) && (url.charAt(0) == '/');
    }	


	/**
	 * Generate a {@link java.lang.String} with a specified number
	 * of a given character.
     * 
	 * @param n  character count 
	 * @param c  the character
	 * @return   a string with c n times
	 */
	public static String nChars(int n, char c)
	{
		StringBuffer sb = new StringBuffer(n);
		for (int i = 0; i<n; i++)
			sb.append(c);
		return sb.toString();
	}
	
}
