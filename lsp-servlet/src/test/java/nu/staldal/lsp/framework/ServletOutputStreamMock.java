package nu.staldal.lsp.framework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;

public class ServletOutputStreamMock extends ServletOutputStream
{
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    @Override
    public void write(int b) throws IOException
    {
        baos.write(b);
    }
    
    public byte[] toByteArray()
    {
        return baos.toByteArray();
    }
    
    public String toString(String charset)
    {
        try
        {
            return baos.toString(charset);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }

}
