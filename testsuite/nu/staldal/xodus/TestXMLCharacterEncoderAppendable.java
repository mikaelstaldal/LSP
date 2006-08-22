package nu.staldal.xodus;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test XMLCharacterEncoder with Appendable.
 *
 * @author Mikael Ståldal
 */
public class TestXMLCharacterEncoderAppendable
{
    StringBuilder sb;
    XMLCharacterEncoder encoder;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        sb = new StringBuilder();
        encoder = new XMLCharacterEncoder(sb, true);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        encoder = null;
        sb = null;
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
        
        assertEquals("not encoded", "ABCDEF234Zåäö***###***$$$", sb.toString());
    }
}
