package nu.staldal.ujl.tuple;

/**
 * Mutable tuple with 2 elements. 
 *
 * @author Mikael StÃ¥ldal
 * 
 * @param <T1> type of the first element in the tuple
 * @param <T2> type of the second element in the tuple
 */
public class MutableTuple2<T1,T2> extends Tuple2<T1,T2> implements MutableTuple {

    private static final long serialVersionUID = -8709763840212962134L;

    /**
     * Constructor. Set all elements to <code>null</code>
     */
    public MutableTuple2() {
        super(null, null);
    }
    
    /**
     * Constructor.
     * 
     * @param v1  value of the first element in the tuple.
     * @param v2  value of the second element in the tuple.
     */
    public MutableTuple2(T1 v1, T2 v2) {
        super(v1, v2);
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
    
}
