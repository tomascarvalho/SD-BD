
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

    public Object[] verificaLogIn(User person) throws RemoteException;

    public Object[] novoUtilizador(String[] userInfo) throws RemoteException;
    
    public Object[] getUserSaldo(int userID) throws RemoteException;
    
    public void DB() throws RemoteException;

    public Object[] novoProjecto(String[] projectInfo) throws RemoteException;
}
