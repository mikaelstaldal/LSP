package nu.staldal.zt;

import java.util.List;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import nu.staldal.util.Environment;
import nu.staldal.xmltree.Element;

/**
 * ZtRemove.
 *
 * @author Mikael Ståldal
 */
public class ZtRemoveElement extends ZtElement {

    private static final long serialVersionUID = -665145344324415819L;

    public ZtRemoveElement(Element element, List<ZtAttr> attrs, String string,
            boolean stringIsLiteral, String map, String list) {
        super(element, attrs, string, stringIsLiteral, map, list);
    }
        
    @Override
    public void toSAX(ContentHandler sax, Environment<String, Object> env)
            throws SAXException {
        // do not output anything here
    }
}
