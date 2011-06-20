package nu.staldal.lsp.spring.webmvc;

public class LspClass {

	private final String pageName;
	private final String className;
	private final byte[] classBytes;

	public LspClass(final String pageName, final byte[] classBytes) {
		this.pageName = pageName;
		this.className = "_LSP_" + pageName;
		this.classBytes = classBytes;
	}

	public String getPageName() {
		return pageName;
	}

	public String getClassName() {
		return className;
	}

	public byte[] getClassBytes() {
		return classBytes;
	}

}
