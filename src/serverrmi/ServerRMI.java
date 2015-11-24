package serverrmi;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import maininterface.Connection;

public class ServerRMI {

    private void startServer(){
        try {
            Registry registry = LocateRegistry.createRegistry(Connection.RMI_PORT);            
            // Create a new Service
            registry.bind(Connection.RMI_ID, new Server());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }     
        System.out.println("El servidor está listo.");
    }
     
    public static void main(String[] args) {
        ServerRMI server = new ServerRMI();
        server.startServer();
    }
    
}
