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

import java.io.StringReader;

import nu.staldal.lsp.LSPExpr;
import nu.staldal.syntax.*;

public class LSPExprParser extends Parser
{

	private void consumeDelimiter(int type, String msg)
		throws ParseException, java.io.IOException
	{
        if (!(token instanceof DelimiterToken)
				|| !((DelimiterToken)token).isType(type))
		{
			throw new ParseException(msg, token);
		}

		token = lexer.nextToken();
		if (token instanceof IllegalToken)
		{
			throw new ParseException("Illegal token", token);
		}
	}

	private boolean consumeDelimiter(int type)
		throws ParseException, java.io.IOException
	{
		if (!(token instanceof DelimiterToken)
				|| !((DelimiterToken)token).isType(type))
		{
			return false;
		}
		token = lexer.nextToken();
		if (token instanceof IllegalToken)
		{
			throw new ParseException("Illegal token", token);
		}
		return true;
	}


	public LSPExprParser()
	{
		super();
	}


	public LSPExpr parse(Lexer lexer)
		throws ParseException, java.io.IOException
	{
		init(lexer);

		LSPExpr e = expr();

		if (!(token instanceof EOFToken))
		{
			throw new ParseException(
				"Unexpected token after end of input", token);
		}

        return e;
	}


	public LSPExpr parse(String s)
		throws ParseException
	{
		LSPExprLexer lexer = new LSPExprLexer(new StringReader(s));

		try {
			return parse(lexer);
		}
		catch (java.io.IOException e)
		{
			throw new Error("IOException when reading from String: " + e);
		}
	}


	/**
	 * Expr ::= OrExpr
	 */
	LSPExpr expr() throws ParseException, java.io.IOException
	{
		return orExpr();
	}


	/**
	 * PrimaryExpr ::= VariableReference
	 *			  	 | '(' Expr ')'
	 *			   	 | Literal
	 *			  	 | Number
	 *			  	 | FunctionCall
	 */
	LSPExpr primaryExpr() throws ParseException, java.io.IOException
	{
		if (token instanceof VariableReferenceToken)
		{
			return
				new VariableReference((VariableReferenceToken)consumeToken());
		}
		else if (consumeDelimiter(DelimiterToken.LPAREN))
		{
			LSPExpr e = expr();
			consumeDelimiter(DelimiterToken.RPAREN, ") expected");
			return e;
		}
		else if (token instanceof StringToken)
		{
			return new StringLiteral((StringToken)consumeToken());
		}
		else if (token instanceof NumberToken)
		{
			return new NumberLiteral((NumberToken)consumeToken());
		}
		else if (token instanceof FunctionNameToken)
		{
			return functionCall();
		}
		else
		{
			throw new ParseException("Unexpected token", token);
		}
	}

	/**
	 * FunctionCall ::= FunctionName '(' ( Argument ( ',' Argument )* )? ')'
	 */
	LSPExpr functionCall() throws ParseException, java.io.IOException
	{
		FunctionNameToken functionName =
			(FunctionNameToken)consumeToken(FunctionNameToken.class);
		consumeDelimiter(DelimiterToken.LPAREN, "( expected");
		FunctionCall call = new FunctionCall(functionName);

		if (!(token instanceof DelimiterToken) ||
			!((DelimiterToken)token).isType(DelimiterToken.RPAREN))
		{
			call.addArgument(argument());
			while ((token instanceof DelimiterToken) &&
				((DelimiterToken)token).isType(DelimiterToken.COMMA))
			{
				consumeDelimiter(DelimiterToken.COMMA, ", expected");
				call.addArgument(argument());
			}
		}

		consumeDelimiter(DelimiterToken.RPAREN, ") expected");
		return call;
	}


	/**
	 * Argument ::= Expr
	 */
	LSPExpr argument() throws ParseException, java.io.IOException
	{
		return expr();
	}

	/**
	 * OrExpr ::= AndExpr
	 *	    	| OrExpr 'or' AndExpr
	 */
	LSPExpr orExpr() throws ParseException, java.io.IOException
	{
		LSPExpr ret = andExpr();
		while (consumeDelimiter(DelimiterToken.OR))
		{
			ret = new BinaryExpr(ret, andExpr(), BinaryExpr.OR);
		}
		return ret;
	}

