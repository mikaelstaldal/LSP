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

import nu.staldal.syntax.*;

%%

%char
%class LSPExprLexer
%implements Lexer
%function nextToken
%type Token
%unicode

%eofval{
	return new EOFToken(-1, yychar+1);
%eofval}

DIGITS = [0-9]+

/* *** Unicode support */

LETTER = {BASECHAR}|{IDEOGRAPHIC}
BASECHAR = [A-Z]
IDEOGRAPHIC = [a-z]
DIGIT = [0-9]
COMBININGCHAR = "combiningchar"
EXTENDER = "extender"

NCNAME = ({LETTER}|_)({LETTER}|{DIGIT}|\.|-|_|{COMBININGCHAR}|{EXTENDER})*

VARNAME = ({LETTER}|_)({LETTER}|{DIGIT}|-|_|{COMBININGCHAR}|{EXTENDER})*

%%

"("  { return new DelimiterToken(yychar+1, DelimiterToken.LPAREN); }
")"  { return new DelimiterToken(yychar+1, DelimiterToken.RPAREN); }
","  { return new DelimiterToken(yychar+1, DelimiterToken.COMMA); }
or   { return new DelimiterToken(yychar+1, DelimiterToken.OR); }
and  { return new DelimiterToken(yychar+1, DelimiterToken.AND); }
"="  { return new DelimiterToken(yychar+1, DelimiterToken.EQ); }
"!=" { return new DelimiterToken(yychar+1, DelimiterToken.NE); }
"<"  { return new DelimiterToken(yychar+1, DelimiterToken.LT); }
"<=" { return new DelimiterToken(yychar+1, DelimiterToken.LE); }
">"  { return new DelimiterToken(yychar+1, DelimiterToken.GT); }
">=" { return new DelimiterToken(yychar+1, DelimiterToken.GE); }
"+"  { return new DelimiterToken(yychar+1, DelimiterToken.PLUS); }
"-"  { return new DelimiterToken(yychar+1, DelimiterToken.MINUS); }
"*"  { return new DelimiterToken(yychar+1, DelimiterToken.TIMES); }
div  { return new DelimiterToken(yychar+1, DelimiterToken.DIV); }
mod  { return new DelimiterToken(yychar+1, DelimiterToken.MOD); }


\"[^\"]*\"|'[^']*' { 
    String s = yytext();
    return new StringToken(yychar+1, s.substring(1, s.length()-1));
}

{DIGITS}(\.({DIGITS})?)?|\.{DIGITS} { 
    return new NumberToken(yychar+1, Double.valueOf(yytext()).doubleValue()); 
}

({NCNAME}:)?{NCNAME} {
    String s = yytext();
    int colon = s.indexOf(':');
    if (colon < 0)
    {
    	if (s.equals("comment") 
    		|| s.equals("text") 
    		|| s.equals("processing-instruction") 
    		|| s.equals("node"))
    	{
    	    return new IllegalToken(-1, yychar+1, yytext());
    	}
    	else
    	{
	    return new FunctionNameToken(yychar+1, null, s);
	}
    }
    else
    {
    	return new FunctionNameToken(yychar+1, 
    	    s.substring(0, colon), s.substring(colon+1, s.length()));
    }
}

\${VARNAME}(\.{VARNAME})? { 
    String s = yytext();
    int dot = s.indexOf('.');
    if (dot < 0)
    {
    	return new VariableReferenceToken(yychar+1, s.substring(1), null);
    }
    else
    {
    	return new VariableReferenceToken(yychar+1, 
    	    s.substring(1, dot), s.substring(dot+1, s.length()));
    }
}

(" "|\r|\n|\t)+ { }

. { return new IllegalToken(-1, yychar+1, yytext()); }
