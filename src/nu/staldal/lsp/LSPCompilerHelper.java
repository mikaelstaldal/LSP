/*
 * Copyright (c) 2003, Mikael Ståldal
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

package nu.staldal.lsp;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import org.xml.sax.*;
import javax.xml.parsers.*;

import nu.staldal.util.Utils;


/**
 * Wrapper around LSPCompiler to handle reading input files and
 * writing the result to a file.
 *
 * @see LSPCompiler
 */
public class LSPCompilerHelper
{
	private final SAXParserFactory spf;
	private final LSPCompiler compiler;
	
	private String currentMainPage;
	private File currentPagePath;

	/**
	 * Where to look for files to compile, default is current directory.
	 */
	public File startDir;
	
	/**
	 * Where to look for imported files with pseudo-absolute URL:s,
	 * default is current directory.
	 */
	public File sourceDir;

	/**
	 * Where to place generated files, default is current directory.
	 */
	public File targetDir;


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
		sourceDir = new File(System.getProperty("user.dir", "."));
		targetDir = new File(System.getProperty("user.dir", "."));
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
		currentMainPage = mainPage;
		File inputFile = new File(startDir, currentMainPage);
		currentPagePath = inputFile.getParentFile();
		File outputFile = new File(targetDir, targetFilename(inputFile.getName()));
		
		if (!(force 
				|| !outputFile.isFile()
				|| outputFile.lastModified() < inputFile.lastModified()))
			return false; // *** check imported files + compileDynamic
		
		try {
			ContentHandler sax = compiler.startCompile(
				new URLResolver() {
					public void resolve(String url, ContentHandler ch) 
						throws IOException, SAXException
					{
						getFileAsSAX(url, ch);	
					}
				});
			
			getFileAsSAX(inputFile.toURL().toString(), sax);
		
			LSPPage page = compiler.finishCompile();
			
			FileOutputStream fos = new FileOutputStream(outputFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(page);
			oos.close();
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


	private void getFileAsSAX(String url, ContentHandler ch)
		throws SAXException, IOException
	{
		InputSource is;
		
		if (Utils.absoluteURL(url) && url.startsWith("res:"))
		{
			InputStream in = getClass().getResourceAsStream(
				url.substring(4));
				
			if (in == null) 
				throw new FileNotFoundException(url + " (Resource not found)");
			
			is = new InputSource(in);
		}
		else if (Utils.absoluteURL(url))
		{
			is = new InputSource(url);
		}
		else if (Utils.pseudoAbsoluteURL(url))
		{
			is = new InputSource(
				new File(sourceDir, url.substring(1)).toURL().toString());
		}
		else // relative URL 	
		{			
			is = new InputSource(
				new File(currentPagePath, url).toURL().toString());
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


	/**
	 * Generates a target filename with <code>.lspc</code> extension.
	 */
	public static String targetFilename(String sourceFilename)
	{
		int dot = sourceFilename.lastIndexOf('.');
		String basename = (dot > -1) ? sourceFilename.substring(0, dot) 
									 : sourceFilename;
		return basename+".lspc";		
	}

}

