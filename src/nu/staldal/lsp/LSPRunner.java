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

import java.io.*;
import java.util.*;

import org.xml.sax.*;


/**
 * Execute LSP pages in a stand-alone environment.
 * Useful for testing and debugging.
 */
public class LSPRunner
{
    
    public static void main(String[] args)
        throws InstantiationException, IllegalAccessException, VerifyError, 
            SAXException, IOException 
    {
        if (args.length < 2)
        {
            System.err.println("LSP runtime version " + LSPPage.LSP_VERSION_NAME);
            System.err.println("Syntax: LSPRunner <pageName> <outFile>");
            System.err.println("Use \"-\" as <outFile> for standard output");
            return;
        }
        
        LSPHelper helper = new LSPHelper(ClassLoader.getSystemClassLoader());
        
        LSPPage page = helper.getPage(args[0]);
        if (page == null)
        {
            System.err.println("LSP page " + args[0] + " not found");
            return;
        }
        
        System.err.println("Content-Type: " + helper.getContentType(page));
        
        OutputStream out = args[1].equals("-") 
            ? (OutputStream)System.out 
            : (OutputStream)(new FileOutputStream(args[1]));
        
        helper.executePage(page, Collections.EMPTY_MAP, null, out);
                           
        out.close();
    }

}

