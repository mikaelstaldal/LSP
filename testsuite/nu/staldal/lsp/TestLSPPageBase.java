package nu.staldal.lsp;

import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class TestLSPPageBase extends TestCase
{
    private StringHandler sax;    
    
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        sax = new StringHandler();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
        sax = null;
    }

    public void testOutputStringWithout() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "foo", false);
        LSPPageBase.outputStringWithoutCR(sax, "bar", false);
        assertEquals("foobar", sax.getBuf().toString());
    }

    public void testOutputStringWithoutLF1() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "\n", false);
        assertEquals("\n", sax.getBuf().toString());
    }

    public void testOutputStringWithoutLF2() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "foo\n", false);
        assertEquals("foo\n", sax.getBuf().toString());
    }

    public void testOutputStringWithoutLF3() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "\nfoo", false);
        assertEquals("\nfoo", sax.getBuf().toString());
    }

    public void testOutputStringWithoutLF4() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "foo\nbar", false);
        assertEquals("foo\nbar", sax.getBuf().toString());
    }

    
    public void testOutputStringWithoutCR1() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "\r", false);
        assertEquals("\n", sax.getBuf().toString());
    }

    public void testOutputStringWithoutCR2() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "foo\r", false);
        assertEquals("foo\n", sax.getBuf().toString());
    }

    public void testOutputStringWithoutCR3() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "\rfoo", false);
        assertEquals("\nfoo", sax.getBuf().toString());
    }

    public void testOutputStringWithoutCR4() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "foo\rbar", false);
        assertEquals("foo\nbar", sax.getBuf().toString());
    }

    
    public void testOutputStringWithoutCRLF1() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "\r\n", false);
        assertEquals("\n", sax.getBuf().toString());
    }

    public void testOutputStringWithoutCRLF2() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "foo\r\n", false);
        assertEquals("foo\n", sax.getBuf().toString());
    }

    public void testOutputStringWithoutCRLF3() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "\r\nfoo", false);
        assertEquals("\nfoo", sax.getBuf().toString());
    }

    public void testOutputStringWithoutCRLF4() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "foo\r\nbar", false);
        assertEquals("foo\nbar", sax.getBuf().toString());
    }

    
    public void testOutputStringWithoutLFCR1() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "\n\r", false);
        assertEquals("\n\n", sax.getBuf().toString());
    }

    public void testOutputStringWithoutLFCR2() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "foo\n\r", false);
        assertEquals("foo\n\n", sax.getBuf().toString());
    }

    public void testOutputStringWithoutLFCR3() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "\n\rfoo", false);
        assertEquals("\n\nfoo", sax.getBuf().toString());
    }

    public void testOutputStringWithoutLFCR4() throws SAXException
    {
        LSPPageBase.outputStringWithoutCR(sax, "foo\n\rbar", false);
        assertEquals("foo\n\nbar", sax.getBuf().toString());
    }
}
