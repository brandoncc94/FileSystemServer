/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

/**
 *
 * @author Brandon
 */
public class InfoNodeFile extends InfoNode {
    private String content; 

    public InfoNodeFile(String name, boolean isFile, String pContent, int pSize) {
        super(name, isFile,pSize);
        content = pContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }    
    
}
