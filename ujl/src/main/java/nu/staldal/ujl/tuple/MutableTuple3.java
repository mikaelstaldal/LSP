package nu.staldal.ujl.tuple;

/**
 * Mutable tuple with 3 elements. 
 *
 * @author Mikael StÃ¥ldal
 * 
 * @param <T1> type of the first element in the tuple
 * @param <T2> type of the second element in the tuple
 * @param <T3> type of the third element in the tuple
 */
public class MutableTuple3<T1,T2,T3> extends Tuple3<T1,T2,T3> implements MutableTuple {

    private static final long serialVersionUID = 6556496594742314437L;

    /**
     * Constructor. Set all elements to <code>null</code>
     */
    public MutableTuple3() {
        super(null, null, null);
    }
    
    /**
     * Constructor.
     * 
     * @param v1  value of the first element in the tuple.
     * @param v2  value of the second element in the tuple.
     * @param v3  value of the third element in the tuple.
     */
    public MutableTuple3(T1 v1, T2 v2, T3 v3) {
        super(v1, v2, v3);
    }
    
    /**
     * Set the first element in the tuple.
     * 
     * @param v1  new value of the first element in the tuple.
     */
    public void setV1(T1 v1) {
        this.v1 = v1;
    }

    /**
     * Set the second element in the tuple.
     * 
     * @param v2  new value of the second element in the tuple.
     */
    public void setV2(T2 v2) {
        this.v2 = v2;
    }

    /**
     * Set the third element in the tuple.
     * 
     * @param v3  new value of the third element in the tuple.
     */
    public void setV3(T3 v3) {
        this.v3 = v3;
    }
    
}
