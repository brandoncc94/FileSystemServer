
package RMI;

public class InfoNode {
    private String name;
    private boolean isFile;
    private int size;

    public InfoNode(String name, boolean isFile,int pSize) {
        this.name = name;
        this.isFile = isFile;
        this.size = pSize;
    }
    
    public InfoNode(String name, boolean isFile) {
        this.name = name;
        this.isFile = isFile;
        this.size = 0;
    }

    public String getName() {
        return name;
    }

    public boolean isIsFile() {
        return isFile;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getSize(){
        return this.size;
    }
    
    
    
}
