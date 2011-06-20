package nu.staldal.lsp.spring.webmvc.test;

import nu.staldal.lsp.spring.webmvc.LspViewResolver;
import org.junit.Test;
import org.springframework.web.servlet.ViewResolver;

import static org.junit.Assert.*;

public class AppContextLoadingTest extends AbstractWebApplicationTest {

	@Test
	public void lspViewResolverInjected() {
		assertTrue(applicationContext.getBean(ViewResolver.class) instanceof LspViewResolver);
	}
}
