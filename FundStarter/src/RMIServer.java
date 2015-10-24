
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public Object[] resposta = new Object[2];
    private ArrayList<ClientRequest> myRequests = new ArrayList<ClientRequest>();

    Scanner sc = new Scanner(System.in);
    Connection connection;
    PreparedStatement preparedstatement = null;
    Statement request = null;
    ResultSet rs = null;
    String query;
    ResultSet returnBD = null;

    protected RMIServer() throws RemoteException {
        super();
    }

    public ClientRequest verificaLogIn(ClientRequest person) throws RemoteException {

        resposta[0] = "userrec";
        resposta[1] = 11;

        return person;
    }

    public ClientRequest novoUtilizador(ClientRequest clrqst) throws RemoteException {

        String[] userInfo = (String[]) clrqst.getRequest()[1];

        clrqst.setStage(2);
        myRequests.add(clrqst);
        
        try
        {
            query = "SELECT * FROM utilizador WHERE username= '"+userInfo[2]+"'";
            request = connection.createStatement();
            rs = request.executeQuery(query);
            if (rs == null)
            {
                try {
                    query = "INSERT INTO utilizador (nome, apelido, username, pass, saldo) VALUES (?,?,?,?,?)";
                    preparedstatement = connection.prepareStatement(query);
                    preparedstatement.setString(1, userInfo[0]);
                    preparedstatement.setString(2, userInfo[1]);
                    preparedstatement.setString(3, userInfo[2]);
                    preparedstatement.setString(4, userInfo[3]);
                    preparedstatement.setInt(5, 100);
                    preparedstatement.executeUpdate();

                } catch (SQLException e) {
                    System.err.println("SQLException:" + e);
                }
                
                try {
                    query = "SELECT id FROM utilizador WHERE username='" + userInfo[0] + "'";
                    request = connection.createStatement();
                    rs = request.executeQuery(query);

                    rs.next();
                    resposta[0] = "infosave";
                    resposta[1] = rs.getInt(1);

                    clrqst.setResponse(resposta);
                    clrqst.setStage(3);

                    updateRequest(clrqst);

                } catch (SQLException ex) {
                    System.err.println("Erro:" + ex);
                } finally {
                    if (request != null) {
                        try {
                            request.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            
            else{
                resposta[0] = "user_already_exists";
                resposta[1] = userInfo[2];
                clrqst.setResponse(resposta);
                clrqst.setStage(3);
            }
            
        }  catch (SQLException e) {
            System.err.println("SQLException:" + e);
        }


        return clrqst;
    }

    public ClientRequest novoProjecto(ClientRequest clrqst) throws RemoteException { //Verificar Erro?

        System.out.println("[RMI Server] Função <novoProjecto> chamada!");

        String[] projectInfo = (String[]) clrqst.getRequest()[1];

        clrqst.setStage(2);
        myRequests.add(clrqst);

        try {
            query = "INSERT INTO projecto (titulo, descricao, valorpretendido, valoractual) VALUES (?,?,?,?)";
            preparedstatement = connection.prepareStatement(query);
            preparedstatement.setString(1, projectInfo[0]);
            preparedstatement.setString(2, projectInfo[1]);
            preparedstatement.setInt(3, Integer.parseInt(projectInfo[2]));
            preparedstatement.setInt(4, 0);
            preparedstatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQLException:" + e);
        }

        try {
            query = "SELECT id FROM projecto WHERE titulo='" + projectInfo[0] + "'";
            request = connection.createStatement();
            rs = request.executeQuery(query);

            rs.next();
            resposta[0] = "infosave";
            resposta[1] = rs.getInt(1);

            clrqst.setResponse(resposta);
            clrqst.setStage(3);

            updateRequest(clrqst);

        } catch (SQLException ex) {
            System.err.println("Erro:" + ex);
        } finally {
            if (request != null) {
                try {
                    request.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return clrqst;
    }

    public ClientRequest getUserSaldo(ClientRequest clrqst) throws RemoteException {

        System.out.println("[RMI Server] Função <getUserSaldo> chamada!");

        int userID = (int) clrqst.getRequest()[1];

        clrqst.setStage(2);
        myRequests.add(clrqst);

        try {
            query = "SELECT saldo FROM utilizador WHERE id='" + userID + "'";
            request = connection.createStatement();
            rs = request.executeQuery(query);

            rs.next();
            resposta[0] = rs.getInt("saldo");

            clrqst.setResponse(resposta);
            clrqst.setStage(3);

            updateRequest(clrqst);

        } catch (SQLException ex) {
            System.err.println("Erro:" + ex);
        } finally {
            if (request != null) {
                try {
                    request.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return clrqst;
    }

    public void DB() throws RemoteException {
        System.out.println("-------- PostgreSQL "
                + "JDBC Connection Testing ------------");

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;

        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        try {

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres", "postgres",
                    "oracle");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;

        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }

    }

    /**
     * Função responsável para actualizar os pedidos do cliente, a medida que eles vão sendo tratados.
     */
    private void updateRequest(ClientRequest clrqst) {

        int requestIndex = myRequests.indexOf(clrqst);

        myRequests.get(requestIndex).setStage(4);
    }

    public static void main(String[] args) throws RemoteException {

        int rmiPort;
        String rmiName;
        String bdPort;
        String bdName;

        PropertiesReaderRMI properties = new PropertiesReaderRMI();
        
        rmiPort = properties.getPort();
        rmiName=properties.getName();
        bdPort=properties.getBDPort();
        bdName=properties.getBDIP();
        
        RMIServerInterface remoteServer = new RMIServer();

        LocateRegistry.createRegistry(rmiPort).rebind(rmiName, remoteServer);
        remoteServer.DB();

        System.out.println("[RMI Server] Pronto e à escuta.");

    }
}
