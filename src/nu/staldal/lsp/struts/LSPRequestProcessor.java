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

package nu.staldal.lsp.struts;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.xml.sax.SAXException;

import org.apache.struts.config.*;
import org.apache.struts.action.*;

import nu.staldal.lsp.LSPPage;
import nu.staldal.lsp.servlet.LSPManager;


/**
 * Struts RequestProcessor to use LSP pages in a Struts application.
 */
public class LSPRequestProcessor extends RequestProcessor
{
    private LSPManager lspManager;    
    
    
    public void init(ActionServlet servlet, ModuleConfig moduleConfig)
       throws ServletException 
    {
        super.init(servlet, moduleConfig);

        lspManager = LSPManager.getInstance(servlet.getServletContext());
    }
    
    
    protected void processForwardConfig(HttpServletRequest request,
                                        HttpServletResponse response,
                                        ForwardConfig forward)
        throws java.io.IOException, ServletException 
	{
        if (forward == null) 
        {
            return;
        }
        
        String forwardPath = forward.getPath();
        
        if (forwardPath.endsWith(".lsp") && !forward.getRedirect())
        {
            if (forwardPath.charAt(0) == '/')
                forwardPath = forwardPath.substring(1);
            
            // Remove ".lsp"
            String pageName = forwardPath.substring(0, forwardPath.length()-4);

            LSPPage thePage = lspManager.getPage(pageName);                
            if (thePage == null)
            {
                throw new ServletException("Unable to find LSP page: " + pageName);
            }
            
            Map lspParams = new HashMap();
            for (Enumeration e = request.getAttributeNames(); e.hasMoreElements(); )
            {                
                String name = (String)e.nextElement();
                Object value = request.getAttribute(name);
                lspParams.put(name, value);
            }
    
            try {		
                lspManager.executePage(thePage, 
                                       lspParams,
                                       request,
                                       response);
            }
            catch (SAXException e)
            {
                throw new ServletException(e);	
            }

            response.flushBuffer();
        }
        else
        {
            super.processForwardConfig(request, response, forward);
        }
    }
}

