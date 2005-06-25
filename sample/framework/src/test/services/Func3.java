package test.services;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import nu.staldal.lsp.framework.*;


public class Func3 implements Service
{
    
    public void init()
        throws ServletException
    {
        // nothing to do
    }
         
    
    public String execute(ServletContext context, 
                HttpServletRequest request, HttpServletResponse response,
                Map pageParams, boolean isPost)
        throws ServletException, IOException
    {
        String flag = request.getParameter("flag");
        pageParams.put("flag", (flag == null) ? "" : flag);
        return "Func3";
	}    
    
    public void destroy()
    {
        // nothing to do
    }
}

