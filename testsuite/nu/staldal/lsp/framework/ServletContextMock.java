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

    public Enumeration getAttributeNames()
    {
        return attrs.keys();
    }

    public ServletContext getContext(String uripath)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getInitParameter(String name)
    {
        return initParams.get(name);
    }

    public Enumeration getInitParameterNames()
    {
        return initParams.keys();
    }

    public int getMajorVersion()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getMimeType(String file)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getMinorVersion()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public RequestDispatcher getNamedDispatcher(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRealPath(String path)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String path)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public URL getResource(String path) throws MalformedURLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public InputStream getResourceAsStream(String path)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Set getResourcePaths(String path)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getServerInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Servlet getServlet(String name) throws ServletException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getServletContextName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getServletNames()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getServlets()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void log(String msg)
    {
        System.err.println("ServletContext.log(): " + msg);
    }

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

}
