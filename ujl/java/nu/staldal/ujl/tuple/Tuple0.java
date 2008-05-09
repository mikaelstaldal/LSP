package nu.staldal.ujl.tuple;

/**
 * Immutable tuple with 0 elements. Singleton.
 *
 * @author Mikael Ståldal
 */
public class Tuple0 implements Tuple {

    private static final long serialVersionUID = -2751375014811750915L;
    
    private static final Tuple0 instance = new Tuple0();
    
    /**
     * Get the sole instance.
     * 
     * @return the sole instance.
     */
    public static Tuple0 getInstance() {
        return instance;
    }
    
    /**
     * Private default constructor to prevent instantiation.
     */
    private Tuple0() {
        // nothing to do
    }
    
    
    public int size() {
        return 0;
    }
    
    /**
     * Enforce singleton when deserializing.
     * 
     * @return the sole instance.
     */
    public Object readResolve() {
        return instance;        
    }
    
}
