package nu.staldal.lsp.framework;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class TestRestfulDispatcherServlet
{
    private RestfulDispatcherServlet dispatcherServlet;
    
    @Before
    public void setUp()
    {
        dispatcherServlet = new RestfulDispatcherServlet();
    }
    
    private void doTest(String expectedReturn, List expectedExtraArgs, String input) {
        HttpServletRequestMock mock = new HttpServletRequestMock(null, input);
        assertEquals(expectedReturn, dispatcherServlet.dispatchService(mock));
        assertEquals(expectedExtraArgs, mock.getAttribute("ExtraArgs"));
    }
    
    @Test
    public void testDispatchService() throws Exception
    {
        
        
        doTest("foobar", Arrays.asList(), "foobar");
        doTest("foobar", Arrays.asList(), "/foobar");
        doTest("foobar", Arrays.asList(), "foobar/");
        doTest("foobar", Arrays.asList(), "/foobar/");
        doTest("foobar", Arrays.asList(), "//foobar");

        doTest("foobar", Arrays.asList("17", "apa"), "//foobar/17/apa");
        doTest("foobar", Arrays.asList("17"), "//foobar/17");
        
        doTest("foo.bar", Arrays.asList(), "foo/bar");
        doTest("foo.bar", Arrays.asList(), "/foo/bar");
        doTest("foo.bar", Arrays.asList(), "/foo/bar/");
        doTest("foo.bar", Arrays.asList(), "/foo//bar");
        
        doTest("foo.bar", Arrays.asList("17"), "/foo/bar/17");
        doTest("foo.bar", Arrays.asList("17", "apa"), "/foo/bar/17/apa");
        
        doTest("", Arrays.asList(), "");
        doTest("", Arrays.asList(), "/");
        doTest("", Arrays.asList(), "//");
        doTest("", Arrays.asList(), null);
	}
	
}
