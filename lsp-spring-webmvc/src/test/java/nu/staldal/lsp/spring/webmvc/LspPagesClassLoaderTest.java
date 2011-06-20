package nu.staldal.lsp.spring.webmvc;

import org.junit.Test;

import static org.junit.Assert.*;

public class LspPagesClassLoaderTest {

	private static final String PAGE_NAME = "page_name";

	final LspPagesClassLoader classLoader = new LspPagesClassLoader(null);

	@Test
	public void remembersLoadedPages() throws Exception {
		// when
		classLoader.addClass(new LspClass(PAGE_NAME, null));
		// then
		assertTrue(classLoader.isPageLoaded(PAGE_NAME));
	}

	@Test
	public void changedSourceCodeIsNewPage() throws Exception {
		// when
		classLoader.addClass(new LspClass(PAGE_NAME, null));
		// then
		assertFalse(classLoader.isPageLoaded(PAGE_NAME + "changed"));
	}

}
