package serverrmi;

import RMI.Node;
import RMI.Tree;
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
        
        Tree tree = new Tree("Raíz");
        tree.getRoot().addChild(new Node("Hijo 1", tree.getRoot()));
        System.out.println(tree.getRoot().getData());
        System.out.println(((Node)tree.getRoot().getChildren().get(0)).getData());
        System.out.println(((Node)tree.getRoot().getChildren().get(0)).getParent().getData());
        ServerRMI server = new ServerRMI();
        server.startServer();
    }
    
}
