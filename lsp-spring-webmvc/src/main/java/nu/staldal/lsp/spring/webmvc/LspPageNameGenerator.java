package nu.staldal.lsp.spring.webmvc;

import java.io.File;

public interface LspPageNameGenerator {

	String generatePageName(File pageFile);

}
