package nu.staldal.lsp.compiler;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;

import nu.staldal.lsp.LSPHelper;
import nu.staldal.lsp.LSPPage;

import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLSPCompilation
{
	private static File classDir;
	
	private LSPCompilerHelper lspCompilerHelper;
	private LSPHelper lspHelper;
		
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		classDir = new File("LSPclasses");
		classDir.mkdir();
	}
		
	@Before
	public void setUp() throws Exception
	{
		lspCompilerHelper = new LSPCompilerHelper();
		lspCompilerHelper.setTargetDir(classDir);
		lspCompilerHelper.setStartDir(new File(new File("testsuite"), "lspPages"));
		lspHelper = new LSPHelper(new URLClassLoader(new URL[] { classDir.toURL() }));
	}

	@After
	public void tearDown() throws Exception
	{
		lspCompilerHelper = null;
		lspHelper = null;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		for (File f : classDir.listFiles()) { f.delete(); }
		classDir.delete();
	}

	private void doTest(String pageName, String expectedResult)
		throws Exception
	{
    	lspCompilerHelper.doCompile(pageName + ".lsp", true);
    	
    	LSPPage thePage = lspHelper.getPage(pageName);
    	assertNotNull(thePage);
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	lspHelper.executePage(thePage, Collections.emptyMap(), null, baos);
    	assertEquals(expectedResult, baos.toString("UTF-8"));		
	}
	
    @Test(expected=nu.staldal.lsp.LSPException.class)
    public void testSimplePageWithError()
		throws Exception
    {
    	doTest("SimplePage",
    			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
    		  + "<root>\n"
    		  + "<p>Hello, world!</p>\n"
    		  + "</root>");
    }
	
    @Test
    public void testWhitespacePreserve()
		throws Exception
    {
    	doTest("WhitespacePreserve",
    			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
    		  + "<root>\n"
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
    			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
    		  + "<root>\n"
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
