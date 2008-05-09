package test.services;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import nu.staldal.lsp.framework.*;


public class Forward2 implements Service
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
        return "*Func1";
    }
    
    
    public void destroy()
    {
        // nothing to do
    }
}

