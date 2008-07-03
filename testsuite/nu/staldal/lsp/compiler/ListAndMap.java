package nu.staldal.lsp.compiler;

import java.util.Collection;
import java.util.Iterator;

public class ListAndMap implements Collection<Object> {

    public String getFoo() {
        return "FOO";
    }
    
    public String getBar() {
        return "BAR";
    }

    
    // Collection
    
    public int size() {
        return 3;
    }
    
    public boolean isEmpty() {
        return size() == 0;
    }

    public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            private int i = 0;
            
            public boolean hasNext() {
                return i < 3;
            }

            public Object next() {
                return "Number "+i++;
            }

            public void remove() {
                throw new UnsupportedOperationException(); 
            }            
        };
    }

    public boolean add(Object o) {
        throw new UnsupportedOperationException(); 
    }

    public boolean addAll(Collection<? extends Object> c) {
        throw new UnsupportedOperationException(); 
    }

    public void clear() {
        throw new UnsupportedOperationException(); 
    }

    public boolean contains(Object o) {
        throw new UnsupportedOperationException(); 
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException(); 
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException(); 
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException(); 
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException(); 
    }

    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException(); 
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException(); 
    }
    
}
