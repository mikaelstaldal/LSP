package nu.staldal.lsp.spring.webmvc;

import nu.staldal.lsp.spring.webmvc.test.AbstractWebApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

import static org.junit.Assert.*;

public class LspViewResolverTest extends AbstractWebApplicationTest {

	@Autowired
	private ViewResolver viewResolver;

	@Test
	public void resolvesView() throws Exception {
		// when
		final View view = viewResolver.resolveViewName("resolvesView", Locale.US);
		// then
		assertTrue(view instanceof LspView);
	}

}
