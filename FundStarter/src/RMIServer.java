
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public ClientRequest verificaLogIn(ClientRequest clrqst) throws RemoteException {

        Object[] objecto = clrqst.getRequest();
        String[] person = (String[]) objecto[1];
        String user = person[0];
        System.out.println(user);
        String password = person[1];
        int id = -1;

        try {
            query = "SELECT id, username, pass FROM utilizador WHERE username = ? AND pass = ?";
            preparedstatement = connection.prepareStatement(query);
            preparedstatement.setString(1, user);
            preparedstatement.setString(2, password);
            rs = preparedstatement.executeQuery();
            if (rs.next()) {
                System.out.println("O user existe e está correcto");
                resposta[0] = "log_in_correcto";
                id = rs.getInt("id");
                resposta[1] = id;
                
                
                System.out.println("ID request->" + clrqst.getRequestID());
            } else {
                System.out.println("User/pass incorrecta/ não existentes");
                resposta[0] = "log_in_error";
                resposta[1] = id;
            }

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex);
        }

        clrqst.setResponse(resposta);

        return clrqst;
    }

    public ClientRequest novoUtilizador(ClientRequest clrqst) throws RemoteException {

        String[] userInfo = (String[]) clrqst.getRequest()[1];

        clrqst.setStage(2);
        myRequests.add(clrqst);

        try {

            query = "SELECT * FROM utilizador WHERE username= '" + userInfo[0] + "'";
            request = connection.createStatement();
            rs = request.executeQuery(query);

            if (!rs.next()) //User não existe, logo insere
            {
                try {
                    query = "INSERT INTO utilizador (username, pass, saldo) VALUES (?,?,?)";
                    preparedstatement = connection.prepareStatement(query);
                    preparedstatement.setString(1, userInfo[0]);
                    preparedstatement.setString(2, userInfo[1]);
                    preparedstatement.setInt(3, 100);
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

                    System.out.println("ID request->" + clrqst.getRequestID());
                    updateRequest(clrqst);

                } catch (SQLException ex) {
                    System.err.println("SQLException: " + ex);
                } finally {
                    if (resposta == null) {
                        resposta[0] = "erro";
                    }
                    if (request != null) {
                        try {
                            request.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else {
                resposta[0] = "user_already_exists";
                resposta[1] = userInfo[0];
                clrqst.setResponse(resposta);
                clrqst.setStage(3);
            }

        } catch (SQLException e) {
            System.err.println("SQLException:" + e);
        }

        return clrqst;
    }

    public ClientRequest novoProjecto(ClientRequest clrqst) throws RemoteException { //Verificar Erro

        System.out.println("[RMI Server] Função <novoProjecto> chamada!");

        String[] projectInfo = (String[]) clrqst.getRequest()[1];
        int id_niveis_extra = 0, id = 0, i, j, k;
        clrqst.setStage(2);
        myRequests.add(clrqst);
        String myUserID = "" + clrqst.getRequest()[0];

        try {
            query = "INSERT INTO projecto (titulo, descricao, valorpretendido, data_limite, valoractual) VALUES (?,?,?,?,?)";
            preparedstatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedstatement.setString(1, projectInfo[0]);
            preparedstatement.setString(2, projectInfo[1]);
            preparedstatement.setInt(3, Integer.parseInt(projectInfo[2]));
            preparedstatement.setDate(4, java.sql.Date.valueOf(projectInfo[3]));
            preparedstatement.setInt(5, 0);
            preparedstatement.executeUpdate();
            rs = preparedstatement.getGeneratedKeys();
            rs.next();
            id = rs.getInt("id");

        } catch (SQLException e) {
            System.err.println("SQLException:" + e);
        }

        try {
            i = 0;
            j = 0;
            if (Integer.parseInt(projectInfo[4]) != 0) {
                k = Integer.parseInt(projectInfo[4]);
                while (i < k) {
                    j++;
                    i++;
                    query = "INSERT INTO recompensas (valor, titulo, id_projecto) VALUES(?,?,?)";
                    preparedstatement = connection.prepareStatement(query);
                    preparedstatement.setInt(1, Integer.parseInt(projectInfo[4 + j]));
                    j++;
                    preparedstatement.setString(2, projectInfo[4 + j]);
                    preparedstatement.setInt(3, id);
                    preparedstatement.executeUpdate();

                }

            }

            j++;
            i = 0;
            if (Integer.parseInt(projectInfo[4 + j]) != 0) {
                k = Integer.parseInt(projectInfo[4 + j]);
                while (i < k) {
                    j++;
                    i++;
                    query = "INSERT INTO niveis_extra (descricao, valor, id_projecto) VALUES (?,?,?)";
                    preparedstatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    preparedstatement.setInt(2, Integer.parseInt(projectInfo[4 + j]));

                    j++;
                    preparedstatement.setString(1, projectInfo[4 + j]);
                    preparedstatement.setInt(3, id);
                    preparedstatement.executeUpdate();
                }
                rs = preparedstatement.getGeneratedKeys();
                rs.next();
                id_niveis_extra = rs.getInt("id");
                query = "UPDATE projecto SET id_niveis_extra=" + id_niveis_extra + " WHERE id=" + id;
                preparedstatement = connection.prepareStatement(query);
                preparedstatement.executeUpdate();

            }

            j++;
            i = 0;
            k = Integer.parseInt(projectInfo[4 + j]);
            while (i < k) {
                i++;
                j++;
                query = "INSERT INTO product_type (descricao, id_project, contador) VALUES (?,?,?)";
                preparedstatement = connection.prepareStatement(query);
                preparedstatement.setString(1, projectInfo[4 + j]);
                preparedstatement.setInt(2, id);
                preparedstatement.setInt(3, 0);
            }

            resposta[0] = "infosave";

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

    public ClientRequest getActualProjects(ClientRequest clrqst) throws RemoteException {

        int i;
        System.out.println("[RMI Server] Função <getActualProjects> chamada!");
        String[] actual_projects = new String[2000];
        Object[] objecto = clrqst.getRequest();
        int choice = (int) objecto[1];

        clrqst.setStage(2);
        myRequests.add(clrqst);

        try {

            if (choice == 0) {
                query = "SELECT * FROM projecto WHERE status = TRUE";
            } else {
                query = "SELECT * FROM projecto WHERE status = FALSE";
            }
            request = connection.createStatement();
            rs = request.executeQuery(query);
            i = 0;
            if (!rs.next()) {
                if (choice == 0) {
                    System.out.println("Nao ha projectos activos"); //Queremos mandar isto para o cliente?
                    actual_projects[0] = "error_no_active_projects";
                    resposta[0] = actual_projects;
                } else {
                    System.out.println("Não há projectos antigos");
                    actual_projects[0] = "error_no_old_projects";
                    resposta[0] = actual_projects;
                }

            } //Aqui percorro os projectos e mando o título e o ID
            else {

                actual_projects[i] = "" + rs.getInt("id");
                i++;
                actual_projects[i] = rs.getString("titulo");
                i++;
                actual_projects[i] = "" + rs.getInt("valoractual");
                i++;
                actual_projects[i] = "" + rs.getInt("valorpretendido");
                i++;

                while (rs.next()) {

                    actual_projects[i] = "" + rs.getInt("id");
                    i++;
                    actual_projects[i] = rs.getString("titulo");
                    i++;
                    actual_projects[i] = "" + rs.getInt("valoractual");
                    i++;
                    actual_projects[i] = "" + rs.getInt("valorpretendido");
                    i++;

                }
                resposta[0] = actual_projects;
            }
            resposta[1] = i;

            clrqst.setResponse(resposta);
            clrqst.setStage(3);

            updateRequest(clrqst);

        } catch (SQLException ex) {
            System.err.println("SQLException:" + ex);

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
        rmiName = properties.getName();
        bdPort = properties.getBDPort();
        bdName = properties.getBDIP();

        RMIServerInterface remoteServer = new RMIServer();

        LocateRegistry.createRegistry(rmiPort).rebind(rmiName, remoteServer);
        remoteServer.DB();

        System.out.println("[RMI Server] Pronto e à escuta.");

    }
}
