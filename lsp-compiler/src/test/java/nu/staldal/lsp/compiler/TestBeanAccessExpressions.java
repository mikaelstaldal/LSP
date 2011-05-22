package nu.staldal.lsp.compiler;

import nu.staldal.lsp.LSPException;
import nu.staldal.lsp.LSPHelper;
import nu.staldal.lsp.LSPPage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestBeanAccessExpressions {

    private static final String XML_PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    private static File classDir;

    private LSPCompilerHelper lspCompilerHelper;
    private LSPHelper lspHelper;

    @Test
    public void getter() throws Exception {
// when
        final HashMap<String, Object> context = createContext(new GetterBean());
// then
        assertBeanPropertyReadable(context);
    }

    public static class GetterBean {
        public String getProperty() {
            return "value";
        }
    }

    @Test
    public void field() throws Exception {
// when
        final HashMap<String, Object> context = createContext(new FieldBean());
// then
        assertBeanPropertyReadable(context);
    }

    public static class FieldBean {
        public final String property = "value";
    }

    @Test
    public void method() throws Exception {
// when
        final HashMap<String, Object> context = createContext(new MethodBean());
// then
        assertBeanPropertyReadable(context);
    }

    public static class MethodBean {
        public String property() {
            return "value";
        }
    }

    @Test(expected = LSPException.class)
    public void methodParametersNotAccepted() throws Exception {
// when
        final HashMap<String, Object> context = createContext(new ParametersBean());
// then
        assertBeanPropertyReadable(context);
    }

    public static class ParametersBean {
        public String property(final String parameter) {
            return "value";
        }
    }

    private HashMap<String, Object> createContext(final Object bean) {
        final HashMap<String, Object> context = new HashMap<String, Object>();
        context.put("bean", bean);
        return context;
    }

// TODO: Extract abstract class

    private void assertBeanPropertyReadable(final Map<String, Object> context)
            throws Exception {
        final String expectedResult = XML_PROLOG + "value";
        final String pageName = "Beans";

        lspCompilerHelper.doCompile(pageName + ".lsp", true);
        final LSPPage thePage = lspHelper.getPage(pageName);
        assertNotNull(thePage);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        lspHelper.executePage(thePage, context, null, baos);
        final String result = baos.toString("UTF-8");
        assertEquals(expectedResult, result);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        classDir = new File("LSPclasses");
        classDir.mkdir();
    }

    @Before
    public void setUp() throws Exception {
        lspCompilerHelper = new LSPCompilerHelper();
        lspCompilerHelper.setTargetDir(classDir);
        lspCompilerHelper.setStartDir(new File(new File(new File(new File("src"), "test"), "resources"), "lspPages"));
        lspHelper = new LSPHelper(new URLClassLoader(new URL[]{classDir.toURL()}));
    }

    @After
    public void tearDown() throws Exception {
        lspCompilerHelper = null;
        lspHelper = null;
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        for (final File f : classDir.listFiles()) {
            f.delete();
        }
        classDir.delete();
    }

}