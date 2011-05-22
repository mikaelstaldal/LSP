package nu.staldal.ujl.tuple;

/**
 * Mutable tuple with 1 element. 
 *
 * @author Mikael StÃ¥ldal
 * 
 * @param <T1> type of the first (and only) element in the tuple
 */
public class MutableTuple1<T1> extends Tuple1<T1> implements MutableTuple {

    private static final long serialVersionUID = -443554359477991644L;

    /**
     * Constructor. Set elements to <code>null</code>
     */
    public MutableTuple1() {
        super(null);
    }
    
    /**
     * Constructor.
     * 
     * @param v1  value of the first (and only) element in the tuple.
     */
    public MutableTuple1(T1 v1) {
        super(v1);
    }
    
    /**
     * Set the first element in the tuple.
     * 
     * @param v1  new value of the first element in the tuple.
     */
    public void setV1(T1 v1) {
        this.v1 = v1;
    }

}
