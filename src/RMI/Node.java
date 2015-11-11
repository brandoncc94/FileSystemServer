
package RMI;

import java.util.ArrayList;

public class Node<T> {
    private T data;
    private Node<T> parent;
    private ArrayList<Node<T>> children;

    public Node() { }
    
    public Node(T data, Node<T> parent) {
        this.data = data;
        this.parent = parent;
        this.children = new ArrayList<>();
    }
    
    public void addChild(Node pNode) {
        this.getChildren().add(pNode);
    }

    public T getData() {
        return data;
    }

    public Node<T> getParent() {
        return parent;
    }

    public ArrayList<Node<T>> getChildren() {
        return children;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public void setChildren(ArrayList<Node<T>> children) {
        this.children = children;
    }
}