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

import java.util.*;
import java.io.IOException;

import javax.servlet.*;

import org.xml.sax.*;

import org.infohazard.maverick.flow.*; 

import nu.staldal.lsp.*;
import nu.staldal.lsp.servlet.*;
import nu.staldal.lsp.wrapper.ReadonlyBeanMap;


class LSPView implements View
{
    private final LSPManager lspManager;    
    private final LSPPage thePage;
    private final String beanName;

    
    public LSPView(String path, String beanName, LSPManager lspManager)
        throws ConfigException
    {
        this.lspManager = lspManager;
        this.beanName = beanName;
        
        if (path.charAt(0) == '/')
        {
            // remove '/'
            path = path.substring(1);
        }

        if (path.endsWith(".lsp"))
        {
            // Remove ".lsp"
            path = path.substring(0, path.length()-4);
        }
            
        this.thePage = lspManager.getPage(path);                
        if (thePage == null)
        {
            throw new ConfigException("Unable to find LSP page: " + path);
        }
    }

    
    /**
     * Interface method.
     */
	public void go(ViewContext vctx) 
        throws IOException, ServletException
    {
        Map<String,Object> lspParams = new HashMap<String,Object>();
        
        if (vctx.getViewParams() != null)
        {
            // unchecked warning due to use of legacy API
            lspParams.putAll(vctx.getViewParams());
        }

        /*
        for (Enumeration e = vctx.getRequest().getAttributeNames(); e.hasMoreElements(); )
        {                
            String name = (String)e.nextElement();
            Object value = request.getAttribute(name);
            lspParams.put(name, value);
        }
        */
        
        Object model = vctx.getModel();        
        if (model == null)
        {
            lspParams.put(beanName, FullMap.getInstance());
        }
        else
        {
            lspParams.put(beanName, new ReadonlyBeanMap(model));
        }            

        TransformStep next = vctx.getNextStep();
        
        if (next == null || next.isLast())
        {
            // no transforms

            try {		
                lspManager.executePage(thePage, 
                                       lspParams,
                                       vctx.getRequest(),
                                       vctx.getRealResponse());
            }
            catch (SAXException e)
            {
                Exception ee = e.getException();
                if (ee == null)
                    throw new ServletException(e);
                if (ee instanceof IOException)
                    throw (IOException)ee;
                else if (ee instanceof ServletException)
                    throw (ServletException)ee;
                else if (ee instanceof RuntimeException)
                    throw (RuntimeException)ee;
                else
                    throw new ServletException(ee);
            }

            vctx.getRealResponse().flushBuffer();
        }
        else
        {
            // we have transform(s)
                        
            try {
                ContentHandler sax = next.getSAXHandler();
                
                sax.startDocument();
                thePage.execute(sax, lspParams, 
                    new LSPServletContext(vctx.getServletContext(), 
                                          vctx.getRequest(), 
                                          vctx.getRealResponse(),
                                          lspManager));
                sax.endDocument();
            }
            catch (SAXException e)
            {
                Exception ee = e.getException();
                if (ee == null)
                    throw new ServletException(e);
                if (ee instanceof IOException)
                    throw (IOException)ee;
                else if (ee instanceof ServletException)
                    throw (ServletException)ee;
                else if (ee instanceof RuntimeException)
                    throw (RuntimeException)ee;
                else
                    throw new ServletException(ee);
            }
            
            next.done();
        }
    }

}

