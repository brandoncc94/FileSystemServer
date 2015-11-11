
package RMI;

import java.util.ArrayList;

public class Tree<T> {
    private Node<T> root;

    public Tree(T pRootData) {
        root = new Node<>();
        root.setData(pRootData);
        root.setChildren(new ArrayList<>());
    }

    public Node<T> getRoot() {
        return root;
    }

    public void setRoot(Node<T> root) {
        this.root = root;
    }
}
