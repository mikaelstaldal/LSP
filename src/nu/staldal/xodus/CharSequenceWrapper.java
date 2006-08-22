package nu.staldal.xodus;

/**
 * Wrap char[] as a CharSequence without copying.
 *
 * @author Mikael Ståldal
 */
public class CharSequenceWrapper implements CharSequence
{
    private final char[] charArray;
    private final int start;
    private final int end;
    
    /**
     * Private constructor to prevent instantiation from outside.
     */
    private CharSequenceWrapper() 
    {
        throw new Error();
    }
    
    private CharSequenceWrapper(char[] charArray, int start, int end)
    {
        this.charArray = charArray;
        this.start = start;
        this.end = end;
    }
    
    public char charAt(int index)
    {
        return charArray[start+index];
    }

    public int length()
    {
        return end-start;
    }

    public CharSequence subSequence(int start, int end)
    {
        if (start > length())
            throw new StringIndexOutOfBoundsException(start);
        
        if (end > length())        
            throw new StringIndexOutOfBoundsException(start);
        
        return new CharSequenceWrapper(charArray, this.start+start, this.start+end);
    }

    @Override
    public String toString()
    {
        return String.valueOf(charArray, start, end-start);
    }
    
    /**
     * Create a CharSequenceWrapper using a whole char[].
     * 
     * @param charArray the char[]
     * 
     * @return a CharSequenceWrapper for the whole charArray
     * 
     * @throws NullPointerException if charArray is <code>null</code>
     */
    public static CharSequence valueOf(char[] charArray)
    {
        if (charArray == null) throw new NullPointerException("charArray");
        return new CharSequenceWrapper(charArray, 0, charArray.length);
    }

    /**
     * Create a CharSequenceWrapper using part of a char[].
     * 
     * @param charArray the char[]
     * @param start     the start index, inclusive
     * @param end       the end index, exclusive
     * 
     * @return a CharSequenceWrapper for part of charArray
     * 
     * @throws NullPointerException if charArray is <code>null</code>
     * @throws StringIndexOutOfBoundsException if start or end is invalid. 
     */
    public static CharSequence valueOf(char[] charArray, int start, int end)
    {
        if (charArray == null) throw new NullPointerException("charArray");
        if (start < 0) throw new StringIndexOutOfBoundsException("start < 0");
        if (end < start) throw new StringIndexOutOfBoundsException("end < start");
        if (end > charArray.length) throw new StringIndexOutOfBoundsException("end > charArray.length");
        return new CharSequenceWrapper(charArray, start, end);
    }
}
