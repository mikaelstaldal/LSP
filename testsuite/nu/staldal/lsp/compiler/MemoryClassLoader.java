package nu.staldal.lsp.compiler;

import java.util.HashMap;
import java.util.Map;

public class MemoryClassLoader extends ClassLoader
{
	private Map<String,byte[]> classes;
	
	public MemoryClassLoader()
	{
		classes = new HashMap<String,byte[]>();
	}
	
	public void addClass(String name, byte[] byteCode)
	{
		classes.put(name, byteCode);
	}
	
    @Override
	public Class<?> findClass(String name)
    	throws ClassNotFoundException
    {
        byte[] b = classes.get(name);
        if (b == null)
        	throw new ClassNotFoundException(name);
        
        return defineClass(name, b, 0, b.length);
    }	
}
