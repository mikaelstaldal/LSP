package nu.staldal.ujl.tuple;

/**
 * Immutable tuple with 1 element. 
 *
 * @author Mikael Ståldal
 * 
 * @param <T1> type of the first (and only) element in the tuple
 */
public class Tuple1<T1> implements Tuple {

    private static final long serialVersionUID = -7840467540416258533L;
    
    T1 v1;
    
    /**
     * Constructor.
     * 
     * @param v1  value of the first (and only) element in the tuple.
     */
    public Tuple1(T1 v1) {
        this.v1 = v1;
    }
    
    /**
     * Get the first element in the tuple.
     * 
     * @return the first element in the tuple.
     */
    public T1 getV1() {
        return v1;
    }
    
    public int size() {
        return 1;
    }

}
