/*
 * Copyright (c) 2003-2006, Mikael Ståldal
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the author nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * Note: This is known as "the modified BSD license". It's an approved
 * Open Source and Free Software license, see
 * http://www.opensource.org/licenses/
 * and
 * http://www.gnu.org/philosophy/license-list.html
 */

package nu.staldal.lsp.compiler;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;

import org.xml.sax.*;
import javax.xml.parsers.*;

import nu.staldal.lsp.*;


/**
 * Wrapper around LSPCompiler to handle reading input files and
 * writing the result to a file.
 *<p>
 * Instances of this class may be reused, 
 * but may <em>not</em> be used from several threads concurrently.
 * Create several instances if multiple threads needs to compile 
 * concurrently. 
 *
 * @see LSPCompiler
 */
public class LSPCompilerHelper
{
	private final SAXParserFactory spf;
	private final LSPCompiler compiler;
	
	private File currentPagePath;

	private File startDir;
	private File[] sourcePath;
	private File targetDir;
		
	
    /**
	 * Create a new LSP compiler. The instance may be reused, 
	 * but may <em>not</em> be used from several threads concurrently.
	 * Create several instances if multiple threads needs to compile 
	 * concurrently.
	 */
	public LSPCompilerHelper()
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
				
		startDir = new File(System.getProperty("user.dir", "."));
		sourcePath = new File[0];
		targetDir = new File(System.getProperty("user.dir", "."));
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

		try {
			ClassLoader classLoader = new URLClassLoader(
				new URL[] { targetDir.toURL() }, getClass().getClassLoader());
			Class pageClass = Class.forName(
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
	 * @param mainPage  the main LSP file to compile
	 * @param force     force compilation, override dependency checking.
	 *
	 * @return <code>true</code> if page was compiled, <code>false</code>otherwise
	 *
	 * @throws LSPException with an error message if unsuccessful
	 */
	public boolean doCompile(String mainPage, boolean force)
		throws LSPException
	{
		String currentMainPage = mainPage;
		File inputFile = new File(startDir, currentMainPage);
		currentPagePath = inputFile.getParentFile();
		File outputFile = new File(targetDir, "_LSP_"+getPageName(inputFile.getName())+".class");
		
		if (!checkDepend(force, outputFile, inputFile))
			return false;
		
		try {
			ContentHandler sax = compiler.startCompile(
				getPageName(inputFile.getName()),
				new URLResolver() {
					public void resolve(String url, ContentHandler ch) 
						throws IOException, SAXException
					{
						getFileAsSAX(url, ch);	
					}
				});

            getFileAsSAX(inputFile.toURL().toString(), sax);            
					
			FileOutputStream fos = new FileOutputStream(outputFile);
			try {
				compiler.finishCompile(fos);
			}
			catch (Exception e)
			{
				fos.close();
				outputFile.delete();				
				if (e instanceof SAXException)
					throw (SAXException)e;
				else if (e instanceof IOException)
					throw (IOException)e;
				else
					throw (RuntimeException)e;
			}
			fos.close();
			return true;
		}
		catch (SAXParseException spe)
		{
			Exception ee = spe.getException();
			if (ee instanceof RuntimeException)
			{
				throw (RuntimeException)ee;
			}
			try {
				String sysId = (spe.getSystemId() == null)
					? currentMainPage 
					: ((spe.getSystemId().startsWith("file:")) 
						? new File(new URI(spe.getSystemId())).toString()
						: spe.getSystemId());

				throw new LSPException(sysId + ":" + spe.getLineNumber()
					+ ":" + spe.getColumnNumber() + ": " + spe.getMessage());
			}
			catch (URISyntaxException ex)
			{
				throw new LSPException("Error building " + currentMainPage + ":" 
					+ ex.toString());				
			}
		}
		catch (SAXException se)
		{
			Exception ee = se.getException();
			if (ee instanceof RuntimeException)
			{
				throw (RuntimeException)ee;
			}
			else
			{
				throw new LSPException("Error building " + currentMainPage
					+ ": " + se.getMessage());    			
			}
		}
		catch (IOException e)
		{
			throw new LSPException("Error building " + currentMainPage
					+ ": " + e.toString());
		}
	}


	/**
	 * Compiles LSP from a String to a byte[]. 
	 * No dependency checking, compiles always.
	 *
	 * @param pageName  name of the LSP page
	 * @param lspData   the LSP data
	 *
	 * @throws LSPException with an error message if unsuccessful
	 */
	byte[] doCompileFromString(String pageName, String lspData)
		throws LSPException
	{
		try {
			ContentHandler sax = compiler.startCompile(
				pageName,
				new URLResolver() {
					public void resolve(String url, ContentHandler ch) 
						throws IOException, SAXException
					{
						getFileAsSAX(url, ch);	
					}
				});
			
			getStringAsSAX(lspData, sax, pageName);
					
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				compiler.finishCompile(baos);
			}
			catch (Exception e)
			{
				if (e instanceof SAXException)
					throw (SAXException)e;
				else if (e instanceof IOException)
					throw (IOException)e;
				else
					throw (RuntimeException)e;
			}
			return baos.toByteArray();
		}
		catch (SAXParseException spe)
		{
			Exception ee = spe.getException();
			if (ee instanceof RuntimeException)
			{
				throw (RuntimeException)ee;
			}
			try {
				String sysId = (spe.getSystemId() == null)
					? pageName 
					: ((spe.getSystemId().startsWith("file:")) 
						? new File(new URI(spe.getSystemId())).toString()
						: spe.getSystemId());

				throw new LSPException(sysId + ":" + spe.getLineNumber()
					+ ":" + spe.getColumnNumber() + ": " + spe.getMessage());
			}
			catch (URISyntaxException ex)
			{
				throw new LSPException("Error building " + pageName + ":" 
					+ ex.toString());				
			}
		}
		catch (SAXException se)
		{
			Exception ee = se.getException();
			if (ee instanceof RuntimeException)
			{
				throw (RuntimeException)ee;
			}
			else
			{
				throw new LSPException("Error building " + pageName
					+ ": " + se.getMessage());    			
			}
		}
		catch (IOException e)
		{
			throw new LSPException("Error building " + pageName
					+ ": " + e.toString());
		}
	}

	
	private void getFileAsSAX(String url, ContentHandler ch)
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
			is = new InputSource(new File(url).toURL().toString());
        }
		else if (url.length() > 2 
                    && url.charAt(0) == '\\' && url.charAt(1) == '\\')
        {   // Windows UNC pathname
			is = new InputSource(new File(url).toURL().toString());
        }
        else if (Utils.absoluteURL(url))
		{
			is = new InputSource(url);
		}
		else if (url.charAt(0) == '/')            
		{   // UNIX pathname
			is = new InputSource(new File(url).toURL().toString());
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
            
            if (!file.isFile())
                throw new FileNotFoundException(url + " (File not found)");
            
            is = new InputSource(file.toURL().toString());
		}

