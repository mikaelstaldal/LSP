package nu.staldal.lsp.compiler;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.Collections;

import nu.staldal.lsp.LSPHelper;
import nu.staldal.lsp.LSPPage;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLSPCompilation
{
	private static LSPCompilerHelper lspCompilerHelper;
	private static LSPHelper lspHelper;
	private static MemoryClassLoader classLoader;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		lspCompilerHelper = new LSPCompilerHelper();
		classLoader = new MemoryClassLoader();
		lspHelper = new LSPHelper(classLoader);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		lspCompilerHelper = null;
		lspHelper = null;
	}

	private void doTest(String name, String lspText, String expectedResult)
		throws Exception
	{
    	byte[] classData = lspCompilerHelper.doCompileFromString(name, lspText);
    	classLoader.addClass("_LSP_"+name, classData);
    	
    	LSPPage thePage = lspHelper.getPage(name);
    	assertNotNull(thePage);
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	lspHelper.executePage(thePage, Collections.emptyMap(), null, baos);
    	assertEquals(expectedResult, baos.toString("UTF-8"));		
	}
	
    @Test
    public void testWhitespacePreserve()
		throws Exception
    {
    	doTest("WhitespacePreserve",

    			"<root xmlns:lsp='http://staldal.nu/LSP/core'>\n"
    		  + "<lsp:output encoding='UTF-8'/>"
    		  + "<ul xml:space='preserve'>\n"
    		  + "<lsp:for-each select='seq(1,10)' var='ent'>\n"
    		  + "<li><lsp:value-of select='$ent'/></li>\n"
    		  + "</lsp:for-each>\n"
    		  + "</ul>\n"
    		  + "</root>\n",
    		  
    			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
    		  + "<root>"
    		  + "<ul xml:space=\"preserve\">\n"
    		  + "\n"	    
    		  + "<li>1</li>\n"
    		  + "\n"	    
    		  + "<li>2</li>\n"
    		  + "\n"	    
    		  + "<li>3</li>\n"
    		  + "\n"	    
    		  + "<li>4</li>\n"
    		  + "\n"	    
    		  + "<li>5</li>\n"
    		  + "\n"	    
    		  + "<li>6</li>\n"
    		  + "\n"	    
    		  + "<li>7</li>\n"
    		  + "\n"	    
    		  + "<li>8</li>\n"
    		  + "\n"	    
    		  + "<li>9</li>\n"
    		  + "\n"	    
    		  + "<li>10</li>\n"
    		  + "\n"	        			    
    		  +	"</ul>\n"
    		  + "</root>");
    }

    @Test
    public void testWhitespaceStrip() throws Exception
    {
    	doTest("WhitespaceStrip",

    			"<root xmlns:lsp='http://staldal.nu/LSP/core'>\n"
    		  + "<lsp:output encoding='UTF-8'/>"
    		  + "<ul>\n"
    		  + "<lsp:for-each select='seq(1,10)' var='ent'>\n"
    		  + "<li><lsp:value-of select='$ent'/></li>\n"
    		  + "</lsp:for-each>\n"
    		  + "</ul>\n"
    		  + "</root>\n",
    		  
    			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
    		  + "<root>"
    		  + "<ul>\n"
    		  + "<li>1</li>"
    		  + "<li>2</li>"
    		  + "<li>3</li>"
    		  + "<li>4</li>"
    		  + "<li>5</li>"
    		  + "<li>6</li>"
    		  + "<li>7</li>"
    		  + "<li>8</li>"
    		  + "<li>9</li>"
    		  + "<li>10</li>\n"
    		  +	"</ul>\n"
    		  + "</root>");
    }
	
}
