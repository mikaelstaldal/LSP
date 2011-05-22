package nu.staldal.ujl.tuple;

/**
 * Mutable tuple with 5 elements. 
 *
 * @author Mikael StÃ¥ldal
 * 
 * @param <T1> type of the first element in the tuple
 * @param <T2> type of the second element in the tuple
 * @param <T3> type of the third element in the tuple
 * @param <T4> type of the fourth element in the tuple
 * @param <T5> type of the fifth element in the tuple
 */
public class MutableTuple5<T1,T2,T3,T4,T5> extends Tuple5<T1,T2,T3,T4,T5> implements MutableTuple {

    private static final long serialVersionUID = 6504645229670102824L;

    /**
     * Constructor. Set all elements to <code>null</code>
     */
    public MutableTuple5() {
        super(null, null, null, null, null);
    }
    
    /**
     * Constructor.
     * 
     * @param v1  value of the first element in the tuple.
     * @param v2  value of the second element in the tuple.
     * @param v3  value of the third element in the tuple.
     * @param v4  value of the fourth element in the tuple.
     * @param v5  value of the fifth element in the tuple.
     */
    public MutableTuple5(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5) {
        super(v1, v2, v3, v4, v5);
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

    /**
     * Set the fourth element in the tuple.
     * 
     * @param v4  new value of the fourth element in the tuple.
     */
    public void setV4(T4 v4) {
        this.v4 = v4;
    }
    
    /**
     * Set the fifth element in the tuple.
     * 
     * @param v5  new value of the fifth element in the tuple.
     */
    public void setV5(T5 v5) {
        this.v5 = v5;
    }
}
