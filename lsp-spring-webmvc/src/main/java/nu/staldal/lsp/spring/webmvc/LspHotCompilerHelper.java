package nu.staldal.lsp.spring.webmvc;

import nu.staldal.lsp.LSPPage;
import nu.staldal.lsp.URLResolver;
import nu.staldal.lsp.Utils;
import nu.staldal.lsp.compiler.LSPCompiler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

// Copied from LspCompilerHelper.java
public class LspHotCompilerHelper
{
	private final SAXParserFactory spf;
	private final LSPCompiler compiler;

	private File currentPagePath;

	private File[] sourcePath;
	private File targetDir;
	private File encloseFile;

    /**
	 * Create a new LSP compiler. The instance may be reused,
	 * but may <em>not</em> be used from several threads concurrently.
	 * Create several instances if multiple threads needs to compile
	 * concurrently.
	 */
	LspHotCompilerHelper()
	{
		try {
			spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			spf.setValidating(false);
			spf.setFeature("http://xml.org/sax/features/namespaces", true);
			spf.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
			spf.setFeature("http://xml.org/sax/features/validation", false);
		}
		catch (ParserConfigurationException e)
		{
			throw new Error("Unable to configure XML parser");
		}
		catch (SAXException e)
		{
			throw new Error("Unable to configure XML parser");
		}

		compiler = new LSPCompiler();

		sourcePath = new File[0];
		targetDir = new File(System.getProperty("user.dir", "."));
		encloseFile = null;
	}


	private boolean checkDepend(boolean force, File outputFile, File inputFile)
	{
		if (force)
		{
			return true;
		}

		if (!outputFile.isFile())
		{
			return true;
		}

		if (outputFile.lastModified() < inputFile.lastModified())
		{
			return true;
		}

		if (encloseFile != null
				&& (outputFile.lastModified() < encloseFile.lastModified()))
		{
			return true;
		}

		try {
			ClassLoader classLoader = new URLClassLoader(
				new URL[] { targetDir.toURI().toURL() }, getClass().getClassLoader());
			Class<?> pageClass = Class.forName(
				"_LSP_"+getPageName(inputFile.getName()),
				true,
				classLoader);
			LSPPage thePage = (LSPPage)pageClass.newInstance();
			if (thePage.isCompileDynamic())
			{
				return true;
			}

			String[] compDepFiles = thePage.getCompileDependentFiles();
			for (int i = 0; i<compDepFiles.length; i++)
			{
                File f = new File(currentPagePath, compDepFiles[i]);
                if (!f.isFile())
                {
                    for (int j = 0; j<sourcePath.length; j++)
                    {
                        f = new File(sourcePath[j], compDepFiles[i]);
                        if (f.isFile()) break;
                    }
                }

				if (!f.isFile()) return false;

				if (f.lastModified() > thePage.getTimeCompiled())
				{
					return true;
				}
			}
		}
		catch (MalformedURLException e)
		{
			return false;
		}
		catch (ClassNotFoundException e)
		{
			return true;
		}
		catch (InstantiationException e)
		{
			return true;
		}
		catch (IllegalAccessException e)
		{
			return true;
		}
		catch (VerifyError e)
		{
			return true;
		}
		catch (LinkageError e)
		{
			return true;
		}

		return false;
	}


