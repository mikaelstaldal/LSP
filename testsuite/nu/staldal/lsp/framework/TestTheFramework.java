package nu.staldal.lsp.framework;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import nu.staldal.lsp.compiler.LSPCompilerHelper;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTheFramework
{
    private static File classDir;
    private static ClassLoader savedClassLoader;    
    
    private static LSPCompilerHelper lspCompilerHelper;
    
    private static DispatcherServlet dispatcherServlet;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        classDir = new File("LSPclasses");
        classDir.mkdir();
        savedClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { classDir.toURL() }));

        lspCompilerHelper = new LSPCompilerHelper();
        lspCompilerHelper.setTargetDir(classDir);
        lspCompilerHelper.setStartDir(new File(new File("testsuite"), "lspPages"));        

        ServletContextMock servletContextMock = new ServletContextMock();
        servletContextMock.setInitParameter("ServicePackages", "nu.staldal.lsp.framework");
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init(servletContextMock);            
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        dispatcherServlet.destroy();
        dispatcherServlet = null;
        lspCompilerHelper = null;
        
        Thread.currentThread().setContextClassLoader(savedClassLoader);
        for (File f : classDir.listFiles()) { f.delete(); }
        classDir.delete();
    }

    private void doTest(String pageName, String serviceName, String expectedResult)
        throws Exception
    {
        lspCompilerHelper.doCompile(pageName + ".lsp", true);
        
        HttpServletResponseMock response = new HttpServletResponseMock();
        dispatcherServlet.doGet(new HttpServletRequestMock("/"+serviceName), response);
        
        String result = response.toString("UTF-8");
        // System.out.println(result);
        assertEquals(expectedResult, result);       
    }
    
    @Test
    public void testService()
        throws Exception
    {
        doTest("TestPage", "Service1",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
              + "<root>Service1: servletPath=/Service1 init=1 instanceCounter=1</root>");

        doTest("TestPage", "Service1",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
              + "<root>Service1: servletPath=/Service1 init=1 instanceCounter=2</root>");
        
        doTest("TestPage", "Service1",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
              + "<root>Service1: servletPath=/Service1 init=1 instanceCounter=3</root>");
    }

    @Test
    public void testThrowawayService()
        throws Exception
    {
        doTest("TestPage", "ThrowawayService1",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<root>ThrowawayService1: servletPath=/ThrowawayService1 instanceCounter=1</root>");

        doTest("TestPage", "ThrowawayService1",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<root>ThrowawayService1: servletPath=/ThrowawayService1 instanceCounter=1</root>");

        doTest("TestPage", "ThrowawayService1",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<root>ThrowawayService1: servletPath=/ThrowawayService1 instanceCounter=1</root>");
    }
}
