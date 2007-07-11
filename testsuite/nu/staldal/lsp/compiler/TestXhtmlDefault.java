package nu.staldal.lsp.compiler;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Map;

import nu.staldal.lsp.LSPHelper;
import nu.staldal.lsp.LSPPage;

import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test default output type. 
 *
 * @author Mikael Ståldal
 */
public class TestXhtmlDefault
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
        Map<String,Object> pageParams = Collections.emptyMap();
        lspHelper.executePage(thePage, pageParams, null, baos);
        String result = baos.toString("UTF-8");
        System.out.println(result);
        assertEquals(expectedResult, result);       
    }
    
    @Test
    public void testDefault()
        throws Exception
    {
        doTest("DefaultTest",
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>Test</title></head>\n"
                + "<body>\n"
                + "<p>Hello,<br />world!</p>\n"
                + "</body>\n"
                + "</html>");
    }

    @Test
    public void testXhtml()
        throws Exception
    {
        lspCompilerHelper.setHtml(false);
        doTest("DefaultTest",
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>Test</title></head>\n"
                + "<body>\n"
                + "<p>Hello,<br />world!</p>\n"
                + "</body>\n"
                + "</html>");
    }

    @Test
    public void testHtml()
        throws Exception
    {
        lspCompilerHelper.setHtml(true);
        doTest("DefaultTest",
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
                + "<html>\n"
                + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"><title>Test</title></head>\n"
                + "<body>\n"
                + "<p>Hello,<br>world!</p>\n"
                + "</body>\n"
                + "</html>");
    }
    
}