/*
 * Copyright (c) 2003, Mikael Ståldal
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the author nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * Note: This is known as "the modified BSD license". It's an approved
 * Open Source and Free Software license, see
 * http://www.opensource.org/licenses/
 * and
 * http://www.gnu.org/philosophy/license-list.html
 */

package nu.staldal.lsp;

import java.io.*;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;

import nu.staldal.xtree.*;
import nu.staldal.util.*;

import nu.staldal.lsp.wrapper.*;
import nu.staldal.lsp.expr.*;
import nu.staldal.lsp.compile.*;
import nu.staldal.lsp.compiledexpr.*;


public abstract class LSPPageBase implements LSPPage
{
	protected final String[] extLibsURLs;
	protected final String[] extLibsClassNames;
	protected final String[] compileDependentFiles;
	protected final String[] executeDependentFiles;
	protected final boolean compileDynamic;
	protected final boolean executeDynamic;
	protected final long timeCompiled;
	protected final String pageName;

	
	protected LSPPageBase(String[] extLibsURLs, String[] extLibsClassNames,
		String[] compileDependentFiles, String[] executeDependentFiles,
		boolean compileDynamic, boolean executeDynamic, long timeCompiled,
		String pageName)
	{
		this.extLibsURLs = extLibsURLs;
		this.extLibsClassNames = extLibsClassNames;
		this.compileDependentFiles = compileDependentFiles;
		this.executeDependentFiles = executeDependentFiles;
		this.compileDynamic = compileDynamic;
		this.executeDynamic = executeDynamic;
		this.timeCompiled = timeCompiled;
		this.pageName = pageName;
	}

    public final String[] getCompileDependentFiles()
	{
		return compileDependentFiles;	
	}

    public final String[] getExecuteDependentFiles()
	{
		return executeDependentFiles;	
	}

    public final boolean isCompileDynamic()
	{
		return compileDynamic;	
	}

    public final boolean isExecuteDynamic()
	{
		return executeDynamic;	
	}

    public final long getTimeCompiled()
	{	
		return timeCompiled;
	}	
	
    public final String getPageName()
	{	
		return pageName;
	}
	
    public final void execute(ContentHandler sax, URLResolver resolver,
        	Map params, Object extContext)
        throws SAXException
	{
        Environment env = new Environment();
		for (Iterator it = params.entrySet().iterator(); it.hasNext(); )
		{
			Map.Entry ent = (Map.Entry)it.next();
			String key = (String)ent.getKey();
			Object value = ent.getValue();						
			env.bind(key, convertObjectToLSP(value, key));
		}
		params = null;

		Map extLibs = new HashMap();
		
		for (int i = 0; i < extLibsURLs.length; i++)
		{
			String nsURI = extLibsURLs[i];
			String className = extLibsClassNames[i];
			
			LSPExtLib extLib = lookupExtensionHandler(extLibs, nsURI, className);			
			
			extLib.startPage(resolver, extContext, pageName);
		}

		try {
			_execute(sax, resolver, env, extLibs, sax, new AttributesImpl());
		}
		catch (IllegalArgumentException e)
		{
			throw new SAXException(e);	
		}
		
		for (int i = 0; i < extLibsURLs.length; i++)
		{
			String nsURI = extLibsURLs[i];
			String className = extLibsClassNames[i];
			
			LSPExtLib extLib = (LSPExtLib)extLibs.get(className);			
			
			extLib.endPage();
		}
	}


	protected static final LSPExtLib lookupExtensionHandler(Map extLibs, String nsURI, String className)
		throws SAXException
	{
		try {
			LSPExtLib extLib = (LSPExtLib)extLibs.get(className);
			if (extLib == null)
			{
				Class extClass = Class.forName(className);
				extLib = (LSPExtLib)extClass.newInstance();
				extLib.init(nsURI);
				extLibs.put(className, extLib);
			}
			return extLib;
		}
		catch (ClassNotFoundException e)
		{
			throw new LSPException("Extension class not found: " 
				+ className);
		}
		catch (InstantiationException e)
		{
			throw new LSPException("Unable to instantiate extension class: " 
				+ e.getMessage());
		}
		catch (IllegalAccessException e)
		{
			throw new LSPException("Unable to instantiate extension class: " 
				+ e.getMessage());
		}
		catch (ClassCastException e)
		{
			throw new LSPException("Extension class " + className 
				+ " does not implement the required interface");
		}
	}


