
package RMI;

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
