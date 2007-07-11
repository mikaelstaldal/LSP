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

import java.io.*;
import java.util.*;

import javax.servlet.*;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.*;

import org.infohazard.maverick.flow.*;
import org.infohazard.maverick.transform.XMLTransformStep;

import nu.staldal.lsp.servlet.LSPManager;
import nu.staldal.xodus.*;


class XSLTransform implements Transform
{
	
    protected final String path;
    protected final ServletContext servletContext;
    protected final LSPManager lspManager;
    protected final String finalContentType;
    protected final URIResolver uriResolver;
    
    
	public XSLTransform(String path,
						ServletContext servletContext,
                        LSPManager lspManager,
						String finalContentType,
						URIResolver uriResolver) 
	{
        this.path = path;
        this.servletContext = servletContext;
        this.lspManager = lspManager;
        this.finalContentType = finalContentType;
        this.uriResolver = uriResolver;
	}


	public TransformStep createStep(TransformContext tctx) 
        throws ServletException
	{
		return new Step(tctx);
	}

    
	protected class Step extends XMLTransformStep
	{
		protected boolean handlerUsed = false;

        
		public Step(TransformContext tctx) 
            throws ServletException
		{
			super(tctx);
		}


		protected void assignContentType(TransformContext tctx) 
            throws ServletException
		{
			if (this.getNext().isLast() && !tctx.halting())
				this.getNext().setContentType(finalContentType);
			else
				this.getNext().setContentType("text/xml");
		}

        
        protected Serializer getSerializer(OutputStream os, Templates t)
            throws IOException
        {
            return Serializer.createSerializer(
                new StreamResult(os), t.getOutputProperties());
        }
        

		@Override
        public ContentHandler getSAXHandler() 
            throws IOException, ServletException
		{
			this.handlerUsed = true;
			
			assignContentType(this.getTransformCtx());
			
			Templates stylesheet;
            TransformerHandler tHandler;
			try
			{
				SAXTransformerFactory saxTFact = (SAXTransformerFactory)TransformerFactory.newInstance();
                stylesheet = lspManager.getLSPHelper().getStylesheet(path); 
				tHandler = saxTFact.newTransformerHandler(stylesheet);
			}
			catch (TransformerConfigurationException ex)
			{
				throw new ServletException(ex);
			}

			if (this.getTransformCtx().getTransformParams() != null)
            {
			    // unchecked warning due to use of legacy API
                for (Iterator<Map.Entry<String,Object>> it = this.getTransformCtx().getTransformParams().entrySet().iterator(); 
                    it.hasNext(); )
                {
                    Map.Entry<String,Object> entry = it.next();
                    tHandler.getTransformer().setParameter(entry.getKey(), entry.getValue());
                }
            }                        
			
			if (this.getNext().isLast())
            {
                ServletResponse response = this.getNext().getResponse();
                response.setContentType(lspManager.getLSPHelper().getContentType(stylesheet));
                tHandler.setResult(new SAXResult(getSerializer(response.getOutputStream(), 
                    stylesheet)));
            }
			else
            {
				tHandler.setResult(new SAXResult(this.getNext().getSAXHandler()));
            }
				
			return tHandler;
		}


		@Override
        public void go(Source input) throws IOException, ServletException
		{
			assignContentType(this.getTransformCtx());
			
			Templates stylesheet;
            Transformer trans;
			try
			{
                stylesheet = lspManager.getLSPHelper().getStylesheet(path); 
				trans = stylesheet.newTransformer();
			}
			catch (TransformerConfigurationException ex)
			{
				throw new ServletException(ex);
			}
			
			if (this.getTransformCtx().getTransformParams() != null)
            {
			    // unchecked warning due to use of legacy API
                for (Iterator<Map.Entry<String,Object>> it = this.getTransformCtx().getTransformParams().entrySet().iterator(); 
                    it.hasNext(); )
                {
                    Map.Entry<String,Object> entry = it.next();
                    trans.setParameter(entry.getKey(), entry.getValue());
                }
            }
			
			Result res;
			if (this.getNext().isLast())
				res = new SAXResult(getSerializer(this.getNext().getResponse().getOutputStream(),
                            stylesheet));
			else
				res = new SAXResult(this.getNext().getSAXHandler());

			try
			{
				trans.transform(input, res);
			}
			catch (TransformerException ex)
			{
				throw new ServletException(ex);
			}

			this.getNext().done();
		}

        
		@Override
        public void done() 
            throws IOException, ServletException
		{
			if (this.fakeResponse == null)
			{
				if (this.handlerUsed)
					this.getNext().done();
				else
					throw new IllegalStateException("done() called illegally");
			}
			else
			{
				this.go(this.fakeResponse.getOutputAsReader());
				this.fakeResponse = null;
			}
		}                
        
	}

}

