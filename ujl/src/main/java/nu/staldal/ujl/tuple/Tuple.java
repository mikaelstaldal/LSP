package nu.staldal.ujl.tuple;

import java.io.Serializable;

/**
 * Immutable tuple. 
 *
 * @author Mikael StÃ¥ldal
 */
public interface Tuple extends Serializable {

    /**
     * Number of elements in this tuple.
     * 
     * @return number of elements in this tuple.
     */
    public int size();
}
