/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

/**
 *
 * @author Juan Carlos
 */
public class FileSystem {
    private String root;
    private Tree fileSystem;
    private Node current_Directory;
    private int size;
    private int freeSpace;

    public FileSystem(String pRoot, int pSize) {
        this.root = pRoot;
        this.size = pSize;
        this.freeSpace = pSize;
        this.fileSystem = new Tree<InfoNode>(new InfoNode(root,false));
        this.current_Directory = this.fileSystem.getRoot();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int Size) {
        this.size = Size;
    }

    public int getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(int FreeSpace) {
        this.freeSpace = FreeSpace;
    }
    
    public void setCurrent_Directory(Node current_Directory) {
        this.current_Directory = current_Directory;
    }

    public Node getCurrent_Directory() {
        return current_Directory;
    }

    public String getRoot() {
        return root;
    }

    public Tree getFileSystem() {
        return fileSystem;
    }

    
    
    
}
