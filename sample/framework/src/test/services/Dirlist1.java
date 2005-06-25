package test.services;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import nu.staldal.lsp.framework.*;


public class Dirlist1 implements Service
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
        pageParams.put("dirlist", 
                new String[] { "file 1", "file 2", "file 3" });
        return "dirlist";
    }
    
    
    public void destroy()
    {
        // nothing to do
    }
}

