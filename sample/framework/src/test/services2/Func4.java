package test.services2;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import nu.staldal.lsp.framework.*;


public class Func4 implements Service
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
        return "Func4";
    }
    
    
    public void destroy()
    {
        // nothing to do
    }
}

