package nu.staldal.xmltree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Node list.
 *
 * @author Mikael Ståldal
 */
class NodeList implements List<Node>, Serializable {

    private static final long serialVersionUID = 6133493000199140313L;
    
    private final ArrayList<Node> children;
    private final NodeWithChildren myNode;
    
    NodeList(NodeWithChildren myNode, int capacity) {
        this.myNode = myNode;
        if (capacity >= 0)
            this.children = new ArrayList<Node>(capacity);
        else
            this.children = new ArrayList<Node>();
    }
    
    public Node get(int index) {
        return children.get(index);
    }
    
    public boolean add(Node newChild) {
        newChild.setParent(myNode);
        return children.add(newChild);
    }
    
    public Node set(int index, Node newChild) {
        Node oldChild = children.get(index);
        oldChild.setParent(null);
        newChild.setParent(myNode);
        children.set(index, newChild);
        return oldChild;
    }

    public boolean addAll(Collection<? extends Node> c) {
        for (Node n : c) {
            add(n);
        }
        return c.size() > 0;
    }

    public void clear() {
        for (Node child : children) {
            child.setParent(null);
        }
        children.clear();
    }

    public boolean contains(Object o) {
        return children.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return children.containsAll(c);
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    public Iterator<Node> iterator() {
        return children.iterator();
    }

    public int size() {
        return children.size();
    }

    public Object[] toArray() {
        return children.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return children.toArray(a);
    }
    
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public void add(int index, Node element) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection<? extends Node> c) {
        throw new UnsupportedOperationException();
    }

    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    public ListIterator<Node> listIterator() {
        return children.listIterator();
    }

    public ListIterator<Node> listIterator(int index) {
        return children.listIterator(index);
    }

    public Node remove(int index) {
        throw new UnsupportedOperationException();
    }

    public List<Node> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
    
}
