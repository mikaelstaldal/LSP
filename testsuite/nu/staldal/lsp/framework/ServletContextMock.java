package nu.staldal.lsp.framework;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ServletContextMock implements ServletContext, ServletConfig
{
    private Hashtable<String,Object> attrs = new Hashtable<String,Object>(); 
    private Hashtable<String,String> initParams = new Hashtable<String,String>(); 
    
    
    public void setInitParameter(String name, String value)
    {
        initParams.put(name, value);
    }
    
    public Object getAttribute(String name)
    {
        return attrs.get(name);
    }

    public Enumeration<?> getAttributeNames()
    {
        return attrs.keys();
    }

    public ServletContext getContext(String uripath)
    {
        // nothing to do
        return null;
    }

    public String getInitParameter(String name)
    {
        return initParams.get(name);
    }

    public Enumeration<?> getInitParameterNames()
    {
        return initParams.keys();
    }

    public int getMajorVersion()
    {
        // nothing to do
        return 0;
    }

    public String getMimeType(String file)
    {
        // nothing to do
        return null;
    }

    public int getMinorVersion()
    {
        // nothing to do
        return 0;
    }

    public RequestDispatcher getNamedDispatcher(String name)
    {
        // nothing to do
        return null;
    }

    public String getRealPath(String path)
    {
        // nothing to do
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String path)
    {
        // nothing to do
        return null;
    }

    public URL getResource(String path) throws MalformedURLException
    {
        // nothing to do
        return null;
    }

    public InputStream getResourceAsStream(String path)
    {
        // nothing to do
        return null;
    }

    public Set<?> getResourcePaths(String path)
    {
        // nothing to do
        return null;
    }

    public String getServerInfo()
    {
        // nothing to do
        return null;
    }

    @SuppressWarnings("deprecation")
    public Servlet getServlet(String name) throws ServletException
    {
        // nothing to do
        return null;
    }

    public String getServletContextName()
    {
        // nothing to do
        return null;
    }

    @SuppressWarnings("deprecation")
    public Enumeration<?> getServletNames()
    {
        // nothing to do
        return null;
    }

    @SuppressWarnings("deprecation")
    public Enumeration<?> getServlets()
    {
        // nothing to do
        return null;
    }

    public void log(String msg)
    {
        System.err.println("ServletContext.log(): " + msg);
    }

    @SuppressWarnings("deprecation")
    public void log(Exception exception, String msg)
    {
        System.err.println("ServletContext.log(): " + msg);
        exception.printStackTrace();
    }

    public void log(String message, Throwable throwable)
    {
        System.err.println("ServletContext.log(): " + message);
        throwable.printStackTrace();
    }

    public void removeAttribute(String name)
    {
        attrs.remove(name);
    }

    public void setAttribute(String name, Object object)
    {
        attrs.put(name, object);
    }

    public ServletContext getServletContext()
    {
        return this;
    }

    public String getServletName()
    {
        return "MockServlet";
    }

    public String getContextPath()
    {
        return "/mockservlet";
    }
}
