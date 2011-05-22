package nu.staldal.lsp.compiler;

import static org.junit.Assert.*;

import java.io.IOException;

import nu.staldal.lsp.LSPException;
import nu.staldal.lsp.URLResolver;

//import org.junit.After;
//import org.junit.Before;
import org.junit.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TestLSPCompile
{
    @Test
	public void checkPageName()
    {
    	assertTrue(LSPCompiler.checkPageName("A"));
    	assertTrue(LSPCompiler.checkPageName("A1"));
    	assertTrue(LSPCompiler.checkPageName("_A1"));
    	assertFalse(LSPCompiler.checkPageName("1A"));
    	assertFalse(LSPCompiler.checkPageName(""));
    	assertFalse(LSPCompiler.checkPageName("illegal-name"));
    }

    @Test(expected=nu.staldal.lsp.LSPException.class)
    public void testIllegalPageName() throws LSPException
    {
    	LSPCompiler lspCompiler = new LSPCompiler();
    	lspCompiler.startCompile("illegal-name", 
			new URLResolver() {
				public void resolve(String url, ContentHandler ch) 
					throws IOException, SAXException
				{
					// dummy	
				}
			});
    }    
}
