package nu.staldal.lsp.framework;

import junit.framework.*;


public class TestFramework extends TestCase
{
    public TestFramework(String name)
    {
        super(name);
    }

    public void testFixServiceName() throws Exception
    {
        assertEquals("foobar", DispatcherServlet.fixServiceName("foobar"));
        assertEquals("foobar", DispatcherServlet.fixServiceName("/foobar"));
        assertEquals("foobar", DispatcherServlet.fixServiceName("foobar.s"));
        assertEquals("foobar", DispatcherServlet.fixServiceName("/foobar.s"));
        assertEquals("foobar", DispatcherServlet.fixServiceName("foobar.sss"));
        
        assertEquals("foo/bar", DispatcherServlet.fixServiceName("foo/bar"));
        assertEquals("foo/bar", DispatcherServlet.fixServiceName("/foo/bar"));
        assertEquals("foo/bar", DispatcherServlet.fixServiceName("foo/bar.s"));
        assertEquals("foo/bar", DispatcherServlet.fixServiceName("/foo/bar.s"));
        
        assertEquals("", DispatcherServlet.fixServiceName(""));
        assertEquals("", DispatcherServlet.fixServiceName("/"));
        assertEquals("", DispatcherServlet.fixServiceName("/."));
        assertEquals("", DispatcherServlet.fixServiceName("/.s"));
        assertEquals("", DispatcherServlet.fixServiceName("/.sss"));
        assertEquals("", DispatcherServlet.fixServiceName(null));
	}
	
}
