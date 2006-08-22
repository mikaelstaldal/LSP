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
}