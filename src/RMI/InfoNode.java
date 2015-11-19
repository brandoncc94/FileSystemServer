
package RMI;

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
