package nu.staldal.lsp.spring.webmvc;

import org.apache.commons.lang.StringUtils;

import java.io.File;

class LspPageNameGenerator {

	public String generatePageName(final File inputFile) {
		final String fileName = inputFile.getName();
		final String realPageName = StringUtils.replaceChars(fileName, "/.-", "___");
		final String suffix = String.valueOf(inputFile.lastModified());
		return realPageName + suffix;
	}

}
