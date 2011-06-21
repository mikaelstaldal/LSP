package nu.staldal.lsp.spring.webmvc;

import nu.staldal.lsp.spring.webmvc.test.AbstractWebApplicationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.ServletContext;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class LspViewDynamicEnclosePagesTest extends AbstractWebApplicationTest {

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
	public void definesParentPageInLspRootElement() throws Exception {
		// given
		final LspView view = resolveView("encloses/customPageLspRoot");
		// when
		addContentToPage(view);
		// then
		assertParentPageRendered();
	}

	private LspView resolveView(final String viewNam) throws Exception {
		return (LspView) viewResolver.resolveViewName(viewNam, LOCALE);
	}

	@Test
	public void definesParentPageInWhateverRootElement() throws Exception {
		// given
		final LspView view = resolveView("encloses/customPageWhateverRoot");
		// when
		addContentToPage(view);
		// then
		assertParentPageRendered();
	}

	@Test
	public void definesParentPageOneLevelDownTheTree() throws Exception {
		// given
		final LspView view = resolveView("encloses/subdir/customPageInSubdir");
		// when
		addContentToPage(view);
		// then
		assertParentPageRendered();
	}

	private void addContentToPage(final LspView view) throws Exception {
		final ModelMap model = new ModelMap();
		model.addAttribute("childValue", "childValue");
		model.addAttribute("parentValue", "parentValue");
		view.render(model, req, resp);
	}

	private void assertParentPageRendered() throws UnsupportedEncodingException {
		assertThat(resp.getContentAsString(), containsString("childValue"));
		assertThat(resp.getContentAsString(), containsString("parentValue"));
	}

}
