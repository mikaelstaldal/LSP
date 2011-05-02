package nu.staldal.ujl.tuple;

/**
 * Immutable tuple with 5 elements. 
 *
 * @author Mikael StÃ¥ldal
 * 
 * @param <T1> type of the first element in the tuple
 * @param <T2> type of the second element in the tuple
 * @param <T3> type of the third element in the tuple
 * @param <T4> type of the fourth element in the tuple
 * @param <T5> type of the fifth element in the tuple
 */
public class Tuple5<T1,T2,T3,T4,T5> implements Tuple {

    private static final long serialVersionUID = -7306116985504687709L;
    
    T1 v1;
    T2 v2;
    T3 v3;
    T4 v4;
    T5 v5;
    
    /**
     * Constructor.
     * 
     * @param v1  value of the first element in the tuple.
     * @param v2  value of the second element in the tuple.
     * @param v3  value of the third element in the tuple.
     * @param v4  value of the fourth element in the tuple.
     * @param v5  value of the fifth element in the tuple.
     */
    public Tuple5(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
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

    /**
     * Get the fourth element in the tuple.
     * 
     * @return the fourth element in the tuple.
     */
    public T4 getV4() {
        return v4;
    }
    
    /**
     * Get the fifth element in the tuple.
     * 
     * @return the fifth element in the tuple.
     */
    public T5 getV5() {
        return v5;
    }

    public int size() {
        return 5;
    }

}
