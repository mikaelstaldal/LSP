/*
 * Copyright (c) 2003, Mikael Ståldal
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


/**
 * Command Line interface to the LSP compiler.
 */
public class LSPCompilerCLI
{

	public static void main(String[] args)
	{
		LSPCompilerHelper compiler = new LSPCompilerHelper();
		
		ArrayList mainPages = new ArrayList(); 

		for (int i = 0; i<args.length; i++)
		{
			if (args[i].charAt(0) == '-')
			{
				if (args[i].equals("-verbose"))
				{
					compiler.verbose = true;
				}
				else if (args[i].equals("-sourcepath"))
				{
					i++;
					compiler.sourceDir = new File(args[i]);
				}
				else if (args[i].equals("-d"))
				{
					i++;
					compiler.targetDir = new File(args[i]);
				}
				else
				{
					syntaxError();
					return;
				}	
			}
			else
			{
				mainPages.add(args[i]);	
			}
		}
		
		if (mainPages.isEmpty())
		{
			syntaxError();
			return;
		}			
		
		for (Iterator it = mainPages.iterator(); it.hasNext(); )
			compiler.doCompile((String)it.next());		
	}
	
		
	private static void syntaxError()
	{
		System.err.println("Syntax: lspc [-verbose] [-sourcepath sourcepath] [-d destpath] inputFile ...");	
	}

}