	protected static Object convertObjectToLSP(Object value, String name)
		throws LSPException
	{
		if (value == null)
			throw new LSPException(name + ": LSP cannot handle null objects");
		else if (value instanceof String)
			return value;
		else if (value instanceof Boolean)
			return value;
		else if (value instanceof Number)
			return value;
		else if (value instanceof LSPList) 
			return value;
		else if (value instanceof LSPTuple) 
			return value;
		else if (value instanceof Node)
			return value;
		else if (value instanceof Object[]) 
			return new LSPArrayList((Object[])value);
		else if (value instanceof Vector) 
			return new LSPVectorList((Vector)value);
		else if (value instanceof Collection) 
			return new LSPCollectionList((Collection)value);
		else if (value instanceof Enumeration) 
			return new LSPEnumerationList((Enumeration)value);
		else if (value instanceof Iterator) 
			return new LSPIteratorList((Iterator)value);
		else if (value instanceof java.sql.ResultSet) 
			return new LSPResultSetTupleList((java.sql.ResultSet)value);
		else if (value instanceof Dictionary) 
			return new LSPDictionaryTuple((Dictionary)value);
		else if (value instanceof Map) 
			return new LSPMapTuple((Map)value);
		else if (value instanceof char[])
			return new String((char[])value);
		else if (value instanceof byte[])
		{
			try {
				return new String((byte[])value, "ISO-8859-1");
			}
			catch (UnsupportedEncodingException e)
			{
				throw new Error("JVM doesn't support ISO-8859-1 encoding");	
			}
		}
		else
			throw new LSPException(
				name + ": LSP cannot handle objects of type "
				+ value.getClass().getName());
	}

	
	protected static String convertToString(Object value) throws LSPException
	{
		if (value instanceof String)
		{
			return (String)value;
		}
		else if (value instanceof Number)
		{
			double d = ((Number)value).doubleValue();
			if (d == 0)
				return "0";
			else if (d == Math.rint(d))
				return Long.toString(Math.round(d));
			else
				return Double.toString(d);
		}
		else if (value instanceof Boolean)
		{
			return value.toString();
		}
		else
		{
			throw new LSPException(
				"Convert to String not implemented for type "
				+ value.getClass().getName());
		}
	}

	
	protected static double convertToNumber(Object value) throws LSPException
	{
		if (value instanceof Number)
		{
			return ((Number)value).doubleValue();
		}
		else if (value instanceof Boolean)
		{
			return ((Boolean)value).booleanValue() ? 1.0d : 0.0d;
		}
		else if (value instanceof String)
		{
			try {
				return Double.valueOf((String)value).doubleValue();
			}
			catch (NumberFormatException e)
			{
				return Double.NaN;
			}
		}
		else
		{
			throw new LSPException(
				"Convert to Number not implemented for type "
				+ value.getClass().getName());
		}
	}


	protected static boolean convertToBoolean(Object value) throws LSPException
	{
		if (value instanceof Boolean)
		{
			return ((Boolean)value).booleanValue();
		}
		if (value instanceof Number)
		{
			double d = ((Number)value).doubleValue();
			return !((d == 0) || Double.isNaN(d));
		}
		if (value instanceof String)
		{
			return ((String)value).length() > 0;
		}
		if (value instanceof LSPList)
		{
			return ((LSPList)value).length() > 0;
		}
		else
		{
			throw new LSPException(
				"Convert to Boolean not implemented for type "
				+ value.getClass().getName());
		}
	}



	protected static LSPList convertToList(Object value) throws LSPException
	{
		if (value instanceof LSPList) 
			return (LSPList)value;
		else
			throw new LSPException(
				"Convert to list not implemented for type "
				+ value.getClass().getName());
	}
	

	protected static LSPTuple convertToTuple(Object value) throws LSPException
	{
		if (value instanceof LSPTuple) 
			return (LSPTuple)value;
		else
			throw new LSPException(
				"Convert to tuple not implemented for type "
				+ value.getClass().getName());
	}


	protected static void outputStringWithoutCR(ContentHandler sax, String s)
		throws SAXException
	{
		char[] cb = new char[s.length()];
		
		int ci = 0;
		for (int si = 0; si<s.length(); si++)
		{
			char sc = s.charAt(si);
			if (sc == '\r')
			{
				if (si<s.length() && (s.charAt(si+1) == '\n'))
					; // convert CR+LF to LF
				else
					cb[ci++] = '\n'; // convert alone CR to LF
			}
			else
			{
				cb[ci++] = sc;
			}
		}
		
		sax.characters(cb, 0, ci);
	}
	
	
	protected static Double doubleValueOf(double d)
	{
		return new Double(d);	
	}
		

