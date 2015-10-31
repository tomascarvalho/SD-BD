
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;


/*
 * FundStart
 *  Projecto para a cadeira de Sistemas Distribuidos
 *  Ano Lectivo 2015/1016
 *  Carlos Pinto 2011143469
 *  Diana Umbelino 2012169525
 *  Tomás Carvalho 2012138578
 */
/**
 *
 * @author gabrieloliveira
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

    public Object[] resposta = new Object[5]; //alterei
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
        int userID = Integer.parseInt(myUserID);

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
            query = "INSERT INTO projecto_user (id_projecto, id_user) VALUES (?,?)";
            preparedstatement = connection.prepareStatement(query);
            preparedstatement.setInt(1, id);
            preparedstatement.setInt(2, userID);
            preparedstatement.executeUpdate();
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

                    preparedstatement = connection.prepareStatement(query);
                    preparedstatement.setInt(2, Integer.parseInt(projectInfo[4 + j]));
                    j++;

                    preparedstatement.setString(1, projectInfo[4 + j]);
                    preparedstatement.setInt(3, id);
                    preparedstatement.executeUpdate();
                }

            }

            j++;
            i = 0;

            if (Integer.parseInt(projectInfo[4 + j]) != 0) {
                k = Integer.parseInt(projectInfo[4 + j]);
                while (i < k) {
                    i++;
                    j++;
                    query = "INSERT INTO product_type (descricao, id_project, contador) VALUES (?,?,?)";
                    preparedstatement = connection.prepareStatement(query);
                    preparedstatement.setString(1, projectInfo[4 + j]);
                    preparedstatement.setInt(2, id);
                    preparedstatement.setInt(3, 0);
                    preparedstatement.executeUpdate();
                }

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
    
    public ClientRequest donateReward(ClientRequest clrqst) throws RemoteException{
        
        clrqst.setStage(2);
        myRequests.add(clrqst);
        int id_projecto = (int)clrqst.getRequest()[1];
        String username = (String)clrqst.getRequest()[2];
        String recompensa = (String)clrqst.getRequest()[3];
        int id_recompensa = 0;
        int id_user = 0;
        try{
            query = "SELECT id FROM utilizador WHERE username = '"+username+"'";
            request = connection.createStatement();
            rs = request.executeQuery(query);
            if (rs.next()){
                id_user = rs.getInt("id");
                try{
                    query = "SELECT id FROM recompensas WHERE titulo = '"+recompensa+"'";
                    request = connection.createStatement();
                    rs = request.executeQuery(query);
                    if (rs.next()){
                        id_recompensa = rs.getInt("id");
                        
                        try{
                            
                            query = "UPDATE recompensa_user SET id_user= "+id_user+" WHERE id_recompensa= "+id_recompensa;
                            preparedstatement = connection.prepareStatement(query);
                            preparedstatement.executeUpdate();
                        
                            
                            resposta[0] = "success";
                        } catch(SQLException ex){
                            System.err.print("SQLException 289: "+ex);
                        }
                    
                    }
                    else{
                        resposta[0] = "no_reward";
                    }
                    
                } catch(SQLException ex)
                {
                    System.err.print("SQLException 290: "+ex);
                }
                
            }
            else{
                resposta[0] = "no_user_found";
            }
            
            
        } catch(SQLException ex){
            System.err.print("SQLException 283: "+ ex);
        }
        
        clrqst.setResponse(resposta);
        clrqst.setStage(3);
        updateRequest(clrqst);
        return clrqst;
    }
    
    
   
    public ClientRequest getUserProjects(ClientRequest clrqst) throws RemoteException{
        
        System.out.println("[RMI Server] Função <getUserProjects> chamada!");
        ArrayList <Integer> lista_ids = new ArrayList<Integer>();
        ArrayList <String> lista_projectos = new ArrayList<String>();
        
        int project_id =0;
        int userID = (int) clrqst.getRequest()[1];
        
        clrqst.setStage(2);
        myRequests.add(clrqst);
        
        try{
            query = "SELECT id_projecto FROM projecto_user WHERE id_user ="+userID;
            request = connection.createStatement();
            rs = request.executeQuery(query);
            while(rs.next()){

                lista_ids.add(rs.getInt("id_projecto"));
            }
            resposta[0] = lista_ids;
        }catch (SQLException ex) {
            System.err.println("Erro 1:" + ex);
        }
            
        try{
            Iterator <Integer> it = lista_ids.iterator();
            while (it.hasNext()){
                project_id = it.next();
                query = "SELECT titulo FROM projecto WHERE id="+ project_id ;
                request = connection.createStatement();
                rs = request.executeQuery(query);
                if(rs.next()){
                    lista_projectos.add(rs.getString("titulo"));
                }
            }
            
            resposta[1] = lista_projectos;
        }catch (SQLException ex) {
            System.err.println("Erro 2:" + ex);
        }
            clrqst.setResponse(resposta);
            clrqst.setStage(3);
            
            updateRequest(clrqst);

        return clrqst;
        
    }
    
    public ClientRequest enviaMensagem(ClientRequest clrqst) throws RemoteException{
        System.out.println("[RMI Server] Função <enviaMensagem> chamada!");
        int userID = (int) clrqst.getRequest()[0];
        int projectID = (int) clrqst.getRequest()[1];
        String mensagem = (String) clrqst.getRequest()[2];
        clrqst.setStage(2);
        myRequests.add(clrqst);
        try{
            query = "INSERT INTO mensagem(id_user_envia, id_projecto,pergunta) VALUES (?,?,?)";
            preparedstatement = connection.prepareStatement(query);
            preparedstatement.setInt(1, userID);
            preparedstatement.setInt(2, projectID);
            preparedstatement.setString(3, mensagem);
            preparedstatement.executeUpdate();            
            
        }catch (SQLException ex) {
            System.err.println("Erro:" + ex);
        }
        resposta[0] = "Mensagem enviada";
        clrqst.setResponse(resposta);
        clrqst.setStage(3);
        updateRequest(clrqst);
        return clrqst;
    }
    
    public ClientRequest apagaProjecto(ClientRequest clrqst) throws RemoteException{
        
        System.out.println("[RMI Server] Função <apagaProjecto> chamada!");
        
        int valor_a_devolver = 0;
        int userID = 0;
        int projectID = (int) clrqst.getRequest()[1];
        int rewardID = 0;
        clrqst.setStage(2);
        myRequests.add(clrqst);
        
        try {
            query = "SELECT valor, id_user from pledge_user WHERE id_projecto = "+projectID;
            request = connection.createStatement();
            rs = request.executeQuery(query);
            while (rs.next()){
                valor_a_devolver = rs.getInt("valor");
                userID = rs.getInt("id_user");
                
                try{
                    query = "UPDATE utilizador SET saldo = saldo+"+valor_a_devolver+" WHERE id = "+userID;
                    request = connection.createStatement();
                    request.executeUpdate(query);
                } catch(SQLException ex){
                    System.out.println("SQLException 475: "+ex);
                }
                
            }
            
        } catch (SQLException ex) {
            System.err.println("SQLException 489: " + ex);
        }
        try{
            query = "DELETE FROM pledge_user WHERE id_projecto = "+projectID;
            request = connection.createStatement();
            request.executeUpdate(query);
            
        } catch (SQLException ex) {
            System.err.println("Erro:" + ex);
        }
        
        try{
            query = "DELETE FROM projecto WHERE id =" + projectID;
            request = connection.createStatement();
            request.executeUpdate(query);
            
        }catch (SQLException ex) {
            System.err.println("SQLException 418:" + ex);
        }
        try{
            query = "DELETE FROM projecto_user WHERE id_projecto =" + projectID;
            request = connection.createStatement();
            request.executeUpdate(query);
        } catch (SQLException ex) {
            System.err.println("SQLException 426:" + ex);
        }
        try{
            query = "DELETE FROM product_type WHERE id_projecto= "+projectID;
            request = connection.createStatement();
            request.executeUpdate(query);
            
        } catch (SQLException ex) {
            System.err.println("SQLException 434:" + ex);
        }
        try{
            query = "DELETE FROM niveis_extra WHERE id_projecto = "+projectID;
            request = connection.createStatement();
            request.executeUpdate(query);
            
        } catch (SQLException ex) {
            System.err.println("SQLException 442:" + ex);
        }
        try{
            query = "DELETE FROM mensagem WHERE id_projecto = "+projectID;
            request = connection.createStatement();
            request.executeUpdate(query);
            
        } catch (SQLException ex) {
            System.err.println("SQLException 450:" + ex);
        }
        
        try{
            query = "DELETE FROM niveis_extra WHERE id_projecto = "+projectID;
            request = connection.createStatement();
            request.executeUpdate(query);
            
        } catch (SQLException ex) {
            System.err.println("SQLException 459:" + ex);
        }
        
        
        try{
            query = "SELECT id FROM recompensas WHERE id_projecto = "+projectID;
            request = connection.createStatement();
            rs = request.executeQuery(query);
            while (rs.next()){
                rewardID = rs.getInt("id");
                try{
                    query = "DELETE FROM recompensa_user WHERE id_recompensa= "+rewardID;
                    request = connection.createStatement();
                    request.executeUpdate(query);
                } catch(SQLException ex){
                    System.err.print("SQLException 503: "+ex);
                }
                
            }
            query = "DELETE FROM recompensas WHERE id_projecto ="+projectID;
            request = connection.createStatement();
            request.executeUpdate(query);
        } catch(SQLException ex){
            System.err.print("SQLException 511: "+ex);
        }
        
        try{
            query = "DELETE FROM niveis_extra WHERE id_projecto = "+projectID;
            request = connection.createStatement();
            request.executeUpdate(query);
            
        } catch (SQLException ex) {
            System.err.println("Erro:" + ex);
        }
        
        
        resposta[0] = "projecto apagado";
        clrqst.setResponse(resposta);
        clrqst.setStage(3);
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
    
    public ClientRequest voteForProduct(ClientRequest clrqst) throws RemoteException{
        
        myRequests.add(clrqst);
        String product_type = (String)clrqst.getRequest()[1];
        
        try{
            query = "UPDATE product_type SET contador = contador +1 WHERE descricao = '"+product_type+"'";
            preparedstatement = connection.prepareStatement(query);
            preparedstatement.executeUpdate();
        } catch(SQLException ex){
            System.err.print("SQLException 395: "+ex);
        }
        clrqst.setResponse(resposta);
        updateRequest(clrqst);
        return clrqst;
    }

    public void terminaProjecto() throws RemoteException {
        Date date = new Date();
        System.out.println(date.toString());


        Date dataLimite;
        int projectID;
        try {

            query = "SELECT id,data_limite, status FROM projecto";
            request = connection.createStatement();
            rs = request.executeQuery(query);

           while (rs.next()) {
                dataLimite = rs.getDate("data_limite");
                if (dataLimite.before(date) && rs.getBoolean("status")) {
                    projectID=rs.getInt("id");
                    System.out.println("Vou alterar no projecto com id "+projectID);
                    query = "UPDATE projecto SET status=? WHERE id=?";
                    preparedstatement = connection.prepareStatement(query);
                    preparedstatement.setBoolean(1, false);
                    preparedstatement.setInt(2, projectID);
                    preparedstatement.executeUpdate();
                    System.out.println("Executei a query");
                }

            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

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

    public ClientRequest seeLastRequest(ClientRequest clrqst) throws RemoteException {
        System.out.println("Funçao <seeLastRequest> chamada!");
        ClientRequest auxRequest = null;

        for (int i = 0; i < myRequests.size(); i++) {
            if (myRequests.get(i).getRequestID().equals(clrqst.getRequestID())) {
                if (myRequests.get(i).getTimestamp().equals(clrqst.getTimestamp())) {
                    auxRequest = myRequests.get(i);
                    break;
                }
            }
        }

        if (auxRequest == null) {
            //não entra na puta do switch
            switch ((String) clrqst.getRequest()[0]) {
                case "log":
                    clrqst = verificaLogIn(auxRequest);
                    break;
                case "new":
                    clrqst = novoUtilizador(auxRequest);
                    break;
                case "new_project":
                    clrqst = novoProjecto(auxRequest);
                    break;
                case "seesal":
                    clrqst = getUserSaldo(auxRequest);
                    System.out.println("CARALHO!");
                    break;
                case "list_actual_projects":
                    clrqst = getActualProjects(auxRequest);
                    break;
                case "list_old_projects":
                    clrqst = getActualProjects(auxRequest);
                    break;
            }
        } else if (auxRequest.getStage().equals("rmiout")) {
            clrqst = auxRequest;
        } else {
            //acho que não chega aqui
            System.out.println("Entrei dentro do else");
            switch ((String) auxRequest.getRequest()[0]) {
                case "log":
                    clrqst = verificaLogIn(auxRequest);
                    break;
                case "new":
                    clrqst = novoUtilizador(auxRequest);
                    break;
                case "new_project":
                    clrqst = novoProjecto(auxRequest);
                    break;
                case "seesal":
                    clrqst = getUserSaldo(auxRequest);
                    break;
                case "list_actual_projects":
                    clrqst = getActualProjects(auxRequest);
                    break;
                case "list_old_projects":
                    clrqst = getActualProjects(auxRequest);
                    break;
            }

        }
        System.out.println("resposta->" + clrqst);
        return clrqst;
    }

    public ClientRequest getProjectDetails(ClientRequest clrqst) throws RemoteException {
        System.out.println("[RMI Server] Função <getProjectDetails chamada!");

        int i = 0, j = 0, pointer = 0;
        String[] project_details = new String[2000];
        Object[] objecto = clrqst.getRequest();

        int id_projecto = (int) objecto[1];
        int num_recompensas = 0, num_niveis_extra = 0, num_product_type = 0;

        try {
            query = "SELECT titulo, descricao, valorpretendido, valoractual, data_limite "
                    + "FROM projecto "
                    + "WHERE projecto.id =" + id_projecto + " "
                    + "AND status =TRUE";

            request = connection.createStatement();
            rs = request.executeQuery(query);
            if (rs.next()) {
                System.out.println("Devia funcar");
                project_details[j] = rs.getString("titulo");
                j++;
                project_details[j] = rs.getString("descricao");
                j++;
                project_details[j] = "" + rs.getInt("valorpretendido");
                j++;
                project_details[j] = "" + rs.getInt("valoractual");
                j++;
                project_details[j] = "" + rs.getDate("data_limite");
                j++;

                try {
                    query = "SELECT titulo, valor FROM recompensas WHERE id_projecto =" + id_projecto;
                    request = connection.createStatement();
                    rs = request.executeQuery(query);
                    pointer = j;
                    while (rs.next()) {
                        num_recompensas++;
                        j++;
                        project_details[j] = rs.getString("titulo");
                        num_recompensas++;
                        j++;
                        project_details[j] = "" + rs.getInt("valor");
                    }

                } catch (SQLException ex) {
                    System.err.println("SQLException:" + ex);
                }
                project_details[pointer] = "" + num_recompensas;
                j++;
                pointer = j;

                try {

                    query = "SELECT descricao, valor FROM niveis_extra WHERE id_projecto =" + id_projecto;
                    request = connection.createStatement();
                    rs = request.executeQuery(query);
                    while (rs.next()) {
                        num_niveis_extra++;
                        j++;
                        project_details[j] = rs.getString("descricao");
                        num_niveis_extra++;
                        j++;
                        project_details[j] = "" + rs.getInt("valor");

                    }

                } catch (SQLException ex) {
                    System.err.println("SQLException:" + ex);
                }
                project_details[pointer] = "" + num_niveis_extra;
                j++;
                pointer = j;

                try {
                    query = "SELECT descricao, contador FROM product_type WHERE id_projecto=" + id_projecto;
                    request = connection.createStatement();
                    rs = request.executeQuery(query);
                    while (rs.next()) {
                        j++;
                        num_product_type++;
                        project_details[j] = rs.getString("descricao");
                        j++;
                        num_product_type++;
                        project_details[j] = "" + rs.getInt("contador");
                    }

                } catch (SQLException ex) {
                    System.err.println("SQLException:" + ex);
                }
                project_details[pointer] = "" + num_product_type;
                resposta[0] = project_details;
                resposta[1] = j;

            } else {
                System.out.println("No Project Found!");
                project_details[0] = "no_project_to_show";
                resposta[0] = project_details;
            }

        } catch (SQLException ex) {
            System.err.println("SQLException:" + ex);

        }
        clrqst.setResponse(resposta);
        clrqst.setStage(3);
        return clrqst;
    }
    
    public ClientRequest pledgeToProject(ClientRequest clrqst) throws RemoteException{
        System.out.println("[RMI Server] Função <pledgeToProject> chamada!");
        Object[] objecto = clrqst.getRequest();
        int[] how_much__to_who = (int[])(objecto[1]);
        int how_much = how_much__to_who[0];
        int to_who = how_much__to_who[1];
        
        int user_id = (int)objecto[0];
        int novo_valor_actual = 0, novo_saldo = 0;
        ArrayList <String> product_type = new ArrayList();
        int saldo = 0, i = 0;
        String recompensas = new String();
        int id_recompensa = 0;
        
        try{
            query = "SELECT saldo "
                    + "FROM utilizador "
                    + "WHERE id ="+user_id;
            request = connection.createStatement();
            rs = request.executeQuery(query);
            if(rs.next()){
                saldo = rs.getInt("saldo");
            }
            if(saldo >= how_much){
                
                try{
                    System.out.println(to_who);
                    query = "SELECT valoractual FROM projecto WHERE id = "+to_who;
                    request = connection.createStatement();
                    rs = request.executeQuery(query);
                    rs.next();
                    novo_valor_actual = rs.getInt("valoractual");
                    novo_valor_actual = novo_valor_actual + how_much;
                } catch(SQLException ex) {
                    System.err.println("SQLException 622: "+ex);
                }
                try{
                    query = "UPDATE projecto SET valoractual = "+novo_valor_actual+" WHERE id = "+to_who;
                    request = connection.createStatement();
                    request.executeUpdate(query);
                    
                } catch (SQLException ex) {
                System.err.println("SQLException: How much " + ex);
                }
                
                try{
                    query = "SELECT saldo FROM utilizador WHERE id = "+user_id;
                    request = connection.createStatement();
                    rs = request.executeQuery(query);
                    rs.next();
                    novo_saldo = rs.getInt("saldo")-how_much;
                } catch (SQLException ex){
                    System.out.println("SQLException 630: "+ex);
                }
                try{
                    query = "UPDATE utilizador SET saldo = "+novo_saldo+" WHERE id = "+user_id;
                    request = connection.createStatement();
                    request.executeUpdate(query);
                    
                } catch (SQLException ex) {
                    System.err.println("SQLException 640 :" + ex);
                }
                
                
                try{
                    query = "INSERT INTO pledge_user (id_user, id_projecto, valor) VALUES ("+user_id+", "+to_who+", "+how_much+")";
                    request = connection.createStatement();
                    request.executeUpdate(query);
                    
                }catch (SQLException ex) {
                    System.err.println("SQLException 650 :" + ex);
                }
                
                
                try{
                    query = "SELECT * "
                            + "FROM recompensas "
                            + "WHERE id_projecto ="+to_who+" "
                            + "AND valor <="+how_much+" "
                            + "ORDER BY valor DESC";
                    request = connection.createStatement();
                    rs = request.executeQuery(query);
                    if (rs.next()){
                        i++;
                        recompensas = rs.getString("titulo");
                        id_recompensa = rs.getInt("id");
                        resposta[2] = i;
                        resposta[3] = recompensas;
                        
                        try{
                           
                            query = "INSERT INTO recompensa_user (id_recompensa, id_user) VALUES (CAST(? AS integer),CAST(? AS integer))";
                            preparedstatement = connection.prepareStatement(query);
                            preparedstatement.setInt(1, (int)(id_recompensa));
                            preparedstatement.setInt(2, (int)(user_id));
                            preparedstatement.executeUpdate();
                            
                        } catch (SQLException ex){
                            System.out.println("SQLException: 665 "+ex);
                        }
    
                    }
                    else{
                        resposta[2] = i;
                        resposta[3] = "No reward";
                    }
               
                } catch (SQLException ex) {
                    System.err.println("SQLException:" + ex);
                }
                
                
                
                try {
                    query = "SELECT descricao FROM product_type WHERE id_projecto ="+to_who;
                    request = connection.createStatement();
                    rs = request.executeQuery(query);
                    while(rs.next()){
                        product_type.add(rs.getString("descricao"));
                    }
                    
                } catch (SQLException ex) {
                     System.err.println("SQLException 784:" + ex);
                }
                
                resposta[0] = "pledged";
                resposta[1] = saldo-how_much;
                resposta[4] = product_type;
            }
            else{
                System.out.println("sem saldo");
                resposta[0] = "Sem saldo";
                resposta[1]= saldo;
                resposta[4] = product_type;
            }
        } catch (SQLException ex) {
            System.err.println("SQLException 791:" + ex);
        }
        clrqst.setResponse(resposta);
        clrqst.setStage(3);
        return clrqst;
    }
    
    public ClientRequest addAdminToProject(ClientRequest clrqst) throws RemoteException{
        
        System.out.println("[RMI Server] Função <addAdminToProject> chamada!");
        Object[] objecto = clrqst.getRequest();
        String user = (String)(objecto[1]);
        int id_projecto = (int)(objecto[2]);
        int id_user = 0;
        
        try{
            query = "SELECT id FROM utilizador WHERE username= '"+user+"'";
            request = connection.createStatement();
            rs = request.executeQuery(query);
            if (rs.next()){
                id_user = rs.getInt("id");
                try{
                    query = "INSERT INTO projecto_user (id_projecto, id_user) VALUES ("+id_projecto+","+id_user+")";
                    preparedstatement = connection.prepareStatement(query);
 
                    preparedstatement.executeUpdate();
                    resposta[2] ="done";
                    
                } catch (SQLException ex) {
                    System.err.println("SQLException:" + ex);
                }
            } else resposta[2] ="no_user";
            
            
        } catch (SQLException ex) {
            System.err.println("SQLException:" + ex);
        }
        if (resposta[2] == null) resposta[2] = "bode";
        clrqst.setResponse(resposta);
        clrqst.setStage(3);
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

        //cronoThread;
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
