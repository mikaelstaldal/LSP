package test.services;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import nu.staldal.lsp.framework.*;


public class Func2 implements Service
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
        pageParams.put("hello", "Hi, there!");
        pageParams.put("alist", new Object[] { "one", "two", "three", "four", "five" });
        pageParams.put("intlist", new int[] { 0, 11, 22, 33, 44 });

        List food = new ArrayList();
        Map m;
        
        m = new HashMap();
        m.put("name", "orange");
        m.put("type", "fruit");
        m.put("colour", "orange");
        food.add(m);

        m = new HashMap();
        m.put("name", "banana");
        m.put("type", "fruit");
        m.put("colour", "yellow");
        food.add(m);

        m = new HashMap();
        m.put("name", "ice cream");
        m.put("type", "desert");
        m.put("colour", "white");
        food.add(m);

        pageParams.put("food", food);

        return "Func2";
    } 
    
    
    public void destroy()
    {
        // nothing to do
    }
}

