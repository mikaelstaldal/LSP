package test.services2;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import nu.staldal.lsp.servlet.*;
import nu.staldal.lsp.framework.*;


public class Setlocale implements Service
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
        HttpSession sess = request.getSession();
        sess.setAttribute(LSPManager.LOCALE_KEY, Locale.ENGLISH);
        
        return null;
    }

    
    public void destroy()
    {
        // nothing to do
    }
}

