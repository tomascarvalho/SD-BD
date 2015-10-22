
import java.rmi.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gabrieloliveira
 */
public interface RMIServerInterface extends Remote {

    public ClientRequest verificaLogIn(ClientRequest person) throws RemoteException;

    public ClientRequest novoUtilizador(ClientRequest userInfo) throws RemoteException;
    
    public ClientRequest getUserSaldo(ClientRequest clrqst) throws RemoteException;
    
    public void DB() throws RemoteException;

    public ClientRequest novoProjecto(ClientRequest clrqst) throws RemoteException;
    
}
