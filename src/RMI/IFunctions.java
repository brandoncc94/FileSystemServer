
package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFunctions extends Remote{
    //Función para crear espacio virtual en el disco
    void create(int pSize) throws RemoteException;
    //Función para crear un directorio
    void MKDIR(String pName) throws RemoteException;
}
