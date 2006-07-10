/*
 * Copyright (c) 2001-2003, Mikael Ståldal
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

package nu.staldal.lsp.compile;

import java.util.Vector;

import org.xml.sax.Locator;

import nu.staldal.lsp.compiler.LSPExpr;

public class LSPChoose extends LSPNode
{
	private Vector<LSPExpr> whenTests;
	private Vector<LSPNode> whenBodies;
	private LSPNode otherwise;

	public LSPChoose(int nWhens, Locator locator)
	{
        super(locator);
		whenTests = new Vector<LSPExpr>(nWhens);
		whenBodies = new Vector<LSPNode>(nWhens);
		otherwise = null;
	}

	public void addWhen(LSPExpr test, LSPNode body)
	{
		whenTests.addElement(test);
		whenBodies.addElement(body);
	}

	public void setOtherwise(LSPNode body)
	{
		otherwise = body;
	}


	public int getNWhens()
	{
		return whenTests.size();
	}

	public LSPExpr getWhenTest(int i)
	{
		return (LSPExpr)whenTests.elementAt(i);
	}

	public LSPNode getWhenBody(int i)
	{
		return (LSPNode)whenBodies.elementAt(i);
	}

	public LSPNode getOtherwise()
	{
		return otherwise;
	}
}