		try {
			XMLReader parser = spf.newSAXParser().getXMLReader(); 

			parser.setContentHandler(ch);

			parser.parse(is);
		}
		catch (ParserConfigurationException e)
		{
			throw new SAXException(e);
		}		
	}


	private void getStringAsSAX(String data, ContentHandler ch, String pageName)
		throws SAXException, IOException
	{
		InputSource is = new InputSource(new StringReader(data));
		is.setSystemId(pageName+".lsp");
	
		try {
			XMLReader parser = spf.newSAXParser().getXMLReader(); 
	
			parser.setContentHandler(ch);
	
			parser.parse(is);
		}
		catch (ParserConfigurationException e)
		{
			throw new SAXException(e);
		}		
	}	

	/**
	 * Get pageName.
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
	 */
	public void setStartDir(File startDir) 
	{
		this.startDir = startDir;
	}

	/**
	 * Where to look for imported files with relative URL:s
     * (will search the directory where the source file is as well).
	 */
	public void setSourcePath(File[] sourcePath) 
	{
		this.sourcePath = sourcePath;
	}

	/**
	 * Where to place generated files, default is current directory.
	 */
	public void setTargetDir(File targetDir) 
	{
		this.targetDir = targetDir;
	}

    /**
     * Set to <code>true</code> to use <code>xhtml</code> as default output 
     * type.
     */
    public void setXhtml(boolean xhtml)
    {
        compiler.setXhtml(xhtml);    
    }
        
    /**
	 * Set to <code>true</code> to make the compiled page accept 
     * <code>null</code> values without runtime error.
     */
    public void setAcceptNull(boolean acceptNull)
    {
        compiler.setAcceptNull(acceptNull);    
    }
}
