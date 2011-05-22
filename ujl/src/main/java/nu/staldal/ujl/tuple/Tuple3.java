package nu.staldal.ujl.tuple;

/**
 * Immutable tuple with 3 elements. 
 *
 * @author Mikael StÃ¥ldal
 * 
 * @param <T1> type of the first element in the tuple
 * @param <T2> type of the second element in the tuple
 * @param <T3> type of the third element in the tuple
 */
public class Tuple3<T1,T2,T3> implements Tuple {

    private static final long serialVersionUID = -8364234491611374924L;
    
    T1 v1;
    T2 v2;
    T3 v3;
    
    /**
     * Constructor.
     * 
     * @param v1  value of the first element in the tuple.
     * @param v2  value of the second element in the tuple.
     * @param v3  value of the third element in the tuple.
     */
    public Tuple3(T1 v1, T2 v2, T3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }
    
    /**
     * Get the first element in the tuple.
     * 
     * @return the first element in the tuple.
     */
    public T1 getV1() {
        return v1;
    }

    /**
     * Get the second element in the tuple.
     * 
     * @return the second element in the tuple.
     */
    public T2 getV2() {
        return v2;
    }
    
    /**
     * Get the third element in the tuple.
     * 
     * @return the third element in the tuple.
     */
    public T3 getV3() {
        return v3;
    }

    public int size() {
        return 3;
    }

}
