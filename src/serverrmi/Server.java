
package serverrmi;

import maininterface.IFunctions;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
 
public class Server extends UnicastRemoteObject implements IFunctions {
 
    public Server() throws RemoteException { }
    
    @Override
    public void create(int pSize) throws RemoteException {
        System.out.println("Creando disco virtual de tamaño  " + pSize);
    }
    
    @Override
    public void MKDIR(String pName) throws RemoteException {
        System.out.println("Creando el directorio " + pName);
    }
     
}
