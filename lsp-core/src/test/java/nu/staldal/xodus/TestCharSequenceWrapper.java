package nu.staldal.xodus;

import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * 
 *
 * @author Mikael Ståldal
 */
public class TestCharSequenceWrapper
{

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        // nothing to do
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        // nothing to do
    }
    
    public void testCharArray(char[] charArray)
    {
        CharSequence cs = CharSequenceWrapper.valueOf(charArray);
        assertEquals(charArray.length, cs.length());
        StringWriter w =  new StringWriter();
        w.append(cs);
        assertEquals(String.valueOf(charArray), w.toString());
        assertEquals(String.valueOf(charArray), cs.toString());
    }

    @Test
    public void testCharArray0()
    {
        testCharArray(new char[] { });
    }
    
    @Test
    public void testCharArray1()
    {
        testCharArray(new char[] { 'a' });
    }

    @Test
    public void testCharArray3()
    {
        testCharArray(new char[] { 'a','b','c' });
    }

    @Test
    public void testCharArray10()
    {
        char[] charArray = new char[] { '0','1','2','3','4','5','6','7','8','9' };
        testCharArray(charArray);
        
        CharSequence cs = CharSequenceWrapper.valueOf(charArray, 2, 7);
        assertEquals("23456", cs.toString());
        assertEquals("23456", cs.subSequence(0, 5).toString());
        assertEquals("345", cs.subSequence(1, 4).toString());
        assertEquals("34", cs.subSequence(1, 3).toString());
        assertEquals("3", cs.subSequence(1, 2).toString());
        assertEquals("", cs.subSequence(1, 1).toString());
        try {
            cs.subSequence(0, 6);
            fail("Should throw StringIndexOutOfBoundsException");
        }
        catch (StringIndexOutOfBoundsException e) {
            // expected
        }
        
    }
    
    @Test(expected=java.lang.NullPointerException.class)
    public void testCharArrayNull()
    {
        CharSequenceWrapper.valueOf(null);
    }

    @Test(expected=java.lang.StringIndexOutOfBoundsException.class)
    public void testCharArrayNegativeStart()
    {
        CharSequenceWrapper.valueOf(new char[] { 'a', 'b', 'c' }, -1, 5);
    }

    @Test(expected=java.lang.StringIndexOutOfBoundsException.class)
    public void testCharArraySmallEnd()
    {
        CharSequenceWrapper.valueOf(new char[] { 'a', 'b', 'c' }, 2, 1);
    }

    @Test(expected=java.lang.StringIndexOutOfBoundsException.class)
    public void testCharArrayLargeEnd()
    {
        CharSequenceWrapper.valueOf(new char[] { 'a', 'b', 'c' }, 1, 4);
    }
}
