package nu.staldal.ujl.tuple;

/**
 * Immutable tuple with 2 elements. 
 *
 * @author Mikael StÃ¥ldal
 * 
 * @param <T1> type of the first element in the tuple
 * @param <T2> type of the second element in the tuple
 */
public class Tuple2<T1,T2> implements Tuple {

    private static final long serialVersionUID = 4571261685643542021L;
    
    T1 v1;
    T2 v2;
    
    /**
     * Constructor.
     * 
     * @param v1  value of the first element in the tuple.
     * @param v2  value of the second element in the tuple.
     */
    public Tuple2(T1 v1, T2 v2) {
        this.v1 = v1;
        this.v2 = v2;
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
    
    public int size() {
        return 2;
    }

}
