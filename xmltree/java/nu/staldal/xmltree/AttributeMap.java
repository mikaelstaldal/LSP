package nu.staldal.xmltree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;

/**
 * Map for element.attributes.
 *
 * @author Mikael Ståldal
 */
class AttributeMap implements Map<String, String> {

    private final Map<String, String> attributes;
    
    char xmlSpaceAttribute = ' ';
    
    AttributeMap(int numberOfAttributes) {
        if (numberOfAttributes >= 0) {
            attributes = new HashMap<String, String>(numberOfAttributes);
        }
        else {
            attributes = new HashMap<String, String>(0);
        }        
    }

    public String get(Object key) {
        return attributes.get(key);
    }

    public boolean containsKey(Object key) {
        return attributes.containsKey(key);
    }

    public int size() {
        return attributes.size();
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    public boolean containsValue(Object value) {
        return attributes.containsValue(value);
    }

    public Set<java.util.Map.Entry<String, String>> entrySet() {
        return attributes.entrySet();
    }

    public Set<String> keySet() {
        return attributes.keySet();
    }

    public Collection<String> values() {
        return attributes.values();
    }

    public String put(String name, String value) {
        if (name.equals('{' + XMLConstants.XML_NS_URI + "}space")) {
            if (value.equals("preserve"))
                xmlSpaceAttribute = 'p';
            else if (value.equals("default"))
                xmlSpaceAttribute = 'd';
        }

        return attributes.put(name, value);
    }

    public void putAll(Map<? extends String, ? extends String> t) {
        for (Map.Entry<? extends String, ? extends String> entry : t.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public void clear() {
        attributes.clear();
    }

    public String remove(Object name) {
        return attributes.remove(name);
    }

}
