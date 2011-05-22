package nu.staldal.lsp.expr;

import nu.staldal.syntax.*;

import java.io.*;

public class TestLexer
{
	public static void main(String[] args) throws IOException
	{
		System.out.println(args[0]);
		System.out.println();

		LSPExprLexer lexer = new LSPExprLexer(new StringReader(args[0]));

		for (;;)
		{
			Token token = lexer.nextToken();
			if (token instanceof EOFToken) break;
			System.out.println(token);
		}
	}
}
