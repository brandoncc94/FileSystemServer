
package serverrmi;

import RMI.FileSystem;
import RMI.InfoNode;
import maininterface.IFunctions;
import RMI.Tree;
import RMI.Node;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
 
public class Server extends UnicastRemoteObject implements IFunctions {
 
    private ArrayList<FileSystem> fileSystems;
    private int directory = 0;
    public Server() throws RemoteException {
        fileSystems = new ArrayList<FileSystem>();
    }
    
    @Override
    public String create(int pSize) throws RemoteException {
        System.out.println("Creando disco virtual de tamaño  " + pSize);
        int ascii = 65;
        String root = Character.toString((char)(ascii + directory));
        directory++;
        FileSystem new_FileSystem = new FileSystem(root, pSize);
        fileSystems.add(new_FileSystem);
        System.out.println("Disco virtual "+root+" creado exitosamente. Tamaño de disco "+ new_FileSystem.getSize()+".");
        return root;
    }
    
    @Override
    public boolean mkdir(String pName,String pRoot) throws RemoteException {
        System.out.println("Creando el directorio " + pName);
        FileSystem fileSystem = getFileSystem(pRoot);
        Node currentNode = fileSystem.getCurrent_Directory();
        boolean created = currentNode.addChild(new Node(new InfoNode(pName,false),currentNode));
        return created;
    }

    @Override
    public String getPath(String pRoot) throws RemoteException {
        String path = "";
        FileSystem fs = getFileSystem(pRoot);
        Node<InfoNode> actual = fs.getCurrent_Directory();
        if(actual.equals(actual.getParent())){
            return pRoot+":\\";
        }
        else{
            path = actual.getData().getName();
            actual = actual.getParent();
            while(!actual.equals(actual.getParent())){
                path = actual.getData().getName() + "\\" + path;
                actual = actual.getParent();
            }
            path = pRoot+":\\"+path;
            return path;
        }
    }
    
    private FileSystem getFileSystem(String pRoot){
        for (FileSystem fileSystem : fileSystems) {
            if(fileSystem.getRoot().equals(pRoot)){
                return fileSystem;
            }
        }
        return null;
    }
    
    private Node findDirectory(Node pCurrentNode,String pName){
        for (Node<InfoNode> node : (ArrayList<Node>)pCurrentNode.getChildren() ) {
            if(node.getData().getName().equals(pName) && !node.getData().isIsFile() ){
                return node;
            }
        }
        return null;
    }
    
    private Node findPath(String pPath,String pRoot){
        FileSystem fs = getFileSystem(pRoot);
        String[] path = pPath.split("\\\\");
        Node<InfoNode> node = null;
        boolean withRoot = false;
        if(path[0].equals(pRoot+":\\")){
            node = fs.getFileSystem().getRoot();
            withRoot = true;
        }
        int i = 1;
        if(!withRoot){
            node = fs.getCurrent_Directory();
            i=0;
        }
        while(i < path.length){
            if(path[i].equals(".")){
                i++;
                continue;
            }
            if(path[i].equals("..")){
                node = node.getParent();
                i++;
                continue;
            }
            else{
                node = findDirectory(node, path[i]);
                if(node==null){
                    System.out.println("No encontrado");
                    return node;
                }
                i++;
            }
        }
        return node;
    }

    @Override
    public boolean cd(String pNewPath, String pRoot) throws RemoteException {
        FileSystem fs = getFileSystem(pRoot);
        Node<InfoNode> node = findPath(pNewPath,pRoot);
        if(node == null)return false;
        else{
            fs.setCurrent_Directory(node);
            return true;
        }
    }

    @Override
    public String file(String pFileName,String pContent, String pPath, String pRoot) throws RemoteException {
        String[] path = pPath.split("\\\\");
        if(path.length==1){
            
        }
        Node<InfoNode> node = findPath(pPath,pRoot);
        return "";
    }

    @Override
    public String ls(String pRoot) throws RemoteException {
        FileSystem fs = getFileSystem(pRoot);
        String listChildren="";
        ArrayList children = fs.getCurrent_Directory().getChildren();
        System.out.println("La cantidad de archivos es :"+children.size());
        for (Object object : children) {
            Node<InfoNode> child = (Node<InfoNode>) object;
            listChildren = listChildren + child.getData().getName() +"\n";
        }
        return listChildren;
    }

    @Override
    public boolean mv(String[] params, String pRoot) throws RemoteException {
        return false;
    }
}
