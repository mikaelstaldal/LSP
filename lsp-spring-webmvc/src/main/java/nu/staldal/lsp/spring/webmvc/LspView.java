package nu.staldal.lsp.spring.webmvc;

import nu.staldal.lsp.LSPPage;
import nu.staldal.lsp.servlet.LSPManager;
import org.springframework.web.servlet.View;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

public class LspView implements View {

    private final ServletContext servletContext;
	private final LspPagesClassLoader lspPagesClassLoader;
	private final LspPageNameGenerator lspPageNameGenerator;
    final File pageFile;
	final File parentFile;

	public LspView(final ServletContext servletContext, final LspPagesClassLoader lspPagesClassLoader,
                   final LspPageNameGenerator lspPageNameGenerator, final File pageFile, final File parentViewFile) {
        this.servletContext = servletContext;
        this.pageFile = pageFile;
		this.parentFile = parentViewFile;
		this.lspPagesClassLoader = lspPagesClassLoader;
		this.lspPageNameGenerator = lspPageNameGenerator;
	}

	@Override
	public String getContentType() {
		return "text/html";
	}

	@Override
	public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		final LspClass lspClass = compileOrReuseLspPageClass();
//		FileUtils.writeByteArrayToFile(new File("/tmp/lastClass.class"), lspClass.getClassBytes());

		final LSPManager lspManager = LSPManager.getInstance(servletContext, lspPagesClassLoader);
		final LSPPage page = lspManager.getPage(lspClass.getPageName());

		lspManager.executePage(page, (Map<String, Object>) model, request, response);
	}

	LspClass compileOrReuseLspPageClass() {
		final String pageName = lspPageNameGenerator.generatePageName(pageFile);

		if (lspPagesClassLoader.isPageLoaded(pageName)) {
			return lspPagesClassLoader.getLspClass(pageName);
		} else {
			final LspClass lspClass = compileLspPage();
			lspPagesClassLoader.addClass(lspClass);
			return lspClass;
		}
	}

	private LspClass compileLspPage() {
		final LspHotCompilerHelper compiler = new LspHotCompilerHelper();
		compiler.setEncloseFile(parentFile);
		compiler.setAcceptUnbound(true);
		return compiler.doCompile(pageFile, lspPageNameGenerator);
	}

}
