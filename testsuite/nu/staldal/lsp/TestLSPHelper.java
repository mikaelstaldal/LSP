package nu.staldal.lsp;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;

import nu.staldal.lsp.LSPHelper;
import nu.staldal.lsp.LSPPage;
import nu.staldal.lsp.compiler.LSPCompilerHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLSPHelper
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

    @Test
    public void testString()
        throws Exception
    {
        lspCompilerHelper.doCompile("SimplePage.lsp", true);
        
        LSPPage thePage = lspHelper.getPage("SimplePage");
        assertNotNull(thePage);
        String result = lspHelper.executePage(thePage, Collections.emptyMap(), null);
        System.out.println(result);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                      + "<root>\n"
                      + "<p>Hello, world!</p>\n"
                      + "</root>",
                    result);       
    }

    @Test
    public void testHtmlMethod()
        throws Exception
    {
        lspCompilerHelper.doCompile("HtmlMethod.lsp", true);
        
        LSPPage thePage = lspHelper.getPage("HtmlMethod");
        assertNotNull(thePage);
        String result = lspHelper.executePage(thePage, Collections.emptyMap(), null);
        System.out.println(result);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html>\n"
                + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Test</title></head>\n"
                + "<body>\n"                
                + "<p>Hello,<br>world!</p>\n"
                + "</body>\n"
                + "</html>",
                result);       
    }

    @Test
    public void testHtmlFragmentMethod()
        throws Exception
    {
        lspCompilerHelper.doCompile("HtmlFragmentMethod.lsp", true);
        
        LSPPage thePage = lspHelper.getPage("HtmlFragmentMethod");
        assertNotNull(thePage);
        String result = lspHelper.executePage(thePage, Collections.emptyMap(), null);
        System.out.println(result);
        assertEquals("<div>\n"
                   + "<p>Hello,<br>world!</p>\n"
                   + "</div>",
                    result);       
    }

    @Test
    public void testXhtmlMethod()
        throws Exception
    {
        lspCompilerHelper.doCompile("XhtmlMethod.lsp", true);
        
        LSPPage thePage = lspHelper.getPage("XhtmlMethod");
        assertNotNull(thePage);
        String result = lspHelper.executePage(thePage, Collections.emptyMap(), null);
        System.out.println(result);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>Test</title></head>\n"
                + "<body>\n"                
                + "<p>Hello,<br />world!</p>\n"
                + "</body>\n"
                + "</html>",
                    result);       
    }

    @Test
    public void testXhtmlFragmentMethod()
        throws Exception
    {
        lspCompilerHelper.doCompile("XhtmlFragmentMethod.lsp", true);
        
        LSPPage thePage = lspHelper.getPage("XhtmlFragmentMethod");
        assertNotNull(thePage);
        String result = lspHelper.executePage(thePage, Collections.emptyMap(), null);
        System.out.println(result);
        assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                   + "<p>Hello,<br />world!</p>\n"
                   + "</div>",
                    result);       
    }

    @Test
    public void testXmlMethod()
        throws Exception
    {
        lspCompilerHelper.doCompile("XmlMethod.lsp", true);
        
        LSPPage thePage = lspHelper.getPage("XmlMethod");
        assertNotNull(thePage);
        String result = lspHelper.executePage(thePage, Collections.emptyMap(), null);
        System.out.println(result);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                   + "<foo xmlns=\"http://foobar.com/foo\">\n"
                   + "<p>Hello,<br/>world!</p>\n"
                   + "</foo>",
                    result);       
    }

    @Test
    public void testTextMethod()
        throws Exception
    {
        lspCompilerHelper.doCompile("TextMethod.lsp", true);
        
        LSPPage thePage = lspHelper.getPage("TextMethod");
        assertNotNull(thePage);
        String result = lspHelper.executePage(thePage, Collections.emptyMap(), null);
        System.out.println(result);
        assertEquals("First line\n"
                   + "Second line\n"
                   + "Third line\n",
                    result);       
    }
    
}