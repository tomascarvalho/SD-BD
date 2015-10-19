
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

    /**
     * SÓ PARA TESTES!!!!!!!!!
     *
     * fazer depois uma arraylist com os users todos
     *
     */
    private User testeUser = new User("Gabriel", "teste");
    public Object[] resposta= new Object[2];

    protected RMIServer() throws RemoteException {
        super();
    }

    public Object[] verificaLogIn(User person) throws RemoteException {
        
        System.out.println("[RMI Server] Função <varificaLogIn> chamada!");
        if (person.getName().equals(testeUser.getName()) && person.getPass().equals(testeUser.getPass())) {
            resposta[0]="userrec";
            resposta[1]=1;/*só para teste, tem de ir bucar sempre id do user*/
        } else {
            resposta[0]="usernotrec";
            resposta[1]=-1;
        }
        System.out.println("Acabei");
        return resposta;
    }

    public Object[] novoUtilizador(String[] userInfo) throws RemoteException {
        
        System.out.println("[RMI Server] Função <novoUtilizador> chamada!");
        testeUser = new User(userInfo[0], userInfo[1]);

        resposta[0]="infosave";
        resposta[1]=1;/*só para teste, tem de ir bucar sempre id do user*/
        
        return resposta;
    }
    
    public Object[] getUserSaldo(int userID) throws RemoteException{
        
        /* depois terá de pedir a bd para ir buscar o saldo*/
        resposta[0]="seesal";
        resposta[1]=testeUser.getSaldo();
        
        return resposta;
    }

    public static void main(String[] args) throws RemoteException {

        RMIServerInterface remoteServer = new RMIServer();
        LocateRegistry.createRegistry(7777).rebind("fundStarter", remoteServer);
        System.out.println("[RMI Server] Pronto e à escuta.");

    }
}
