package nu.staldal.zt;

import java.util.Collections;

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

    public ZtRemoveElement(Element element) {
        super(element, Collections.EMPTY_LIST, null, false, null, null, false);
    }
        
    @Override
    public void toSAX(ContentHandler sax, Environment<String, Object> env)
            throws SAXException {
        // do not output anything here
    }
}
