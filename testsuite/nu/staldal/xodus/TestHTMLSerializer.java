package nu.staldal.xodus;


import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class TestHTMLSerializer
{
	private Serializer ser;
	private ByteArrayOutputStream baos; 
	
	@Before
	public void setUp() throws Exception
	{
		Properties outputProperties = new Properties();
		outputProperties.setProperty(OutputKeys.METHOD, "html");
		outputProperties.setProperty(OutputKeys.ENCODING, "UTF-8");
		
		baos = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(baos); 
		
		ser = Serializer.createSerializer(result, outputProperties);
	}

	@After
	public void tearDown() throws Exception
	{
		ser = null;
		baos = null;
	}

	@Test
	public void testEscape() throws Exception
	{
		Attributes atts = new AttributesImpl();
		
		ser.startDocument();
		
		ser.startElement("", "html", "", atts);
		
		ser.startElement("", "head", "", atts);
		ser.startElement("", "style", "", atts);
		ser.characters("foo & bar");
		ser.endElement("", "style", "");
		ser.endElement("", "head", "");
		
		ser.startElement("", "body", "", atts);
		ser.characters("foo & bar");
		ser.startElement("", "script", "", atts);
		ser.characters("foo & bar");
		ser.endElement("", "script", "");
		ser.characters("foo & bar");
		ser.endElement("", "body", "");
		
		ser.endElement("", "html", "");
		
		ser.endDocument();

		assertEquals(
				
			"<html><head>"
		  + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
		  + "<style>foo & bar</style></head>"
		  + "<body>foo &amp; bar<script>foo & bar</script>foo &amp; bar</body>"
		  + "</html>",

		  baos.toString("UTF-8"));
	}
}
