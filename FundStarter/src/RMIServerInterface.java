
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

    public String verificaLogIn(User person) throws RemoteException;

    public String novoUtilizador(String[] userInfo) throws RemoteException;
}
