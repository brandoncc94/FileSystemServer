
package serverrmi;

import RMI.FileSystem;
import RMI.InfoNode;
import RMI.InfoNodeFile;
import RMI.InfoPath;
import maininterface.IFunctions;
import RMI.Node;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class Server extends UnicastRemoteObject implements IFunctions {
 
    private ArrayList<FileSystem> fileSystems;
    private int directory = 0;
    public Server() throws RemoteException {
        fileSystems = new ArrayList<FileSystem>();
    }
    
    @Override
    public String create(int pSize) throws RemoteException {
        System.out.println("Creando disco virtual de tamaño  " + pSize + "Kb");
        int ascii = 65;
        String root = Character.toString((char)(ascii + directory));
        directory++;
        FileSystem new_FileSystem = new FileSystem(root, pSize);
        fileSystems.add(new_FileSystem);
        System.out.println("Disco virtual "+root+" creado exitosamente. Tamaño de disco "+ new_FileSystem.getSize()+"Kb.");
        return root;
    }
    
    @Override
    public int mkdir(String pName,String pRoot, boolean pOverride) throws RemoteException {
        FileSystem fileSystem = getFileSystem(pRoot);
        Node currentNode = fileSystem.getCurrent_Directory();
        Node existNode = currentNode.getChild(pName);
        if(existNode !=null){
            if(pOverride){
                currentNode.removeChild(existNode);
                currentNode.addChild(new Node(new InfoNode(pName,false),currentNode));
                return 1;
            }else{
                return -1;
            }
        }
        boolean created = currentNode.addChild(new Node(new InfoNode(pName,false),currentNode));
        return 1;
    }
    
    @Override
    public String tree(String pRoot) throws RemoteException {
        FileSystem fileSystem = getFileSystem(pRoot);
        Node<InfoNode> currentNode = fileSystem.getCurrent_Directory();
        String tree = "";
        String preString = fillString("",currentNode.getData().getName().length()+2)+"|";
        tree += "["+currentNode.getData().getName()+"]";
        if(currentNode.getChildren().size() > 0){
            //int sizeStr = sizeName(currentNode);
            tree += "- " + treeAux(currentNode.getChildren(),preString,0);
        }
        return tree;
    }
    
    private int sizeName(Node<InfoNode> pNode){
        int size = 0;
        for(Node<InfoNode> child : pNode.getChildren()){
            size = Integer.max(size,child.getData().getName().length());
        }
        return size;
    }
    
    private String fillString(String pString,int pSize){
        char[] charArray = new char[pSize];
        Arrays.fill(charArray, ' ');
        String str = new String(charArray);
        str = pString + str.substring(pString.length());
        return str;
    }
    
    private String treeAux(ArrayList<Node<InfoNode>> pChildren,String preString,int pSizeStr){
        String tree = "";
        int cont = 1;
        System.out.println(pChildren.size());
        int sizeChildren = pChildren.size();
        for (Node<InfoNode> child: pChildren) {
            boolean isFile = child.getData().isIsFile();
            if(cont != 1){
                tree += preString.substring(0, preString.length()-1) + "- ";
            }
            if(!isFile){
                tree += "["+child.getData().getName()+"]";
                if(child.getChildren().size() > 0){
                    //int sizeStr = sizeName(child);
                    int sizeStr = child.getData().getName().length()+2;
                    String str = fillString("",sizeStr+1)+"|";
                    tree += "- " + treeAux(child.getChildren(),preString + str,sizeStr);
                }
            }else{
                tree += "("+child.getData().getName()+")"; 
            }
            if(cont != sizeChildren ){
                tree += "\n" + preString +"\n";
            }
            cont++;
        }
        return tree;
    }
    
    @Override
    public String getActualPath(String pRoot) throws RemoteException {
        String path = "";
        FileSystem fs = getFileSystem(pRoot);
        Node<InfoNode> actual = fs.getCurrent_Directory();
        return getPath(actual,pRoot);
    }
    
    private String getPath(Node<InfoNode> pNode,String pRoot){
        String path = "";
        Node<InfoNode> actual = pNode;
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
    
    private Node findNode(Node pCurrentNode,String pName){
        for (Node<InfoNode> node : (ArrayList<Node>)pCurrentNode.getChildren() ) {
            if(node.getData().getName().equals(pName)){
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
        if(path[0].equals(pRoot+":")){
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
    public int createFile(String pFileNamePath, String pContent, String pPath, String pRoot, boolean pOverride) throws RemoteException {
        FileSystem fs = getFileSystem(pRoot);
        int endIndex = pFileNamePath.lastIndexOf("\\");
        String path = pPath; 
        String filename = pFileNamePath;
        Node<InfoNode> node = fs.getCurrent_Directory();
        if (endIndex != -1){ 
            path = pFileNamePath.substring(0, endIndex + 1);
            filename = pFileNamePath.substring(endIndex + 1, pFileNamePath.length());
            node = findPath(path,pRoot);
        }
        int freeSpace = fs.getSize() - getElementSize(fs.getFileSystem().getRoot());
        System.out.println("Espacio disponible : "+freeSpace);
        int tam = pContent.length()/1024 + 1;
        Node<InfoNode> newNode = new Node(new InfoNodeFile(filename,true, pContent, tam), node);
        if(node != null){
            Node<InfoNode> nodeExist = node.getChild(filename);
            if(nodeExist != null){
                if((freeSpace+getElementSize(nodeExist)-tam)<0)
                    return -1;
                else{
                    if(pOverride){
                        node.removeChild(nodeExist);
                        node.addChild(newNode);
                        return 1;
                    }else{
                        return 0;
                    }
                }
            }
            else{
                if((freeSpace-tam)<0){
                    return -1;
                }else{
                    node.addChild(newNode);
                    return 1;
                }
            }
        }
        return -2;
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
        FileSystem fs = getFileSystem(pRoot);
        String fileNamePath = params[1];
        int endIndex = fileNamePath.lastIndexOf("\\");
        String path = "";
        String newPath = params[2];
        String filename = fileNamePath;
        Node<InfoNode> oldDirectory = fs.getCurrent_Directory();
        if (endIndex != -1){ 
            path = fileNamePath.substring(0, endIndex + 1);
            filename = fileNamePath.substring(endIndex + 1, fileNamePath.length());
            oldDirectory = findPath(path,pRoot);
            if(oldDirectory == null)return false;
        }
        Node<InfoNode> node = findNode(oldDirectory, filename);
        if(node == null){
            return false;
        }
        Node<InfoNode> newDirectory = findPath(newPath,pRoot);
        if(newDirectory == null){
            return false;
        }
        boolean moved;
        Node<InfoNode> nodeExist;
        if(node.equals(newDirectory)){
            return false;
        }
        if(oldDirectory.equals(newDirectory)){
            if(params.length > 3){
                String newName = params[3];
                nodeExist = findNode(newDirectory,newName);
                if(nodeExist != null)return false;
                node.getData().setName(newName);
                return true;
            }
            return false;
        }else{
            nodeExist = findNode(newDirectory,node.getData().getName());
            if(nodeExist != null){
                return false;
            }
            oldDirectory.removeChild(node);
            newDirectory.addChild(node);
            node.setParent(newDirectory);
            return true;
        }
    }
       
    @Override
    public String cat(String[] pFilenames, String pRoot) throws RemoteException {
        FileSystem fs = getFileSystem(pRoot);
        
        String filesContent = "";
        for (String pFilename : pFilenames) {
            String path = ""; 
            String filename = pFilename;
            Node<InfoNode> node = fs.getCurrent_Directory();
            int endIndex = pFilename.lastIndexOf("\\");
            if (endIndex != -1){ 
                path = pFilename.substring(0, endIndex + 1);
                filename = pFilename.substring(endIndex + 1, pFilename.length());
                node = findPath(path,pRoot);
            }
            
            ArrayList children =  node.getChildren();

            for (Object object : children) {
                Node<InfoNodeFile> child;
                try{
                    child = (Node<InfoNodeFile>) object;
                    if(child.getData().isIsFile()){
                        if(child.getData().getName().equals(filename)){
                            filesContent += child.getData().getName() + "\n";
                            filesContent += child.getData().getContent() + "\n";
                        }
                    }
                }catch(Exception e){ }
            }
        }
        return filesContent;
    }

    @Override
    public int du(String pName, String pRoot) throws RemoteException {
        //REVISAR CUANDO SOLO ESTA el nombre !!!!!!!!!!!!!!!!!
        FileSystem fs = getFileSystem(pRoot);
        String fileNamePath = pName;
        Node<InfoNode> currentNode = fs.getCurrent_Directory();
        int endIndex = pName.lastIndexOf("\\");
        if(endIndex != -1){
            String path = pName.substring(0, endIndex + 1);
            String name = pName.substring(endIndex + 1, pName.length());
            System.out.println("Ruta: " +  path + ", nodo: "+ name);
            currentNode = findPath(path,pRoot);
            pName = name;
            if(currentNode == null)return -1;
            if(path.equals(pRoot+":\\") && name.equals(""))
                return getElementSize(currentNode);
        }
        currentNode = findNode(currentNode,pName);
        if(currentNode == null){
            return -1;
        }
        return getElementSize(currentNode);
    }
    
    private int getElementSize(Node<InfoNode> pNode){
        if(pNode.getData().isIsFile()){
            return pNode.getData().getSize();
        }else{
            int size = 0;
            for(Node<InfoNode> child : pNode.getChildren()){
                size += getElementSize(child);
            }
            return size;
        }
    }
    
    
    @Override
    public boolean rm(String[] filenames, boolean isDir, String pRoot) throws RemoteException{
        FileSystem fs = getFileSystem(pRoot);
        Node<InfoNode> node = fs.getCurrent_Directory();
        ArrayList children =  node.getChildren();
        ArrayList<Object> removedChildren = new ArrayList<>();
        
        if(isDir){
            try{
                if(filenames.length > 1){
                    node = findDirectory(node, filenames[1]);
                    children =  node.getChildren();
                }                
                if(node != null){
                    System.out.println("Nodo encontrado: " + node.getData().getName());
                }else{
                    System.out.println("ERROR! Directorio " + filenames[1] + " no encontrado.");
                }            
            }catch(Exception e) { return false; }
        }
        
        for (Object object : children) {
            try{
                Node<InfoNodeFile> child = (Node<InfoNodeFile>) object;
                if(child.getData().isIsFile()){
                    if(isDir)
                        removedChildren.add(child);
                    else{
                        for(int i = 0; i < filenames.length; i++){
                            if(child.getData().getName().equals(filenames[i])){
                                removedChildren.add(child);
                            }   
                        }   
                    }
                }
            }
            catch(Exception e){ }
        }
        
        for(int i = 0; i < removedChildren.size(); i++)
                    children.remove(removedChildren.get(i));
        
        return true;
    }
    
    @Override
    public String find(String pName, String pRoot) throws RemoteException{
        FileSystem fs = getFileSystem(pRoot);
        Node<InfoNode> node = fs.getFileSystem().getRoot();
        ArrayList children =  node.getChildren();
        pName = pName.replaceAll("\\*", ".*");
        pName = pName.replaceAll("\\+", ".+");
        return find_aux(children, pName, pRoot, "");
    }
    
    public String find_aux(ArrayList pChildren, String pName, String pRoot, String pRutas){
        if(pChildren.size() <= 0)
            return pRutas;
        for (Object object : pChildren) {
            Node<InfoNode> child = (Node<InfoNode>) object;
            if(child.getData().getName().matches(pName))
                pRutas += getPath(child, pRoot) + "\n";
            if(!child.getData().isIsFile())
                pRutas = find_aux(child.getChildren(), pName, pRoot, pRutas);
        }
        return pRutas;
    }
    
    @Override
    public boolean cpy(String[] paths, int type, String root) throws RemoteException{
        FileSystem fs = getFileSystem(root);
        if(type == 1){
            //Copiado virtual - virtual
            String fileNamePath = paths[0];
            int endIndex = fileNamePath.lastIndexOf("\\");
            String path = "";
            String newPath = paths[1];
            String filename = fileNamePath;
            Node<InfoNode> oldDirectory = fs.getCurrent_Directory();
            if (endIndex != -1){ 
                path = fileNamePath.substring(0, endIndex + 1);
                filename = fileNamePath.substring(endIndex + 1, fileNamePath.length());
                oldDirectory = findPath(path,root);
                if(oldDirectory == null)return false;
            }
            Node<InfoNode> node = findNode(oldDirectory, filename);
            if(node == null) return false;
            Node<InfoNode> newDirectory = findPath(newPath,root);
            if(newDirectory == null)return false;
            if(newDirectory.getData().isIsFile()) return false;

            Node<InfoNode> nodeExist;
            if(node.equals(newDirectory))
                return false;
            else{
                nodeExist = findNode(newDirectory,node.getData().getName());
                if(nodeExist != null)return false;
                newDirectory.addChild(node);
                node.setParent(newDirectory);
                return true;
            }
        }else if(type == 2){
            //Copiado real virtual
            File file = new File(paths[0]);
            if(!file.exists()) return false;
            if(file.isDirectory()){
                return true;
            }else{
                Node<InfoNode> newDir = findPath(paths[1],root);
                if(newDir == null)return false;
                if(newDir.getData().isIsFile()) return false;
                String content;
                try {
                    content = getContentFromFile(file.getAbsolutePath());
                    int tam = content.length()/1024 + 1;
                    Node<InfoNode> newNode = null;
                    newNode = new Node(new InfoNodeFile(file.getName(), 
                                            true, content, tam), newNode);
                    newDir.addChild(newNode);
                    newNode.setParent(newDir);
                    return true;
                } catch (IOException ex) {
                    return false;
                }
            }
        }else if (type == 3){
            //Copiado virtual real
            System.out.println("COPIADO VIRTUAL REAL");
            String path = "";
            String filename = paths[0];
            Node<InfoNode> node = fs.getCurrent_Directory();
            path = getPath(node, root);
            int endIndex = paths[0].lastIndexOf("\\");
            if (endIndex != -1){ 
                path = paths[0].substring(0, endIndex + 1);
                filename = paths[0].substring(endIndex + 1, paths[0].length());
                node = findPath(path, root);
            }
            System.out.println("PATH: " + path);
            Node<InfoNode> newNode = findPath(path, root);
            try{
                Node<InfoNodeFile> newDire = findNode(newNode, filename);
                if(newDire == null) return false;
                File f = new File(paths[1] + File.separator + newDire.getData().getName());
                //f.getParentFile().mkdirs();
                try {
                    f.createNewFile();                        
                    BufferedWriter writer = null;
                    writer = new BufferedWriter( new FileWriter(f));
                    writer.write(newDire.getData().getContent());
                    writer.close();
                    return true;
                } catch (IOException ex) {
                    return false;
                }
            }catch(Exception e){
                Node<InfoNode> newDire = findNode(newNode, filename);
                if(newDire == null) return false;
                //return cpy_aux(newDire.getChildren(), root, paths[1], filename);
            }
        }
        return false;
    }
    
    public String getContentFromFile(String pPath) throws FileNotFoundException, IOException{
        String text = "";
        try(BufferedReader br = new BufferedReader(new FileReader(pPath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            text = sb.toString();
        }
        return text;
    }
}
