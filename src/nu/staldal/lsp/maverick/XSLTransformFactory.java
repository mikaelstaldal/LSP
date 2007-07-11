/*
 * Copyright (c) 2005, Mikael Ståldal
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

package nu.staldal.lsp.maverick;

import javax.servlet.*;
import javax.xml.transform.*;

import org.infohazard.maverick.flow.*;
import org.infohazard.maverick.util.XML;
import org.jdom.Element;

import nu.staldal.lsp.servlet.LSPManager;


/**
 * Maverick TransformFactory for using XSLT with LSP.
 */
public class XSLTransformFactory implements TransformFactory
{
	protected ServletContext servletContext;
	protected String defaultFinalContentType = "text/html";
	protected URIResolver uriResolver = null;
    
    protected LSPManager lspManager;
    
    
	public void init(Element factoryNode, ServletConfig servletCfg) 
        throws ConfigException
	{
		this.servletContext = servletCfg.getServletContext();

        lspManager = LSPManager.getInstance(servletCfg.getServletContext());
        
		if (factoryNode != null)
		{
			String ot = XML.getValue(factoryNode,  "default-output-type");
			if (ot != null)
				this.defaultFinalContentType = ot;

			String uriResolverStr = XML.getValue(factoryNode, "uri-resolver");
			if (uriResolverStr != null)
			{
				try
				{
					Class<?> resolverClass = Class.forName(uriResolverStr, true, 
                        lspManager.getClassLoader());
					this.uriResolver = (URIResolver)resolverClass.newInstance();
				}
				catch (ClassNotFoundException e)
				{
					throw new ConfigException(e);
				}
				catch (InstantiationException e)
				{
					throw new ConfigException(e);
				}
				catch (IllegalAccessException e)
				{
					throw new ConfigException(e);
				}
			}
		}
	}
    
    
	public Transform createTransform(Element transformNode) 
        throws ConfigException
	{
		String outputType = XML.getValue(transformNode, "output-type");
		if (outputType == null)
			outputType = this.defaultFinalContentType;

		String path = XML.getValue(transformNode, "path");
		if (path == null)
			throw new ConfigException("XSLT transform node must have a \"path\" attribute:  "
										+ XML.toString(transformNode));

		return new XSLTransform(path, servletContext, 
            lspManager, outputType, uriResolver);
	}

}

