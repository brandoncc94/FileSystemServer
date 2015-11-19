
package RMI;

public class InfoNodeFile extends InfoNode {
    private String content; 
    private int size;

    public InfoNodeFile(String name, boolean isFile, String pContent, int pSize) {
        super(name, isFile);
        content = pContent;
        size = pSize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
