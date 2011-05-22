package nu.staldal.zt;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nu.staldal.lsp.LSPHelper;
import nu.staldal.lsp.LSPPage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class TestZt {
    private LSPHelper lspHelper;

    @Before
    public void setUp() throws Exception {
        lspHelper = new LSPHelper(new URLClassLoader(new URL[] { new File(new File(new File(new File("src"), "test"), "resources"), "ztPages").toURL() }));
    }

    @After
    public void tearDown() throws Exception {
        lspHelper = null;
    }

    private void doTest(String pageName, String expectedResult,
            Map<String, Object> params) throws Exception {
        LSPPage thePage = lspHelper.getPage(pageName);
        assertNotNull(thePage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            lspHelper.executePage(thePage, params, null, baos);
        }
        catch (SAXException e) {
            e.printStackTrace();
            throw e;
        }
        String result = baos.toString("UTF-8");
        System.out.println(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testZt() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("helloWorld", "Hello, World!");
        params.put("helloWorldEm", "<em>Hello, World!</em>");
        params.put("trueCond", true);
        params.put("falseCond", false);
        params.put("url", "http://www.foo.com/bar");
        params.put("rows", "5");
        params.put("cols", "40");
        params.put("text", "The textarea content");

        params.put("theList", Arrays.asList("foo", "bar", "baz", "buzz"));

        doTest("test",
               "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<body>\n"
                + "<span>Hello, World!</span>\n"
                + "<span class=\"big\">&lt;em&gt;Hello, World!&lt;/em&gt;</span>\n"
                + "<span id=\"hello\"><em>Hello, World!</em></span>\n"
                + "<a href=\"http://www.foo.com/bar\">A link</a>\n"
                + "<textarea cols=\"40\" rows=\"5\">The textarea content</textarea>\n"
                + "<div>\n" + "<h1>if true</h1>\n" + "</div>\n" + "\n"
                + "\n" + "<div>\n" + "<h1>if not false</h1>\n"
                + "</div>\n" + "<ul>\n" + "<li><em>foo</em></li>"
                + "<li><em>bar</em></li>" + "<li><em>baz</em></li>"
                + "<li><em>buzz</em></li>\n" + "\n" + "\n" + "</ul>\n"
                + "</body>", 
                params);
    }

    @Test
    public void testIf() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("trueCond", true);
        params.put("falseCond", false);

        doTest("if",
               "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<body>\n"
                + "<div>\n" + "<h1>if true</h1>\n" + "</div>\n" + "\n"
                + "</body>", 
                params);
    }

    @Test
    public void testIfUnbound() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("trueCond", true);

        doTest("if",
               "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<body>\n"
                + "<div>\n" + "<h1>if true</h1>\n" + "</div>\n" + "\n"
                + "</body>", 
                params);
    }
    
    @Test
    public void testIfString() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("trueCond", "foo");
        params.put("falseCond", "");

        doTest("if",
               "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<body>\n"
                + "<div>\n" + "<h1>if true</h1>\n" + "</div>\n" + "\n"
                + "</body>", 
                params);
    }
    
    @Test
    public void testIfArray() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("trueCond", new String[] { "foo", "bar" });
        params.put("falseCond", new String[] {});

        doTest("if",
               "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<body>\n"
                + "<div>\n" + "<h1>if true</h1>\n" + "</div>\n" + "\n"
                + "</body>", 
                params);
    }
    
    @Test
    public void testIfList() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("trueCond", Arrays.asList("foo", "bar"));
        params.put("falseCond", Collections.EMPTY_LIST);

        doTest("if",
               "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<body>\n"
                + "<div>\n" + "<h1>if true</h1>\n" + "</div>\n" + "\n"
                + "</body>", 
                params);
    }
    
    @Test
    public void testList() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("theList", Arrays.asList("foo", "bar", "baz", "buzz"));

        doTest("list",
               "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
               + "<body>\n"
               + "<ul>\n"
               + "<li>foo</li>"
               + "<li>bar</li>" 
               + "<li>baz</li>"
               + "<li>buzz</li>\n"
               + "\n" 
               + "\n" 
               + "</ul>\n"
               + "</body>", 
               params);
    }
    
    @Test
    public void testListOddEven() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("theList", Arrays.asList("foo", "bar", "baz", "buzz"));

        doTest("listoddeven",
               "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
               + "<body>\n"
               + "<ul>\n"
               + "<li class=\"odd\"><em>foo</em></li>"
               + "<li class=\"even\"><em>bar</em></li>" 
               + "<li class=\"odd\"><em>baz</em></li>"
               + "<li class=\"even\"><em>buzz</em></li>\n"
               + "\n" 
               + "\n" 
               + "</ul>\n"
               + "</body>", 
               params);
    }
    
    @Test
    public void testListOddEvenAttr() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("theList", Arrays.asList("foo", "bar", "baz", "buzz"));

        doTest("listoddevenattr",
               "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
               + "<body>\n"
               + "<ul>\n"
               + "<li class=\"foo odd\"><em>foo</em></li>"
               + "<li class=\"foo even\"><em>bar</em></li>" 
               + "<li class=\"foo odd\"><em>baz</em></li>"
               + "<li class=\"foo even\"><em>buzz</em></li>\n"
               + "\n" 
               + "\n" 
               + "</ul>\n"
               + "</body>", 
               params);
    }
    
    @Test
    public void testMatrix() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("theMatrix", Arrays.asList(
                Arrays.asList("one", "two", "three"),
                Arrays.asList("four", "five", "six"),
                Arrays.asList("seven", "eight", "nine")                     
            ));

        doTest("matrix",
               "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
               + "<body>\n"
               + "<table>\n"
               + "<tr>\n"
               + "<td>one</td>"
               + "<td>two</td>"
               + "<td>three</td>\n"
               + "</tr>"
               + "<tr>\n"
               + "<td>four</td>"
               + "<td>five</td>"
               + "<td>six</td>\n"
               + "</tr>"
               + "<tr>\n"
               + "<td>seven</td>"
               + "<td>eight</td>"
               + "<td>nine</td>\n"
               + "</tr>\n"
               + "</table>\n"
               + "</body>", 
               params);
    }
    
    @Test
    public void testMap() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        Map<String, Object> theMap = new HashMap<String, Object>();
        theMap.put("foo", "FOO");
        theMap.put("bar", "BAR");
        theMap.put("baz", "BAZ");
        params.put("theMap", theMap);

        Map<String, Object> fruit = new HashMap<String, Object>();
        fruit.put("name", "Apple");
        fruit.put("taste", "sweet");
        fruit.put("color", "green");
        params.put("theFruit", fruit);

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("name", "Orange");
        map1.put("taste", "sweet");
        map1.put("color", "orange");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("name", "Lemon");
        map2.put("taste", "sour");
        map2.put("color", "yellow");
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("name", "Strawberry");
        map3.put("taste", "sweet");
        map3.put("color", "red");
        params.put("theTable", Arrays.asList(map1, map2, map3));

        doTest("map", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<body>\n" + "<div>\n" + "<p>FOO</p>\n" + "<p>BAR</p>\n"
                + "<p>BAZ</p>\n" + "</div>\n" + "<div>\n"
                + "<p>Name: <span>Apple</span></p>\n"
                + "<p>Taste: <span>sweet</span></p>\n"
                + "<p>Color: <span>green</span></p>\n" + "</div>\n"
                + "<table>\n"
                + "<tr><th>Name</th><th>Taste</th><th>Color</th></tr>\n"
                + "<tr><td>Orange</td><td>sweet</td><td>orange</td></tr>"
                + "<tr><td>Lemon</td><td>sour</td><td>yellow</td></tr>"
                + "<tr><td>Strawberry</td><td>sweet</td><td>red</td></tr>\n"
                + "\n" + "</table>\n" + "</body>", params);
    }

    @Test
    public void testBean() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("theMap", new TestBean());
        params.put("theFruit", new FruitBean("Apple", "sweet", "green"));
        params.put("theTable", Arrays.asList(new FruitBean("Orange", "sweet",
                "orange"), new FruitBean("Lemon", "sour", "yellow"),
                new FruitBean("Strawberry", "sweet", "red")));

        doTest("map", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<body>\n" + "<div>\n" + "<p>-FOO-</p>\n" + "<p>-BAR-</p>\n"
                + "<p>-BAZ-</p>\n" + "</div>\n" + "<div>\n"
                + "<p>Name: <span>Apple</span></p>\n"
                + "<p>Taste: <span>sweet</span></p>\n"
                + "<p>Color: <span>green</span></p>\n" + "</div>\n"
                + "<table>\n"
                + "<tr><th>Name</th><th>Taste</th><th>Color</th></tr>\n"
                + "<tr><td>Orange</td><td>sweet</td><td>orange</td></tr>"
                + "<tr><td>Lemon</td><td>sour</td><td>yellow</td></tr>"
                + "<tr><td>Strawberry</td><td>sweet</td><td>red</td></tr>\n"
                + "\n" + "</table>\n" + "</body>", params);
    }

    @Test
    public void testInclude() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("x", "XXX");

        doTest("include",
               "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
             + "<body>\n"
             + "<h1>Start</h1>\n"
             + "<p><a href=\"http://www.foo.com/bar\">XXX</a></p>\n"
             + "<h1>End</h1>\n"
             + "</body>", 
                params);
    }

    @Test
    public void testEnclose() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("foo", "--FOO--");
        params.put("bar", "--BAR--");

        doTest("useenclose",
               "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
             + "<root>\n"
             + "<head>\n"
             + "<title>--FOO--</title>\n"
             + "</head>\n"
             + "<body>\n"
             + "<h1>Heading</h1>\n"
             + "<ul>\n"
             + "<li>Foo...</li>\n"
             + "<li>--BAR--</li>\n"
             + "</ul>\n"
             + "</body>\n"
             + "</root>",                
               params);
    }   

    @Test
    public void testNotFound() throws Exception {
        assertNull(lspHelper.getPage("bogus"));
    }    
    
    @Test
    public void testZtError() throws Exception {
        try {
            lspHelper.getPage("error");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            Throwable ee = e.getCause();
            assertNotNull(ee);
            assertTrue(ee instanceof SAXParseException);
            System.out.println(e.toString());            
        }
    }    

    @Test
    public void testXMLError() throws Exception {
        try {
            lspHelper.getPage("xmlerror");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            Throwable ee = e.getCause();
            assertNotNull(ee);
            assertTrue(ee instanceof SAXParseException);
            System.out.println(e.toString());            
        }
    }    

    @Test
    public void testCircularImport() throws Exception {
        try {
            lspHelper.getPage("circular");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            Throwable ee = e.getCause();
            assertNotNull(ee);
            assertTrue(ee instanceof SAXParseException);
            System.out.println(e.toString());            
        }
    }    
}
