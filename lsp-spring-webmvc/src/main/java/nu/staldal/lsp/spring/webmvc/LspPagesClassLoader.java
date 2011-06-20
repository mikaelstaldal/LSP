package nu.staldal.lsp.spring.webmvc;

import java.util.HashMap;
import java.util.Map;

class LspPagesClassLoader extends ClassLoader {

	private final Map<String, LspClass> lspClassesByClassName = new HashMap<String, LspClass>();
	private final Map<String, LspClass> lspClassesByPageName = new HashMap<String, LspClass>();

	LspPagesClassLoader(final ClassLoader parent) {
		super(parent);
	}

	public void addClass(final LspClass lspClass) {
		lspClassesByClassName.put(lspClass.getClassName(), lspClass);
		lspClassesByPageName.put(lspClass.getPageName(), lspClass);
	}

	public boolean isPageLoaded(final String pageName) {
		return lspClassesByPageName.containsKey(pageName);
	}

	public LspClass getLspClass(final String pageName) {
		return lspClassesByPageName.get(pageName);
	}

	@Override
	public Class<?> loadClass(final String name) throws ClassNotFoundException {
		return super.loadClass(name, true);
	}

	@Override
	protected Class<?> findClass(final String name) throws ClassNotFoundException {
		if (lspClassesByClassName.containsKey(name)) {
			final LspClass lspClass = lspClassesByClassName.get(name);
			return defineClass(name, lspClass.getClassBytes(), 0, lspClass.getClassBytes().length);
		} else {
			return super.findClass(name);
		}
	}

}