	/**
	 * Compiles an LSP file. Performs dependency checking and compiles
	 * only if nessecary.
	 *
	 *
	 * @return <code>true</code> if page was compiled, <code>false</code>otherwise
	 *
	 * @throws nu.staldal.lsp.LSPException with an error message if unsuccessful
	 */
	public LspClass doCompile(final File inputFile, final LspPageNameGenerator pageNameGenerator) {

		final File foundEnclosingPage = findEnclosingPage(inputFile);
		if (foundEnclosingPage != null) {
			setEncloseFile(foundEnclosingPage);
		}

		currentPagePath = new File(inputFile.getParent());
		final String pageName = pageNameGenerator.generatePageName(inputFile);

		try {
			ContentHandler sax = compiler.startCompile(
					pageName,
					new URLResolver() {
						public void resolve(String url, ContentHandler ch)
								throws IOException, SAXException {
							getFileAsSAX(url, ch);
						}
					});

			getFileAsSAX(inputFile.toURI().toURL().toString(), sax);

			final ByteArrayOutputStream classBytes = new ByteArrayOutputStream();
			compiler.finishCompile(classBytes);
			return new LspClass(pageName, classBytes.toByteArray());
		} catch (SAXParseException spe) {
			Exception ee = spe.getException();
			final String sysId = (spe.getSystemId() == null)
					? pageName
					: ((spe.getSystemId().startsWith("file:"))
					? new File(URI.create(spe.getSystemId())).toString()
					: spe.getSystemId());
			final String message = sysId + ":" + spe.getLineNumber()
					+ ":" + spe.getColumnNumber() + ": " + spe.getMessage();
			throw new RuntimeException(message, spe);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private File findEnclosingPage(final File inputFile) {
		try {
			final SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

			final String[] enclosePageName = new String[1];

			try {
				parser.parse(inputFile, new DefaultHandler() {
					@Override
					public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
						if ("lsp:root".equals(qName)) {
							enclosePageName[0] = attributes.getValue("extend");
						} else {
							enclosePageName[0] = attributes.getValue("lsp:extend");
						}
						throw new SAXException(); // break parsing
					}
				});
			} catch (SAXException ignored) {
			}

			final File enclosePageFile = new File(inputFile.getParentFile(), enclosePageName[0]);
			return enclosePageFile;
		} catch (Exception e) {
			return null;
		}
	}

	void getFileAsSAX(String url, ContentHandler ch)
		throws SAXException, IOException
	{
		InputSource is;

		if (url.startsWith("res:"))
		{
			InputStream in = getClass().getResourceAsStream(
				url.substring(4));

			if (in == null)
				throw new FileNotFoundException(url + " (Resource not found)");

			is = new InputSource(in);
		}
		else if (url.length() > 3
                    && Character.isLetter(url.charAt(0))
                    && url.charAt(1) == ':'
                    && (url.charAt(2) == '/' || url.charAt(2) == '\\'))
        {   // Windows pathname
			is = new InputSource(new File(url).toURI().toURL().toString());
        }
		else if (url.length() > 2
                    && url.charAt(0) == '\\' && url.charAt(1) == '\\')
        {   // Windows UNC pathname
			is = new InputSource(new File(url).toURI().toURL().toString());
        }
        else if (Utils.absoluteURL(url))
		{
			is = new InputSource(url);
		}
		else if (url.charAt(0) == '/')
		{   // UNIX pathname
			is = new InputSource(new File(url).toURI().toURL().toString());
		}
		else // relative URL
		{
            File file = new File(currentPagePath, url);
            if (!file.isFile())
            {
                for (int i = 0; i<sourcePath.length; i++)
                {
                    file = new File(sourcePath[i], url);
                    if (file.isFile()) break;
                }
            }

            if (!file.isFile()) {
							file = new File(encloseFile.getParentFile(), url);
							if (!file.isFile()) {
								throw new FileNotFoundException(url + " (File not found)");
							}
						}

            is = new InputSource(file.toURI().toURL().toString());
		}

		try {
			XMLReader parser = spf.newSAXParser().getXMLReader();

			parser.setContentHandler(ch);
			parser.setErrorHandler(new ErrorHandler() {
				public void fatalError(SAXParseException e)
					throws SAXParseException
				{
					throw e;
				}

				public void error(SAXParseException e)
					throws SAXParseException
				{
					throw e;
				}

				public void warning(SAXParseException e)
				{
					// do nothing
				}
			});

			parser.parse(is);
		}
		catch (ParserConfigurationException e)
		{
			throw new SAXException(e);
		}
	}


	/**
	 * Get pageName.
     *
	 * @param sourceFilename the source filename
     *
	 * @return the page name
	 */
	public static String getPageName(String sourceFilename)
	{
		int dot = sourceFilename.lastIndexOf('.');
		String basename = (dot > -1) ? sourceFilename.substring(0, dot)
									 : sourceFilename;
		return basename;
	}

	/**
	 * Where to look for files to compile, default is current directory.
     *
	 * @param startDir the start directory
	 */
	public void setStartDir(File startDir)
	{
	}

	/**
	 * Where to look for imported files with relative URL:s
     * (will search the directory where the source file is as well).
     *
	 * @param sourcePath the source path
	 */
	public void setSourcePath(File[] sourcePath)
	{
		this.sourcePath = sourcePath;
	}

	/**
	 * Where to place generated files, default is current directory.
     *
     * @param targetDir the target directory
	 */
	public void setTargetDir(File targetDir)
	{
		this.targetDir = targetDir;
	}

	/**
	 * Enclose to use. Set to <code>null</code> to not use any enclose.
     *
	 * @param encloseFile the enclose file
	 */
	public void setEncloseFile(File encloseFile)
	{
		this.encloseFile = encloseFile;
		if (encloseFile != null)
		{
			try {
				compiler.setEnclose(encloseFile.toURI().toURL().toString());
			}
			catch (MalformedURLException e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			compiler.setEnclose(null);
		}
	}

    /**
     * Set to <code>true</code> to use <code>xhtml</code> as default output
     * type.
     *
     * @param xhtml
     *
     * @deprecated XHTML is now default, use {@link #setHtml(boolean)} to override it.
     */
    @Deprecated
    public void setXhtml(boolean xhtml)
    {
        compiler.setXhtml(xhtml);
    }

    /**
     * Set to <code>true</code> to use <code>html</code> as default output
     * type.
     *
     * @param html
     */
    public void setHtml(boolean html)
    {
        compiler.setHtml(html);
    }

    /**
     * @param acceptNull
     *
     * @deprecated use {@link #setAcceptUnbound(boolean)} instead
     */
    @Deprecated
    public void setAcceptNull(boolean acceptNull)
    {
        setAcceptUnbound(acceptNull);
    }


    /**
     * Set to <code>true</code> to make the compiled page accept
     * unbound values without runtime error.
     *
     * @param acceptUnbound
     */
    public void setAcceptUnbound(boolean acceptUnbound)
    {
        compiler.setAcceptUnbound(acceptUnbound);
    }
}
