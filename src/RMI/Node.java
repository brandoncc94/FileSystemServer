
package RMI;

import java.util.ArrayList;

public class Node<T> {
    private T data;
    private Node<T> parent;
    private ArrayList<Node<T>> children;

    public Node() { }
    
    public Node(Node<T> pNode, Node<T> pParent){
        InfoNode info = (InfoNode)pNode.getData();
        this.data = (T) new InfoNode(info.getName(),info.isIsFile(),info.getSize());
        System.out.println(((InfoNode)this.getData()).getName() +" , "+((InfoNode)this.getData()).isIsFile());
        this.parent = pParent;
        this.children = new ArrayList<>();
    }
    
    public Node(T data, Node<T> parent) {
        this.data = data;
        this.parent = parent;
        this.children = new ArrayList<>();
    }
    
    public boolean addChild(Node<InfoNode> pNode) {
        String nodeName = pNode.getData().getName();
        for (Node<T> node : children) {
            Node<InfoNode> child = (Node<InfoNode>) node;
            if(child.getData().getName().equals(nodeName)){
                return false;
            }
        }
        this.getChildren().add((Node<T>)pNode);
        return true;
    }
    
    public Node getChild(String pNodeName){
        for (Node<T> node : children) {
            Node<InfoNode> child = (Node<InfoNode>) node;
            if(child.getData().getName().equals(pNodeName)){
                return child;
            }
        }
        return null;
    }
    
    public boolean removeChild(Node<InfoNode> pNode){
        return children.remove(pNode);
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