	protected static Object getElementFromTuple(LSPTuple tuple, String key)
		throws LSPException
	{
		Object o = tuple.get(key);
		if (o == null)
			throw new LSPException("Element \'" + key + "\' not found in tuple");
		return convertObjectToLSP(o, "."+key);
	}

	
	protected static Object getVariableValue(Environment env, String varName)
		throws LSPException
	{
		Object o = env.lookup(varName);
		
		if (o == null)
			throw new LSPException(
				"Attempt to reference unbound variable: " + varName);
		else
			return o;
	}
		

	protected static void processInclude(
			String url, ContentHandler sax, URLResolver resolver)
		throws SAXException
	{
		try {
			IncludeHandler ih = new IncludeHandler(sax);

            resolver.resolve(url, ih);
		}
		catch (IOException e)
		{
			throw new SAXException(e);
		}
	}		

		
	protected static boolean compareEqual(Object left, Object right)
		throws LSPException
	{		
		if ((left instanceof Boolean) || (right instanceof Boolean))
		{
			return convertToBoolean(left) == convertToBoolean(right);
		}
		else if ((left instanceof Number) || (right instanceof Number))
		{
		 	return convertToNumber(left) == convertToNumber(right);
		}
		else
		{
		 	return convertToString(left).equals(convertToString(right));
		}
	}
	
		
	protected static boolean fnContains(String a, String b)
	{
		return a.indexOf(b) > -1;
	}
		
		
	protected static String fnSubstringBefore(String a, String b)
	{
		int index = a.indexOf(b);

		if (index < 0)
			return "";
		else
			return a.substring(0, index);		
	}
		

	protected static String fnSubstringAfter(String a, String b)
	{
		int index = a.indexOf(b);

		if (index < 0)
			return "";
		else
			return a.substring(index+1);
	}
	

	protected static String fnSubstring(String a, double bd)
	{
		if (Double.isNaN(bd)) return "";
				
		int b = (int)Math.round(bd);
		int c = a.length()+1;

		if (b > a.length()) b = a.length();
		if (c < 1) return "";
		if (c > (a.length()-b+1)) c = a.length()-b+1;

		return a.substring((b-1 < 0) ? 0 : (b-1), b-1+c);
	}


	protected static String fnSubstring(String a, double bd, double cd)
	{
		if (Double.isNaN(bd) || Double.isNaN(cd)) return "";

		int b = (int)Math.round(bd);
		int c = (int)Math.round(cd);

		if (b > a.length()) b = a.length();
		if (c < 1) return "";
		if (c > (a.length()-b+1)) c = a.length()-b+1;

		return a.substring((b-1 < 0) ? 0 : (b-1), b-1+c);
	}


	protected static String fnNormalizeSpace(String a)
	{
		String x = a.trim();

		StringBuffer sb = new StringBuffer(x.length());
		boolean inSpace = false;
		for (int i = 0; i<x.length(); i++)
		{
			char c = x.charAt(i);
			if (c > ' ')
			{
				inSpace = false;
				sb.append(c);
			}
			else
			{
				if (!inSpace)
				{
					sb.append(' ');
					inSpace = true;
				}
			}
		}
		return sb.toString();
	}

	
	protected static String fnTranslate(String a, String b, String c)
	{	
		StringBuffer sb = new StringBuffer(a.length());
		for (int i = 0; i<a.length(); i++)
		{
			char ch = a.charAt(i);
			int index = b.indexOf(ch);
			if (index < 0)
				sb.append(c);
			else if (index >= c.length())
				;
			else
				sb.append(c.charAt(index));
		}
		return sb.toString();
	}
	
	
	protected static double fnRound(double a)
	{	
		return Math.floor(a + 0.5d);
	}
	

	protected static LSPList fnSeq(double start, double end, double step)
	{
		ArrayList vec = new ArrayList((int)((end-start)/step));
		for (; start <= end; start+=step)
		 	vec.add(new Double(start));
		
		return new LSPCollectionList(vec);
	}

		
	protected abstract void _execute(
			ContentHandler sax, URLResolver resolver, Environment env,
			Map extLibs, ContentHandler _sax, AttributesImpl attrs)
		throws SAXException, IllegalArgumentException;	
}

