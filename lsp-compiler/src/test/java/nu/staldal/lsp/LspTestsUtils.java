package nu.staldal.lsp;

import java.io.File;

public class LspTestsUtils {

	public static File getLspPagesDir() {
		final File normalDir = new File("src/test/resources/lspPages");
		if (normalDir.exists()) {
			return normalDir;
		} else {
			final File intellijIdeaDir = new File("lsp-compiler", normalDir.getPath());
			return intellijIdeaDir;
		}
	}

}
