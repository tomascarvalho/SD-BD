
import java.rmi.*;

/*
 * FundStart
 *  Projecto para a cadeira de Sistemas Distribuidos
 *  Ano Lectivo 2015/1016
 *  Carlos Pinto 2011143469
 *  Diana Umbelino 2012******
 *  Tomás Carvalho 2012******
 */

/**
 *
 * @author gabrieloliveira
 */
public interface RMIServerInterface extends Remote {
    
    public ClientRequest getProjectDetails(ClientRequest clrqst) throws RemoteException;

    public ClientRequest verificaLogIn(ClientRequest clrqst) throws RemoteException;

    public ClientRequest novoUtilizador(ClientRequest userInfo) throws RemoteException;

    public ClientRequest getUserSaldo(ClientRequest clrqst) throws RemoteException;
    
    public ClientRequest getActualProjects(ClientRequest clrqst) throws RemoteException;
    
    public void DB() throws RemoteException;

    public ClientRequest novoProjecto(ClientRequest clrqst) throws RemoteException;

}
