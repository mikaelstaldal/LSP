package nu.staldal.xodus;

import javax.xml.transform.stream.StreamResult;

/**
 * StreamResult with an {@link java.lang.Appendable}.
 *
 * @author Mikael Ståldal
 */
public class AppendableStreamResult extends StreamResult
{
    private Appendable appendable;
    
    /**
     * Default constructor.
     */
    public AppendableStreamResult()
    {
        // nothing to do
    }

    /**
     * Constructor.
     * 
     * @param a  the Appendable
     */
    public AppendableStreamResult(Appendable a)
    {
        setAppendable(a);
    }
    
    
    /**
     * Get the Appendable.
     * 
     * @return the Appendable
     */
    public Appendable getAppendable()
    {
        return appendable;
    }

    /**
     * Set the Appendable
     * @param a  the Appendable
     */
    public void setAppendable(Appendable a)
    {
        this.appendable = a;
    }
        
}
