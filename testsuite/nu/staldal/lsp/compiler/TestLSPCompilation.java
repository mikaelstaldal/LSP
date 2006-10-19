package nu.staldal.lsp.compiler;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nu.staldal.lsp.LSPException;
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
        doTest(pageName, expectedResult, Collections.emptyMap());
	}

    private void doTest(String pageName, String expectedResult, Map params)
        throws Exception
    {
        lspCompilerHelper.doCompile(pageName + ".lsp", true);
        
        LSPPage thePage = lspHelper.getPage(pageName);
        assertNotNull(thePage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            lspHelper.executePage(thePage, params, null, baos);
        }
        catch (LSPException e)
        {
            e.printStackTrace();
            throw e;
        }
        String result = baos.toString("UTF-8");
        assertEquals(expectedResult, result);       
    }
    
    @Test(expected=nu.staldal.lsp.LSPException.class)
    public void testSimplePageWithError()
		throws Exception
    {
    	doTest("SimplePageError",
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

    @Test
    public void testHtmlAttribute() throws Exception
    {
        doTest("HtmlAttribute",
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
              + "<html>\n"
              + "<p class=\"foo&amp;\"></p>\n"
              + "<p class=\"foo&{bar}\"></p>\n"
              + "<p class=\"foo&amp;bar\"></p>\n"
              + "</html>");
    }

    @Test
    public void testXhtmlAttribute() throws Exception
    {
        doTest("XhtmlAttribute",
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
              + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
              + "<p class=\"foo&amp;\"></p>\n"
              + "<p class=\"foo&amp;{bar}\"></p>\n"
              + "<p class=\"foo&amp;bar\"></p>\n"
              + "</html>");
    }
    
    @Test(expected=nu.staldal.lsp.LSPException.class)
    public void testNullVariable1() throws Exception
    {
        doTest("NullVariable", "");
    }

    @Test(expected=nu.staldal.lsp.LSPException.class)
    public void testNullVariable2() throws Exception
    {
        Map params = new HashMap();
        params.put("theVar", null);
        doTest("NullVariable", "", params);
    }
    
    @Test(expected=nu.staldal.lsp.LSPException.class)
    public void testNullTupleValue1() throws Exception
    {
        Map params = new HashMap();
        Map tuple = new HashMap();
        params.put("theTuple", tuple);
        doTest("NullTupleValue", "", params);
    }

    @Test(expected=nu.staldal.lsp.LSPException.class)
    public void testNullTupleValue2() throws Exception
    {
        Map params = new HashMap();
        Map tuple = new HashMap();
        tuple.put("theNullValue", null);
        params.put("theTuple", tuple);
        doTest("NullTupleValue", "", params);
    }
    
}