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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLSPCompilationEnclose
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
		// for (File f : classDir.listFiles()) { f.delete(); }
		// classDir.delete();
	}

	private void doTest(String pageName, String encloseName, String expectedResult)
		throws Exception
	{
    	lspCompilerHelper.setEncloseFile(
    		new File(new File(new File("testsuite"), "lspPages"), encloseName));
		
		lspCompilerHelper.doCompile(pageName + ".lsp", true);
    	
    	LSPPage thePage = lspHelper.getPage(pageName);
    	assertNotNull(thePage);
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	lspHelper.executePage(thePage, Collections.emptyMap(), null, baos);
    	assertEquals(expectedResult, baos.toString("UTF-8"));
	}
	
    @Test
    public void testXHTML()
		throws Exception
    {
    	doTest("TransformedPage", "xhtmlEnclose.lsp",
    			"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
    		  + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
    		  + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
    		  + "<title>Test</title>\n"
    		  + "<meta name=\"foo\" content=\"bar\" />\n"
    		  + "</head>\n"
    		  + "<body>\n"
    		  + "<h1>Test</h1>\n"
    		  + "<div>\n"
    		  + "<p>Hello, world!</p>\n"
    		  + "</div>\n"
    		  + "</body>\n"
    		  + "</html>");    			
    }	

    @Test
    public void testXHTML2()
		throws Exception
    {
    	doTest("TransformedPage2", "xhtmlEnclose.lsp",
    			"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
    		  + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
    		  + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
    		  + "<title>Test</title>\n"
    		  + "\n"
    		  + "</head>\n"
    		  + "<body>\n"
    		  + "<h1>Test</h1>\n"
    		  + "<div>\n"
    		  + "<p>Other page</p>\n"
    		  + "</div>\n"
    		  + "</body>\n"
    		  + "</html>");    			
    }	

    @Test
    public void testHTML()
		throws Exception
    {
    	doTest("TransformedPage", "htmlEnclose.lsp",
    			"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
    		  + "<html>\n"
    		  + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
    		  + "<title>Test</title>\n"
    		  + "<meta name=\"foo\" content=\"bar\">\n"
    		  + "</head>\n"
    		  + "<body>\n"
    		  + "<h1>Test</h1>\n"
    		  + "<div>\n"
    		  + "<p>Hello, world!</p>\n"
    		  + "</div>\n"
    		  + "</body>\n"
    		  + "</html>");    			
    }	

    @Test
    public void testError()
		throws Exception
    {
    	try {
    		doTest("TransformedPageError", "htmlEnclose.lsp", "");
    		fail("Should throw LSPException");
    	}
    	catch (nu.staldal.lsp.LSPException e)
    	{
    		System.err.println(e.toString());
    	}
    }	

    @Test
    public void testError2()
		throws Exception
    {
    	try {
    		doTest("TransformedPageLSPError", "htmlEnclose.lsp", "");
    		fail("Should throw LSPException");
    	}
    	catch (nu.staldal.lsp.LSPException e)
    	{
    		System.err.println(e.toString());
    	}
    }	
}
