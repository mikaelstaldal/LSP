package test.services;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import nu.staldal.lsp.framework.*;


public class Include implements Service
{
    
    public void init(ServletContext context)
        throws ServletException
    {
        // nothing to do
    }
         
    
    public String execute(
                HttpServletRequest request, HttpServletResponse response,
                Map pageParams, int requestType)
        throws ServletException, IOException
    {
        pageParams.put("foo", request.getAttribute(INCLUDE_ATTR_PREFIX+"myattr"));        
        
        return "Include";
    }
    
    
    public void destroy()
    {
        // nothing to do
    }
}

