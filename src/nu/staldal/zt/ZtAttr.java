package nu.staldal.zt;

import java.io.Serializable;

/**
 * Zt attribute.
 *
 * @author Mikael Ståldal
 */
public class ZtAttr implements Serializable {
    
    private static final long serialVersionUID = -9049241571080057003L;
    
    private final String attributeName;
    private final String string;
    
    public ZtAttr(String attributeName, String string) {
        this.attributeName = attributeName;
        this.string = string;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getString() {
        return string;
    }
    
}
