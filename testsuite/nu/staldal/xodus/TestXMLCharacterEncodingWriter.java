package nu.staldal.xodus;


import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test XMLCharacterEncoder for Writer.
 *
 * @author Mikael Ståldal
 */
public class TestXMLCharacterEncodingWriter
{
    StringWriter w;
    XMLCharacterEncoder encoder;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        w = new StringWriter();
        encoder = new XMLCharacterEncoder(w);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        encoder = null;
        w = null;
    }

    
    @Test
    public void testNotEncoded()
        throws IOException
    {
        encoder.append('A');
        encoder.append("BCDEF");
        encoder.append("0123456789", 2, 5);
        encoder.write('Z');
        encoder.write("åäö");
        encoder.write("---***---", 3, 3);
        encoder.write("###".toCharArray());
        encoder.write("---***---", 3, 3);
        encoder.write("---$$$---".toCharArray(),3,3);
        encoder.finish();
        
        assertEquals("not encoded", "ABCDEF234Zåäö***###***$$$", w.toString());
    }
}
