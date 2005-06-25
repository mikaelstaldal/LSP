package test.services;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import nu.staldal.lsp.framework.*;


public class Func1 implements Service
{
    
    public void init()
        throws ServletException
    {
        // nothing to do
    }
         
    
    public String execute(ServletContext context, 
                HttpServletRequest request, HttpServletResponse response,
                Map pageParams, int requestType)
        throws ServletException, IOException
    {
        if (request.getAttribute("forward") != null)
            pageParams.put("str", "FOO forwarded");
        else
            pageParams.put("str", "FOO");
        
        pageParams.put("num", new Integer(4711));
        return "Func1";
    }
    
    
    public void destroy()
    {
        // nothing to do
    }
}

