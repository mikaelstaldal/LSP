package nu.staldal.zt;

import java.util.List;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import nu.staldal.util.Environment;
import nu.staldal.xmltree.Element;

/**
 * Element with ZeroTemplate If command.
 *
 * @author Mikael Ståldal
 */
public class ZtIfElement extends ZtElement {

    private static final long serialVersionUID = -769784640871887558L;
    
    private final String cond;
    
    public ZtIfElement(Element element, List<ZtAttr> attrs, String string,
            boolean stringIsLiteral, String map, String list, String cond) {
        super(element, attrs, string, stringIsLiteral, map, list);
        this.cond = cond;
    }

    public String getCond() {
        return cond;
    }

    @Override
    public void toSAX(ContentHandler sax, Environment<String, Object> env)
            throws SAXException {
        if (getBooleanNotNull(cond, env)) { 
            super.toSAX(sax, env);
        }
    }
    
}
