package nu.staldal.util;

import java.util.*;

import junit.framework.*;

public class TestEnvironment extends TestCase
{
	protected static final String key1 = "key1";
	protected static final String key2 = "key2";
	protected static final String value1 = "value1";
	protected static final String value2 = "value2";

	protected Environment env;
	
    public TestEnvironment(String name)
    {
        super(name);
    }
	
	public void setUp()
	{
		env = new Environment();	
	}

    public void testBind()
    {
		assertNull(env.bind(key1, value1));
		assertEquals(value1, env.lookup(key1));

		assertEquals(value1, env.bind(key1, value2));
		assertEquals(value2, env.lookup(key1));
		
		assertEquals(value2, env.unbind(key1));
		assertNull(env.lookup(key1));
		
		assertNull(env.unbind(key1));
    }
	
	
	public void testFrame()
	{
		assertNull(env.bind(key1, value1));
		
		env.pushFrame();
		assertEquals(value1, env.lookup(key1));
		assertNull(env.bind(key1, value2));
		assertEquals(value2, env.lookup(key1));
		env.popFrame();
		assertEquals(value1, env.lookup(key1));
		
		env.pushFrame();
		assertEquals(value1, env.lookup(key1));
		assertNull(env.unbind(key1));
		assertEquals(value1, env.lookup(key1));
		env.pushFrame();
		assertNull(env.bind(key2, value2));
		assertEquals(value2, env.lookup(key2));		
		env.popFrame();
		assertNull(env.lookup(key2));		
		env.popFrame();
		assertEquals(value1, env.lookup(key1));		
		
        try {
			env.popFrame();
            fail("Environment did not throw any Exception when trying to pop last frame");
        }
        catch (EmptyStackException e)
        { }
	}
	
}
