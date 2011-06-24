package nu.staldal.lsp.spring.webmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Locale;

public class LspViewResolver implements ViewResolver {

	@Autowired
	private WebApplicationContext applicationContext;

	@Autowired(required = false)
	private LspPageNameGenerator lspPageNameGenerator = new DefaultLspPageNameGenerator();

	private final LspPagesClassLoader lspPagesClassLoader;

	private String viewsPath;
	private String parentView;
	private String suffix = ".lsp";

	public LspViewResolver() {
		lspPagesClassLoader = new LspPagesClassLoader(Thread.currentThread().getContextClassLoader());
	}

	public void setLspPageNameGenerator(final LspPageNameGenerator lspPageNameGenerator) {
		this.lspPageNameGenerator = lspPageNameGenerator;
	}

	public void setViewsPath(final String viewsPath) {
		this.viewsPath = viewsPath;
	}

	public void setParentView(final String parentView) {
		this.parentView = parentView;
	}

	public void setSuffix(final String suffix) {
		this.suffix = suffix;
	}

	@Override
	public View resolveViewName(final String viewName, final Locale locale) throws Exception {
		final File viewFile = new File(getViewsDir(), viewName + suffix);
		final File parentViewFile = createParentViewFileOrNull();

		if (viewFile.exists() && (parentViewFile == null || parentViewFile.exists())) {
			return new LspView(applicationContext.getServletContext(), lspPagesClassLoader, lspPageNameGenerator, viewFile, parentViewFile);
		} else {
			return null;
		}
	}

	public File getViewsDir() throws MalformedURLException {
		final String realPath = applicationContext.getServletContext().getRealPath("");
		return new File(realPath, viewsPath);
	}

	public File createParentViewFileOrNull() throws MalformedURLException {
		if (parentView != null) {
			return new File(getViewsDir(), parentView + suffix);
		} else {
			return null;
		}
	}

}

