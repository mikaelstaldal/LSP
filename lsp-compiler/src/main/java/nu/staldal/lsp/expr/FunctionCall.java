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

package nu.staldal.lsp.expr;

import nu.staldal.lsp.compiler.LSPExpr;

import java.util.ArrayList;

/**
 * A function call
 */
public class FunctionCall extends LSPExpr
{
	protected String prefix;
	protected String name;
	protected ArrayList<LSPExpr> args;


	/**
	 * Create an FuncionCall from NameTokens
	 */
	public FunctionCall(NameToken prefix, NameToken name, int numArgs)
	{
		this.prefix = (prefix == null) ? null : prefix.getName();
		this.name = name.getName();
		args = new ArrayList<LSPExpr>(numArgs);
	}
	
	
	/**
	 * Create an FunctionCall
	 */
	public FunctionCall(String prefix, String name, int numArgs)
	{
		this.prefix = prefix;
		this.name = name;
		args = new ArrayList<LSPExpr>(numArgs);
	}

	
	/**
	 * Create an FunctionCall from NameTokens
	 */
	public FunctionCall(NameToken prefix, NameToken name)
	{
		this.prefix = (prefix == null) ? null : prefix.getName();
		this.name = name.getName();
		args = new ArrayList<LSPExpr>();
	}
	
	
	/**
	 * Create an FunctionCall
	 */
	public FunctionCall(String prefix, String name)
	{
		this.prefix = prefix;
		this.name = name;
		args = new ArrayList<LSPExpr>();
	}
	

	/**
	 * Add an argument to the function
	 *
	 * @param arg  the argument
	 */
	public void addArgument(LSPExpr arg)
	{
		args.add(arg);
	}

	/**
	 * Get the namespace prefix.
	 *
	 * @return the namespace prefix, or <code>null</code> if none
	 */
	public String getPrefix()
	{
		return prefix;
	}

	/**
	 * Get the function name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Get the number of arguments to the function.
	 */
	public int numberOfArgs()
	{
		return args.size();
	}

	/**
	 * Get one argument to the function
	 */
	public LSPExpr getArg(int index)
		throws ArrayIndexOutOfBoundsException
	{
		return args.get(index);
	}


	@Override
    public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("FunctionCall(");
		sb.append(prefix);
		sb.append(",");
		sb.append(name);
		sb.append(",[");
		for (int i = 0; i < numberOfArgs(); i++)
		{
			sb.append(getArg(i).toString());
			if (i < numberOfArgs()-1) sb.append(",");
		}
		sb.append("])");
		return sb.toString();
	}

}
