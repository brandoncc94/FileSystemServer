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
public class InfoNode {
    private String name;
    private boolean isFile;

    public InfoNode(String name, boolean isFile) {
        this.name = name;
        this.isFile = isFile;
    }

    public String getName() {
        return name;
    }

    public boolean isIsFile() {
        return isFile;
    }
    
    
}
