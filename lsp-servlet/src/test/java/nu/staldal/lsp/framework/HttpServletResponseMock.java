package nu.staldal.lsp.framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseMock implements HttpServletResponse
{
    private final ServletOutputStreamMock outputStream = new ServletOutputStreamMock();
    
    private int sc = SC_OK;
    private String msg = null;

    public byte[] toByteArray()
    {
        return outputStream.toByteArray();
    }
    
    public String toString(String charset)
    {
        return outputStream.toString(charset);
    }
    
    public void addCookie(Cookie cookie)
    {
        // nothing to do

    }

    public void addDateHeader(String name, long date)
    {
        // nothing to do

    }

    public void addHeader(String name, String value)
    {
        // nothing to do

    }

    public void addIntHeader(String name, int value)
    {
        // nothing to do

    }

    public boolean containsHeader(String name)
    {
        // nothing to do
        return false;
    }

    public String encodeRedirectURL(String url)
    {
        // nothing to do
        return null;
    }

    @SuppressWarnings("deprecation")
    public String encodeRedirectUrl(String url)
    {
        // nothing to do
        return null;
    }

    public String encodeURL(String url)
    {
        // nothing to do
        return null;
    }

    @SuppressWarnings("deprecation")
    public String encodeUrl(String url)
    {
        // nothing to do
        return null;
    }

    public void sendError(int sc) throws IOException
    {
        sendError(sc, "");
    }

    public void sendError(int sc, String msg) throws IOException
    {
        this.sc = sc;
        this.msg = msg;
    }

    public void sendRedirect(String location) throws IOException
    {
        // nothing to do

    }

    public void setDateHeader(String name, long date)
    {
        // nothing to do

    }

    public void setHeader(String name, String value)
    {
        // nothing to do

    }

    public void setIntHeader(String name, int value)
    {
        // nothing to do

    }

    public void setStatus(int sc)
    {
        // nothing to do

    }

    @SuppressWarnings("deprecation")
    public void setStatus(int sc, String sm)
    {
        // nothing to do

    }

    public void flushBuffer() throws IOException
    {
        // nothing to do

    }

    public int getBufferSize()
    {
        // nothing to do
        return 0;
    }

    public String getCharacterEncoding()
    {
        // nothing to do
        return null;
    }

    public String getContentType()
    {
        // nothing to do
        return null;
    }

    public Locale getLocale()
    {
        // nothing to do
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException
    {
        return outputStream;
    }

    public PrintWriter getWriter() throws IOException
    {
        // nothing to do
        return null;
    }

    public boolean isCommitted()
    {
        // nothing to do
        return false;
    }

    public void reset()
    {
        // nothing to do

    }

    public void resetBuffer()
    {
        // nothing to do

    }

    public void setBufferSize(int size)
    {
        // nothing to do

    }

    public void setCharacterEncoding(String charset)
    {
        // nothing to do

    }

    public void setContentLength(int len)
    {
        // nothing to do

    }

    public void setContentType(String type)
    {
        // nothing to do

    }

    public void setLocale(Locale loc)
    {
        // nothing to do

    }

    public String getMsg()
    {
        return msg;
    }

    public int getSc()
    {
        return sc;
    }
}
