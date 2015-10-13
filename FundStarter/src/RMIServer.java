import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabrieloliveira
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
    
    
    /*
        SÓ PARA TESTES!!!!!!!!!
    */
    private User testeUser=new User("Gabriel","teste");
    protected RMIServer() throws RemoteException{
        super();
    }
    
    public String verificaLogIn(User person) throws RemoteException{
        
        if(person.getName().equals(testeUser.getName()) && person.getPass().equals(testeUser.getPass())){
            return "Utilizador reconhecido";
        }
        else{
            return "Utilizador não reconhecido";
        }
    }
    
    public static void main(String[] args) throws RemoteException{
        
        RMIServerInterface remoteServer=new RMIServer();
        LocateRegistry.createRegistry(7777).rebind("fundStarter", remoteServer);
        System.out.println("[RMI Server] Pronto e à escuta.");
        
    }
}
