/*
 * Copyright (c) 2003, Mikael St�ldal
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

package nu.staldal.lsp.servlet;

import java.util.*;

import org.xml.sax.SAXException;

import javax.servlet.*;

import nu.staldal.lsp.*;


/**
 * A Servlet {@link javax.servlet.RequestDispatcher} 
 * which executes an LSP page.
 */
final class LSPRequestDispatcher implements RequestDispatcher
{
	private final LSPManager manager;
	private final LSPPage thePage;
	private final String type;
	private final String contentType;
	private final String doctypePublic;
	private final String doctypeSystem;
	
    
	LSPRequestDispatcher(LSPManager manager, LSPPage thePage, 
						 String type, String contentType,
						 String doctypePublic, String doctypeSystem)
    {
		this.manager = manager;
		this.thePage = thePage;
		this.type = type;
		this.contentType = (contentType == null) 
			? manager.defaultContentType(type)
			: contentType;
		this.doctypePublic = doctypePublic;
		this.doctypeSystem = doctypeSystem;
    }
    
    
    public void forward(ServletRequest request, ServletResponse response)
        throws ServletException, java.io.IOException
    {
		response.setContentType(contentType);		
        response.resetBuffer();		
		doLSP(request, response);		
		response.flushBuffer();
    }
                    
    
    public void include(ServletRequest request, ServletResponse response)
        throws ServletException, java.io.IOException
    {
		doLSP(request, response);
    }

	
	private void doLSP(ServletRequest request, ServletResponse response)
        throws ServletException, java.io.IOException
	{
		Map lspParams = new HashMap();
		for (Enumeration e = request.getAttributeNames(); e.hasMoreElements(); )
		{
			String name = (String)e.nextElement();
			lspParams.put(name, request.getAttribute(name));
		}

		try {		
			manager.executePage(thePage, 
								lspParams, 
								response,
								type,
								doctypePublic,
								doctypeSystem);
		}
		catch (SAXException e)
		{
			throw new ServletException(e);	
		}
	}
	
}

