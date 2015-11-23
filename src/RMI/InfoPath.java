
package RMI;

import java.nio.file.Path;

public class InfoPath {
    
    private Path path;
    private boolean estado;

    public InfoPath(Path path, boolean estado) {
        this.path = path;
        this.estado = estado;
    }

    public Path getPath() {
        return path;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
