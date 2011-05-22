package nu.staldal.lsp.framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpServletRequestMock implements HttpServletRequest
{
    private Hashtable<String,String[]> parameters = new Hashtable<String,String[]>();    
    private Hashtable<String,Object> attributes = new Hashtable<String,Object>();
    
    public void setParameter(String name, String[] value)
    {
        parameters.put(name, value);
    }    
    
    public void setParameter(String name, String value)
    {
        parameters.put(name, new String[] { value });
    }    

    private final String servletPath;
    private final String pathInfo;
    
    public HttpServletRequestMock(final String servletPath)
    {
        this(servletPath, null);
    }

    public HttpServletRequestMock(final String servletPath, final String pathInfo)
    {
        this.servletPath = servletPath;
        this.pathInfo = pathInfo;
    }
    
    public String getAuthType()
    {
        // nothing to do
        return null;
    }

    public String getContextPath()
    {
        // nothing to do
        return null;
    }

    public Cookie[] getCookies()
    {
        // nothing to do
        return null;
    }

    public long getDateHeader(String name)
    {
        // nothing to do
        return 0;
    }

    public String getHeader(String name)
    {
        // nothing to do
        return null;
    }

    public Enumeration<?> getHeaderNames()
    {
        // nothing to do
        return null;
    }

    public Enumeration<?> getHeaders(String name)
    {
        // nothing to do
        return null;
    }

    public int getIntHeader(String name)
    {
        // nothing to do
        return 0;
    }

    public String getMethod()
    {
        // nothing to do
        return null;
    }

    public String getPathInfo()
    {
        return pathInfo;
    }

    public String getPathTranslated()
    {
        // nothing to do
        return null;
    }

    public String getQueryString()
    {
        // nothing to do
        return null;
    }

    public String getRemoteUser()
    {
        // nothing to do
        return null;
    }

    public String getRequestURI()
    {
        // nothing to do
        return null;
    }

    public StringBuffer getRequestURL()
    {
        // nothing to do
        return null;
    }

    public String getRequestedSessionId()
    {
        // nothing to do
        return null;
    }

    public String getServletPath()
    {
        return servletPath;
    }

    public HttpSession getSession()
    {
        // nothing to do
        return null;
    }

    public HttpSession getSession(boolean create)
    {
        // nothing to do
        return null;
    }

    public Principal getUserPrincipal()
    {
        // nothing to do
        return null;
    }

    public boolean isRequestedSessionIdFromCookie()
    {
        // nothing to do
        return false;
    }

    public boolean isRequestedSessionIdFromURL()
    {
        // nothing to do
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean isRequestedSessionIdFromUrl()
    {
        // nothing to do
        return false;
    }

    public boolean isRequestedSessionIdValid()
    {
        // nothing to do
        return false;
    }

    public boolean isUserInRole(String role)
    {
        // nothing to do
        return false;
    }

    public Object getAttribute(String name)
    {
        return attributes.get(name);
    }

    public Enumeration<?> getAttributeNames()
    {
        return attributes.keys();
    }

    public String getCharacterEncoding()
    {
        // nothing to do
        return null;
    }

    public int getContentLength()
    {
        // nothing to do
        return 0;
    }

    public String getContentType()
    {
        // nothing to do
        return null;
    }

    public ServletInputStream getInputStream() throws IOException
    {
        // nothing to do
        return null;
    }

    public String getLocalAddr()
    {
        // nothing to do
        return null;
    }

    public String getLocalName()
    {
        // nothing to do
        return null;
    }

    public int getLocalPort()
    {
        // nothing to do
        return 0;
    }

    public Locale getLocale()
    {
        // nothing to do
        return null;
    }

    public Enumeration<?> getLocales()
    {
        // nothing to do
        return null;
    }

    public String getParameter(String name)
    {
        String[] param = parameters.get(name);
        if (param == null)
            return null;
        else if (param.length == 0)
            return null;
        else 
            return param[0];
    }

    public Map<?,?> getParameterMap()
    {
        return Collections.unmodifiableMap(parameters);
    }

    public Enumeration<?> getParameterNames()
    {
        return parameters.keys();
    }

    public String[] getParameterValues(String name)
    {
        return parameters.get(name);        
    }

    public String getProtocol()
    {
        // nothing to do
        return null;
    }

    public BufferedReader getReader() throws IOException
    {
        // nothing to do
        return null;
    }

    @SuppressWarnings("deprecation")
    public String getRealPath(String path)
    {
        // nothing to do
        return null;
    }

    public String getRemoteAddr()
    {
        // nothing to do
        return null;
    }

    public String getRemoteHost()
    {
        // nothing to do
        return null;
    }

    public int getRemotePort()
    {
        // nothing to do
        return 0;
    }

    public RequestDispatcher getRequestDispatcher(String path)
    {
        // nothing to do
        return null;
    }

    public String getScheme()
    {
        // nothing to do
        return null;
    }

    public String getServerName()
    {
        // nothing to do
        return null;
    }

    public int getServerPort()
    {
        // nothing to do
        return 0;
    }

    public boolean isSecure()
    {
        // nothing to do
        return false;
    }

    public void removeAttribute(String name)
    {
        attributes.remove(name);
    }

    public void setAttribute(String name, Object o)
    {
        attributes.put(name, o);
    }

    public void setCharacterEncoding(String env)
            throws UnsupportedEncodingException
    {
        // nothing to do

    }

}
