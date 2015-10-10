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
    
    protected RMIServer() throws RemoteException{
        super();
    }
    
    public String testeMessage(String cMessage) throws RemoteException{
        
        System.out.println("[RMI Server]Recebi isto:"+cMessage);
        if(cMessage.equals("Teste")){
            return "Mensagem de Teste Recebida";
        }
        else{
            return "Erro!";
        }
    }
    
    public static void main(String[] args) throws RemoteException{
        
        RMIServerInterface remoteServer=new RMIServer();
        LocateRegistry.createRegistry(7777).rebind("fundStarter", remoteServer);
        System.out.println("[RMI Server] Pronto e à escuta.");
        
    }
}
