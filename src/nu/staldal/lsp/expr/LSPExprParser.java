/*
 * Copyright (c) 2001-200`5, Mikael Ståldal
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

import java.io.IOException;
import java.io.StringReader;

import nu.staldal.lsp.compiler.LSPExpr;
import nu.staldal.syntax.*;

/**
 * Parser for LSP expressions.
 *
 * @author Mikael Ståldal
 */
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


	/**
	 * Create a new parser.
	 */
	public LSPExprParser()
	{
		super();
	}


	/**
     * Parse from a {@link nu.staldal.syntax.Lexer}
     * 
	 * @param lexer  the {@link nu.staldal.syntax.Lexer}
     * 
	 * @return the expression
     * 
	 * @throws ParseException the input has syntax error
	 * @throws IOException if the {@link nu.staldal.syntax.Lexer} throws it
	 */
	public LSPExpr parse(Lexer lexer)
		throws ParseException, IOException
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

    
	public static void checkName(String s)
		throws ParseException
	{
		LSPExprLexer lexer = new LSPExprLexer(new StringReader(s));

		try {  
            Token token = lexer.nextToken();
            if (!(token instanceof NameToken))
            {
                throw new ParseException("Illegal name", token);                
            }
            token = lexer.nextToken();
            if (!(token instanceof EOFToken))
            {
                throw new ParseException(
                    "Unexpected garbage after name", token);
            }            
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
		if (consumeDelimiter(DelimiterToken.DOLLAR))
		{
			return
				new VariableReference((NameToken)consumeToken(NameToken.class));
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
		else if (token instanceof NameToken)
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
	 * FunctionName ::= LSPName ( ':' LSPName )?
	 */
	LSPExpr functionCall() throws ParseException, java.io.IOException
	{
		NameToken functionName1 =
			(NameToken)consumeToken(NameToken.class);
		NameToken functionName2 = null;
		
		if (consumeDelimiter(DelimiterToken.COLON))
		{
			functionName2 = (NameToken)consumeToken(NameToken.class);
		}
		
		if (functionName2 == null &&
			(functionName1.getName().equals("comment")
				|| functionName1.getName().equals("text")
				|| functionName1.getName().equals("processing-instruction")
				|| functionName1.getName().equals("node")))
		{
			throw new ParseException("function name may not be NodeType", 
				functionName1);
		}
			
					
		consumeDelimiter(DelimiterToken.LPAREN, "( expected");
		FunctionCall call = 
			(functionName2 == null) 
				? new FunctionCall(null, functionName1)
				: new FunctionCall(functionName1, functionName2);

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
	 * AndExpr ::= IfExpr
	 *	    	 | AndExpr 'or' IfExpr
	 */
	LSPExpr andExpr() throws ParseException, java.io.IOException
	{
		LSPExpr ret = ifExpr();
		while (consumeDelimiter(DelimiterToken.AND))
		{
			ret = new BinaryExpr(ret, ifExpr(), BinaryExpr.AND);
		}
		return ret;
	}

	/**
	 * IfExpr ::= EqualityExpr
     *   	    | 'if' '(' Expr ')' 'then' Expr 'else' EqualityExpr 
	 */
	LSPExpr ifExpr() throws ParseException, java.io.IOException
	{
		if (consumeDelimiter(DelimiterToken.IF))
		{
			consumeDelimiter(DelimiterToken.LPAREN, "( expected");
			LSPExpr testExpr = expr();
			consumeDelimiter(DelimiterToken.RPAREN, ") expected");
			consumeDelimiter(DelimiterToken.THEN, "then expected");
			LSPExpr thenExpr = expr();
			consumeDelimiter(DelimiterToken.ELSE, "else expected");
			LSPExpr elseExpr = equalityExpr();
			
			return new ConditionalExpr(testExpr, thenExpr, elseExpr);
		}
		else
		{
			return equalityExpr();
		}
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
	 * UnaryExpr ::= TupleExpr
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
			return tupleExpr();
		}
	}

	/**
	 * TupleExpr ::= PrimaryExpr
	 *			   | TupleExpr '.' LSPName
	 *			   | TupleExpr '[' Expr ']'
	 */
	LSPExpr tupleExpr() throws ParseException, java.io.IOException
	{
		LSPExpr ret = primaryExpr();
		while (true)
		{
			if (consumeDelimiter(DelimiterToken.LBRACK))
			{
				ret = new TupleExpr(ret, expr());
				consumeDelimiter(DelimiterToken.RBRACK, "] expected");				
			}		
			else if (consumeDelimiter(DelimiterToken.DOT))
			{
				ret = new TupleExpr(
					ret, new StringLiteral(((NameToken)consumeToken(NameToken.class)).getName()));
			}
			else
			{
				break;
			}
		}
		return ret;
	}

}
