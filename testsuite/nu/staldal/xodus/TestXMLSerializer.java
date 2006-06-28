package nu.staldal.xodus;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Test XMLSerializer.
 *
 * @author Mikael Ståldal
 */
public class TestXMLSerializer
{
    public static final String ENC = "ISO-8859-1";
    
    ByteArrayOutputStream os;
    Serializer ser;
    
    @Before
    public void setUp() throws Exception
    {
        os = new ByteArrayOutputStream();
        Properties outputProperties = new Properties();
        outputProperties.setProperty(OutputKeys.METHOD, "xml");
        outputProperties.setProperty(OutputKeys.ENCODING, ENC);
        ser = Serializer.createSerializer(new StreamResult(os), outputProperties);
    }

    @After
    public void tearDown() throws Exception
    {
        ser = null;
        os = null;
    }


    @Test
    public void test1()
        throws Exception
    {
        ser.startDocument();
        ser.startElement("", "root", "", new AttributesImpl());
        ser.characters("Räksmörgås!");
        ser.characters("0123456789".toCharArray(), 5, 2);
        ser.endElement("", "root", "");
        ser.comment("Kommentar");
        ser.endDocument();
        
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<root>Räksmörgås!56</root><!-- Kommentar -->",
                os.toString(ENC));        
    }

}
