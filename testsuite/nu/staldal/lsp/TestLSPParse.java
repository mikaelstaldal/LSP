package nu.staldal.lsp;

import java.io.*;

import nu.staldal.syntax.ParseException;
import nu.staldal.util.Utils;
import nu.staldal.lsp.compiler.*;

import junit.framework.*;

public class TestLSPParse extends TestCase
{
    public static final String correctExpr =
		"1+2-3.2*4 div f($a.b,$c)-\'4ad\'";
    public static final String correctTest = 
		"BinaryExpr(BinaryExpr(BinaryExpr(NumberLiteral(1.0),12,NumberLiteral(2.0)),13,BinaryExpr(BinaryExpr(NumberLiteral(3.2),14,NumberLiteral(4.0)),15,FunctionCall(null,f,[TupleExpr(VariableReference(a)[StringLiteral(b)]),VariableReference(c)]))),13,StringLiteral(4ad))";

    public static final String correctExpr2 = 
		"if (2 = 3) then 5+8 else \'foo\'";
    public static final String correctTest2 =
		"ConditionalExpr(BinaryExpr(NumberLiteral(2.0),6,NumberLiteral(3.0)),BinaryExpr(NumberLiteral(5.0),12,NumberLiteral(8.0)),StringLiteral(foo))";

    public static final String correctExpr3 = 
		"$Räksmörgåsëüû";
    public static final String correctTest3 =
		"VariableReference(Räksmörgåsëüû)";
        
    public static final String invalidExpr = "1+2-a-3.2*f($a.b,$c)";
    public static final String invalidExpr2 = "if 5 else 8";

    public TestLSPParse(String name)
    {
        super(name);
    }

    public void testLSPParse() throws Exception
    {
        LSPExpr correctParsed = LSPExpr.parseFromString(correctExpr);
        assertEquals(correctTest, correctParsed.toString());

        LSPExpr correctParsed2 = LSPExpr.parseFromString(correctExpr2);
        assertEquals(correctTest2, correctParsed2.toString());
    
        LSPExpr correctParsed3 = LSPExpr.parseFromString(correctExpr3);
        assertEquals(correctTest3, correctParsed3.toString());

        try {
    		LSPExpr invalidParsed = LSPExpr.parseFromString(invalidExpr);
            fail("Parser did not signal error in invalid expression: "
                + invalidExpr);
        }
        catch (ParseException e)
        { }

        try {
    		LSPExpr invalidParsed = LSPExpr.parseFromString(invalidExpr2);
            fail("Parser did not signal error in invalid expression: "
                + invalidExpr2);
        }
        catch (ParseException e)
        { }

	}
	
	public static void main(String[] args)
	{
		System.out.println(args[0]);
		try {
			LSPExpr parsed = LSPExpr.parseFromString(args[0]);
			System.out.println(parsed.toString());
        }
        catch (ParseException e)
        {
			System.out.println(Utils.nChars(e.getColumn()-1,' ') 
				+ "^ "+ e.getMessage());
		}
		
	}
}
