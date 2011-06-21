package nu.staldal.lsp.spring.webmvc;

import nu.staldal.lsp.spring.webmvc.test.AbstractWebApplicationTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LspViewTest extends AbstractWebApplicationTest {

	@Autowired
	private ViewResolver viewResolver;

	private static final Locale LOCALE = Locale.US;

	private ServletContext servletContext;
	private MockHttpServletRequest req;
	private MockHttpServletResponse resp;

	@Before
	public void setUp() throws Exception {
		servletContext = applicationContext.getServletContext();
		req = new MockHttpServletRequest(servletContext, "GET", "/");
		resp = new MockHttpServletResponse();
	}

	@Test
	public void rendersView() throws Exception {
		// given
		final View view = viewResolver.resolveViewName("resolvesView", LOCALE);
		// when
		final ModelMap model = new ModelMap();
		final String pageContent = "example content";
		model.addAttribute("content", pageContent);
		view.render(model, req, resp);
		// then
		assertThat(resp.getContentAsString(), containsString(pageContent));
	}

	@Test
	public void reusesCompiledPageClass() throws Exception {
		// given
		final LspView view = (LspView) viewResolver.resolveViewName("resolvesView", LOCALE);
		// when
		final LspClass firstClass = view.compileOrReuseLspPageClass();
		final LspClass secondClass = view.compileOrReuseLspPageClass();
		// then
		assertThat(firstClass.getClassBytes(), equalTo(secondClass.getClassBytes()));
	}

	@Test
	public void recompilesChangedPage() throws Exception {
		// given
		final LspView view = (LspView) viewResolver.resolveViewName("resolvesView", LOCALE);
		final File pageFile = view.pageFile;
		final File parentFile = view.parentFile; // make use of configured LspViewResolver to find page files for us

		final LspPagesClassLoader lspPagesClassLoader = new LspPagesClassLoader(null);
		final LspPageNameGenerator lspPageNameGenerator = createLspPageNameGenerator();

		// when
		final LspView firstView = new LspView(servletContext, lspPagesClassLoader, lspPageNameGenerator, pageFile, parentFile);
		final LspClass firstClass = firstView.compileOrReuseLspPageClass();

		final LspView secondView = new LspView(servletContext, lspPagesClassLoader, lspPageNameGenerator, pageFile, parentFile);
		final LspClass secondClass = secondView.compileOrReuseLspPageClass();
		// then
		assertThat(firstClass.getClassBytes(), equalTo(secondClass.getClassBytes()));
	}

	private LspPageNameGenerator createLspPageNameGenerator() {
		final LspPageNameGenerator lspPageNameGenerator = mock(LspPageNameGenerator.class);
		when(lspPageNameGenerator.generatePageName(Matchers.<File>any())).thenReturn("page1", "page2");
		return lspPageNameGenerator;
	}

}
