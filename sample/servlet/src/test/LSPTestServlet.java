package test;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import nu.staldal.lsp.servlet.*;
  

public class LSPTestServlet extends HttpServlet
{
    private LSPManager lspManager;
    
    public void init()
        throws ServletException
    {
        lspManager = LSPManager.getInstance(getServletContext(),
            getClass().getClassLoader());
    }
         
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String function = req.getPathInfo();
        if (function == null || function.length() == 0)
        {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, 
                "No function specified");
            return;
        }
        
        if (function.equals("/FUNC1"))
        {
            req.setAttribute("str", "FOO");
            req.setAttribute("num", new Integer(4711));
            lspManager.getRequestDispatcher("Func1").forward(req, resp);
        }
        else if (function.equals("/FUNC2"))
        {
            req.setAttribute("hello", "Hi, there!");
            req.setAttribute("alist", new Object[] { "one", "two", "three" });
            lspManager.getRequestDispatcher("Func2").forward(req, resp);
        }
        else
        {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, 
                "Unknown function");    
        }
    }
    
    
    public void destroy()
    {
        // nothing to do
    }
}

