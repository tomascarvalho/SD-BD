
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

    public Object[] verificaLogIn(User person) throws RemoteException {

        
        return resposta;
    }

    public Object[] novoUtilizador(String[] userInfo) throws RemoteException {/*Falta verifica se não deu erro a inserir na base de dados*/

        System.out.println("[RMI Server] Função <novoUtilizador> chamada!");

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
            query = "SELECT id FROM utilizador WHERE username='"+userInfo[0]+"'";
            request=connection.createStatement();
            rs=request.executeQuery(query);
            
            rs.next();
            resposta[0] = "infosave";
            resposta[1] = rs.getInt(1);
            

        } catch (SQLException ex) {
            System.err.println("Erro:"+ex);
        } finally {
            if (request != null) {
                try {
                    request.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return resposta;
    }

    public Object[] getUserSaldo(int userID) throws RemoteException {

        
        return resposta;
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

    public static void main(String[] args) throws RemoteException {

        RMIServerInterface remoteServer = new RMIServer();
        LocateRegistry.createRegistry(7777).rebind("fundStarter", remoteServer);
        remoteServer.DB();
        System.out.println("[RMI Server] Pronto e à escuta.");

    }
}
