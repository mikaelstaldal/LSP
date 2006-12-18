package nu.staldal.lsp.framework;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javax.servlet.http.HttpServletResponse;

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

    private void doTest(String pageName, String serviceName, String expectedResult, int expectedSc, String... params)
        throws Exception
    {
        lspCompilerHelper.doCompile(pageName + ".lsp", true);
        
        HttpServletRequestMock request = new HttpServletRequestMock("/"+serviceName);
        for (int i = 0; i<params.length; i+=2)
        {
            request.setParameter(params[i], params[i+1]);
        }
        HttpServletResponseMock response = new HttpServletResponseMock();
        dispatcherServlet.doGet(request, response);

        assertEquals(expectedSc, response.getSc());
        
        if (expectedResult != null)
        {
            String result = response.toString("UTF-8");
            // System.out.println(result);
            assertEquals(expectedResult, result);
        }
    }
    
    @Test
    public void testService()
        throws Exception
    {
        doTest("TestPage", "Service1",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
              + "<root>Service1: servletPath=/Service1 init=1 instanceCounter=1</root>",
                HttpServletResponse.SC_OK);

        doTest("TestPage", "Service1",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
              + "<root>Service1: servletPath=/Service1 init=1 instanceCounter=2</root>",
                HttpServletResponse.SC_OK);
        
        doTest("TestPage", "Service1",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
              + "<root>Service1: servletPath=/Service1 init=1 instanceCounter=3</root>",
              HttpServletResponse.SC_OK);
              
    }

    @Test
    public void testThrowawayService()
        throws Exception
    {
        doTest("TestPage", "ThrowawayService1",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<root>ThrowawayService1: servletPath=/ThrowawayService1 instanceCounter=1</root>",
                HttpServletResponse.SC_OK);

        doTest("TestPage", "ThrowawayService1",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<root>ThrowawayService1: servletPath=/ThrowawayService1 instanceCounter=1</root>",
                HttpServletResponse.SC_OK);

        doTest("TestPage", "ThrowawayService1",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<root>ThrowawayService1: servletPath=/ThrowawayService1 instanceCounter=1</root>",
                HttpServletResponse.SC_OK);
    }

    @Test
    public void testThrowawayServiceWithParam()
        throws Exception
    {
        doTest("TestPage", "ThrowawayServiceWithParam",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<root>ThrowawayServiceWithParam:"
                + " mandatoryParam=mandatoryValue" 
                + " param=value"
                + " notUsedParam=defaultValue"
                + " paramWithStrangeName=strangeValue"
                + " intParam=17"
                + " doubleParam=47.11"
                + " notUsedIntParam=0"
                + " integerParam=123"
                + " notUsedIntegerParam=null"
                + " charParam=z"
                + " enumParam=BAR"
                + "</root>",
                HttpServletResponse.SC_OK,
                "mandatoryParam", "mandatoryValue",
                "param", "value",
                "strangeParam", "strangeValue",
                "intParam", "17",
                "doubleParam", "47.11",
                "integerParam", "123",
                "charParam", "z",
                "enumParam", "BAR");
    }

    @Test
    public void testThrowawayServiceWithParam2()
        throws Exception
    {
        doTest("TestPage", "ThrowawayServiceWithParam",
                null,
                HttpServletResponse.SC_BAD_REQUEST,
                "param", "value",
                "strangeParam", "strangeValue");
    }

    @Test
    public void testThrowawayServiceWithParam3()
        throws Exception
    {
        doTest("TestPage", "ThrowawayServiceWithParam",
                null,
                HttpServletResponse.SC_BAD_REQUEST,
                "mandatoryParam", "mandatoryValue",
                "intParam", "abc");
                
    }

    @Test
    public void testThrowawayServiceWithParam4()
        throws Exception
    {
        doTest("TestPage", "ThrowawayServiceWithParam",
                null,
                HttpServletResponse.SC_BAD_REQUEST,
                "mandatoryParam", "mandatoryValue",
                "charParam", "");
                
    }

    @Test
    public void testThrowawayServiceWithParam5()
        throws Exception
    {
        doTest("TestPage", "ThrowawayServiceWithParam",
                null,
                HttpServletResponse.SC_BAD_REQUEST,
                "mandatoryParam", "mandatoryValue",
                "enumParam", "foo");
                
    }
}
