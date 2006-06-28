package nu.staldal.xodus;


import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test XMLCharacterEncoder.
 *
 * @author Mikael Ståldal
 */
public class TestXMLCharacterEncoderOutputStream
{
    /**
     * Junit 3 compatibility. 
     */
    public static junit.framework.Test suite() 
    { 
        return new junit.framework.JUnit4TestAdapter(TestXMLCharacterEncoderOutputStream.class); 
    }
    
    public static final String ENC = "ISO-8859-1";
    
    ByteArrayOutputStream os;
    XMLCharacterEncoder encoder;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        os = new ByteArrayOutputStream();
        encoder = new XMLCharacterEncoder(os, ENC);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        encoder = null;
        os = null;
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
        
        assertEquals("not encoded", "ABCDEF234Zåäö***###***$$$", os.toString(ENC));
    }

    @Test(expected=java.io.CharConversionException.class)
    public void testNonMappable()
        throws IOException
    {
        encoder.append('\u0102');
    }

    @Test
    public void testEncoded()
        throws IOException
    {
        encoder.enableEscaping();
        encoder.append('A');
        encoder.append("\u0102");
        encoder.append("0123456789", 2, 5);
        encoder.write('Z');
        encoder.write("åäö");
        encoder.write("---***---", 3, 3);
        encoder.write("###".toCharArray());
        encoder.write("---***---", 3, 3);
        encoder.write("---$$$---".toCharArray(),3,3);
        encoder.finish();
        
        assertEquals("not encoded", "A&#x102;234Zåäö***###***$$$", os.toString(ENC));
    }
}
