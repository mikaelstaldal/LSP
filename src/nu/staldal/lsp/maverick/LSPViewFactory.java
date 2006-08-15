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

import org.infohazard.maverick.flow.*; 
import org.infohazard.maverick.util.*; 
import org.infohazard.maverick.view.DocumentViewFactory; 

import nu.staldal.lsp.servlet.LSPManager;


/**
 * Maverick ViewFactory to use LSP pages in a Maverick application.
 */
public class LSPViewFactory extends DocumentViewFactory
{
    protected LSPManager lspManager;    


    @Override
    public void init(org.jdom.Element factoryNode, ServletConfig servletCfg)
        throws ConfigException    
    {
        super.init(factoryNode, servletCfg);
        
        lspManager = LSPManager.getInstance(servletCfg.getServletContext());
    }
    
    
    @Override
    public View createView(org.jdom.Element viewNode)
        throws ConfigException
    {
		String path = XML.getValue(viewNode, "path");

		if (path == null || path.length()==0)
			throw new ConfigException("View node must have a path:  " + XML.toString(viewNode));

        String beanName = XML.getValue(viewNode, ATTR_BEAN_NAME);
        if (beanName == null || beanName.length()==0)
            beanName = this.defaultBeanName;
        
		return new LSPView(path, beanName, lspManager);                
    }
    
}

