package nu.staldal.zt;

import java.util.List;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import nu.staldal.util.Environment;
import nu.staldal.xmltree.Element;

/**
 * Element with ZeroTemplate IfNot command.
 *
 * @author Mikael Ståldal
 */
public class ZtIfNotElement extends ZtElement {

    private static final long serialVersionUID = -1264317968724820687L;
    
    private final String cond;
    
    public ZtIfNotElement(Element element, List<ZtAttr> attrs, String string,
            boolean stringIsLiteral, String map, String list, boolean listIsOddEven, String cond) {
        super(element, attrs, string, stringIsLiteral, map, list, listIsOddEven);
        this.cond = cond;
    }
    
    public String getCond() {
        return cond;
    }

    @Override
    public void toSAX(ContentHandler sax, Environment<String, Object> env)
            throws SAXException {
        if (!getBooleanNotNull(cond, env)) { 
            super.toSAX(sax, env);
        }
    }
    
}