	/**
	 * AndExpr ::= EqualityExpr
	 *	    	 | AndExpr 'or' EqualityExpr
	 */
	LSPExpr andExpr() throws ParseException, java.io.IOException
	{
		LSPExpr ret = equalityExpr();
		while (consumeDelimiter(DelimiterToken.AND))
		{
			ret = new BinaryExpr(ret, equalityExpr(), BinaryExpr.AND);
		}
		return ret;
	}

	/**
	 * EqualtyExpr ::= RelaionalExpr
	 *		         | EqualityExpr '=' RelationalExpr
	 *		         | EqualityExpr '!=' RelationalExpr
	 */
	LSPExpr equalityExpr() throws ParseException, java.io.IOException
	{
		LSPExpr ret = relationalExpr();
		for (;;)
		{
			if (consumeDelimiter(DelimiterToken.EQ))
				ret = new BinaryExpr(ret, relationalExpr(), BinaryExpr.EQ);
			else if (consumeDelimiter(DelimiterToken.NE))
				ret = new BinaryExpr(ret, relationalExpr(), BinaryExpr.NE);
			else
				break;
		}
		return ret;
	}

	/**
	 * RelationalExpr ::= AdditiveExpr
	 * 			        | RelationalExpr '<' AdditiveExpr
	 * 			        | RelationalExpr '>' AdditiveExpr
	 * 			        | RelationalExpr '<=' AdditiveExpr
	 * 			        | RelationalExpr '>=' AdditiveExpr
	 */
	LSPExpr relationalExpr() throws ParseException, java.io.IOException
	{
		LSPExpr ret = additiveExpr();
		for (;;)
		{
			if (consumeDelimiter(DelimiterToken.LT))
				ret = new BinaryExpr(ret, additiveExpr(), BinaryExpr.LT);
			else if (consumeDelimiter(DelimiterToken.GT))
				ret = new BinaryExpr(ret, additiveExpr(), BinaryExpr.GT);
			else if (consumeDelimiter(DelimiterToken.LE))
				ret = new BinaryExpr(ret, additiveExpr(), BinaryExpr.LE);
			else if (consumeDelimiter(DelimiterToken.GE))
				ret = new BinaryExpr(ret, additiveExpr(), BinaryExpr.GE);
			else
				break;
		}
		return ret;
	}

	/**
	 * AdditiveExpr ::= MultiplicativeExpr
     *          	  | AdditiveExpr '+' MultiplicativeExpr
     *          	  | AdditiveExpr '-' MultiplicativeExpr
	 */
	LSPExpr additiveExpr() throws ParseException, java.io.IOException
	{
		LSPExpr ret = multiplicativeExpr();
		for (;;)
		{
			if (consumeDelimiter(DelimiterToken.PLUS))
				ret = new BinaryExpr(ret, multiplicativeExpr(),
					BinaryExpr.PLUS);
			else if (consumeDelimiter(DelimiterToken.MINUS))
				ret = new BinaryExpr(ret, multiplicativeExpr(),
					BinaryExpr.MINUS);
			else
				break;
		}
		return ret;
	}

	/**
	 * MultiplicativeExpr ::= UnaryExpr
	 *					    | MultiplicativeExpr '*' UnaryExpr
	 *			  	     	| MultiplicativeExpr 'div' UnaryExpr
	 *					    | MultiplicativeExpr 'mod' UnaryExpr
	 */
	LSPExpr multiplicativeExpr() throws ParseException, java.io.IOException
	{
		LSPExpr ret = unaryExpr();
		for (;;)
		{
			if (consumeDelimiter(DelimiterToken.TIMES))
				ret = new BinaryExpr(ret, unaryExpr(), BinaryExpr.TIMES);
			else if (consumeDelimiter(DelimiterToken.DIV))
				ret = new BinaryExpr(ret, unaryExpr(), BinaryExpr.DIV);
			else if (consumeDelimiter(DelimiterToken.MOD))
				ret = new BinaryExpr(ret, unaryExpr(), BinaryExpr.MOD);
			else
				break;
		}
		return ret;
	}

	/**
	 * UnaryExpr ::= PrimaryExpr
	 *			   | '-' UnaryExpr
	 */
	LSPExpr unaryExpr() throws ParseException, java.io.IOException
	{
		if (consumeDelimiter(DelimiterToken.MINUS))
		{
			return new UnaryExpr(unaryExpr());
		}
		else
		{
			return primaryExpr();
		}
	}
}
