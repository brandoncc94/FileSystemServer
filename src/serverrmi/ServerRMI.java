package serverrmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRMI {

    private void startServer(){
        try {
            //Define port
            int port = 1099;
            Registry registry = LocateRegistry.createRegistry(port);
             
            // Create a new Service
            registry.rebind("CREATE", new Server());
            registry.rebind("MKDIR", new Server());
        } catch (Exception e) {
            e.printStackTrace();
        }     
        System.out.println("El servidor está listo.");
    }
     
    public static void main(String[] args) {
        ServerRMI server = new ServerRMI();
        server.startServer();
    }
    
}
