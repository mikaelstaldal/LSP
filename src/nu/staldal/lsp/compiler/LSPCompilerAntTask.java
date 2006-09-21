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

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

import nu.staldal.lsp.*;
 

/**
 * Apache Ant interface to the LSP compiler.
 *
 * @see LSPCompilerHelper
 */
public class LSPCompilerAntTask extends Task
{
	private LSPCompilerHelper compiler;
	
	private Path sourcepath;
	private File destdir;	
	private FileSet fileset;
	private File encloseFile;
	private boolean force;
	private boolean html;
    private boolean acceptNull;

	
	/**
	 * Create a new LSPCompilerAntTask.
	 */
	public LSPCompilerAntTask()
	{
		compiler = new LSPCompilerHelper();
	}


	@Override
    public void init()
    	throws BuildException
	{
		fileset = null;
		sourcepath = null;		
		destdir = null;
		encloseFile = null;
		force = false;
        html = false;
        acceptNull= false;
    }

	
	// Attribute setter methods
	
	/**
     * Force compilation even if the compiled class exists and is up-to-date.
     * 
	 * @param force
	 */
	public void setForce(boolean force)
	{
		this.force = force;
	}
	
    /**
     * Use XHTML as default output method instead of HTML.
     * 
     * @param xhtml
     *  
     * @deprecated XHTML is now default, use {@link #setHtml(boolean)} to override it.
     */
	public void setXhtml(boolean xhtml)
	{
        this.html = !xhtml;
	}

    /**
     * Use HTML as default output method instead of XHTML.
     * 
     * @param html
     */
    public void setHtml(boolean html)
    {
        this.html = html;
    }
    
	/**
     * Accept <code>null</code> as variable value without runtime exception.
     * 
	 * @param acceptNull
	 */
	public void setAcceptNull(boolean acceptNull)
	{
		this.acceptNull = acceptNull;
	}

	/**
     * Path to search for enclose and included files. 
     * 
	 * @param sourcepath
	 */
	public void setSourcepath(Path sourcepath)
	{
		this.sourcepath = sourcepath;
	}
		
	/**
     * Where to place generated class files.
     * 
	 * @param destdir
	 */
	public void setDestdir(File destdir)
	{
		this.destdir = destdir;
	}

	/**
     * Set enclose file.
     * 
	 * @param encloseFile
	 */
	public void setEnclose(File encloseFile)
	{
		this.encloseFile = encloseFile;
	}
	
	// Handle nested elements
	
	/**
     * Input files.
     * 
	 * @param fileset
	 */
	public void addConfiguredFileset(FileSet fileset)
	{
		this.fileset = fileset;	
	}
	
		
	@Override
    public void execute() throws BuildException
	{
		if (fileset == null)
			throw new BuildException("Must have a nested <fileset> element");

		if (destdir == null)
			throw new BuildException("Must have a destdir attribute");

        compiler.setHtml(html);
        compiler.setAcceptNull(acceptNull);
		
		compiler.setTargetDir(destdir);
				
		if (encloseFile != null) compiler.setEncloseFile(encloseFile);
        
		if (sourcepath != null)
        {
            String[] _sp = sourcepath.list();
            File[] sp = new File[_sp.length];
            
            for (int i = 0; i<_sp.length; i++)
            {
                sp[i] = new File(_sp[i]);   
            }
            
            compiler.setSourcePath(sp);            
        }

		DirectoryScanner ds = fileset.getDirectoryScanner(getProject());
		File fromDir = fileset.getDir(getProject());
		compiler.setStartDir(fromDir);

		String[] srcFiles = ds.getIncludedFiles();
		
		for (int i = 0; i<srcFiles.length; i++)
		{
			try {
				if (compiler.doCompile(srcFiles[i], force))
					log("Compiling " + srcFiles[i]);
			}
			catch (LSPException e)
			{
				log(e.getMessage(), Project.MSG_ERR);
				throw new BuildException("LSP compilation failed");
			}			
		}
	}

}
