package rmiServer;

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

  import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.rmi.*;
  import java.rmi.registry.LocateRegistry;
  import java.rmi.server.UnicastRemoteObject;
  import java.sql.*;
  import java.util.*;
  import java.util.logging.Level;
  import java.util.logging.Logger;


import java.util.Date;

  public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

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

      public Object[] resposta = new Object[5]; //alterei
      private ArrayList<ClientRequest> myRequests = new ArrayList<ClientRequest>();

      Scanner sc = new Scanner(System.in);
      Connection connection;
      PreparedStatement preparedstatement = null;
      Statement request = null;
      ResultSet rs = null;
      String query;
      ResultSet returnBD = null;
      ClientRequest requestCheck = null;

      protected RMIServer() throws RemoteException {
          super();
      }

      public ClientRequest verificaLogIn(ClientRequest clrqst) throws RemoteException {

          Object[] objecto = clrqst.getRequest();
          String[] person = (String[]) objecto[1];
          String user = person[0];
          String password = person[1];
          int id = -1;

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

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

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

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

          System.out.println("[RMI Server] Funcao <novoProjecto> chamada!");

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

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
                      query = "INSERT INTO product_type (descricao, id_projecto, contador) VALUES (?,?,?)";
                      preparedstatement = connection.prepareStatement(query);
                      preparedstatement.setString(1, projectInfo[4 + j]);
                      preparedstatement.setInt(2, id);
                      preparedstatement.setInt(3, 0);
                      preparedstatement.executeUpdate();
                  }

              }

              resposta[0] = "infosave";
              resposta[1] = id;
              System.out.println("\n\nO ID E ESTE: " + id + "    " + resposta[1]);
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

      public ClientRequest donateReward(ClientRequest clrqst) throws RemoteException {

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

          clrqst.setStage(2);
          myRequests.add(clrqst);
          int id_projecto = (int) clrqst.getRequest()[1];
          String username = (String) clrqst.getRequest()[2];
          String recompensa = (String) clrqst.getRequest()[3];

          int id_recompensa = 0;
          int id_user = 0;

          try {
              query = "SELECT id FROM utilizador WHERE username = '" + username + "'";
              request = connection.createStatement();
              rs = request.executeQuery(query);
              if (rs.next()) {
                  id_user = rs.getInt("id");

                  if (recompensa.equals("listar_rec_flag")) {
                      try {

                          query = "UPDATE recompensa_user SET id_user= " + id_user + " WHERE id_recompensa= " + id_projecto;
                          preparedstatement = connection.prepareStatement(query);
                          preparedstatement.executeUpdate();
                          resposta[0] = "success";

                      } catch (SQLException ex) {
                          System.err.print("SQLException 297: " + ex);
                      }

                  } else {
                      try {
                          query = "SELECT id FROM recompensas WHERE titulo = '" + recompensa + "'";
                          request = connection.createStatement();
                          rs = request.executeQuery(query);
                          if (rs.next()) {
                              id_recompensa = rs.getInt("id");

                              try {

                                  query = "UPDATE recompensa_user SET id_user= " + id_user + " WHERE id_recompensa= " + id_recompensa;
                                  preparedstatement = connection.prepareStatement(query);
                                  preparedstatement.executeUpdate();

                                  resposta[0] = "success";
                              } catch (SQLException ex) {
                                  System.err.print("SQLException 289: " + ex);
                              }

                          } else {
                              resposta[0] = "no_reward";
                          }

                      } catch (SQLException ex) {
                          System.err.print("SQLException 326: " + ex);
                      }

                  }
              } else {
                  resposta[0] = "no_user_found";
              }

          } catch (SQLException ex) {
              System.err.print("SQLException 283: " + ex);
          }

          clrqst.setResponse(resposta);
          clrqst.setStage(3);
          updateRequest(clrqst);
          return clrqst;

      }

      public ClientRequest listarRecompensas(ClientRequest clrqst) throws RemoteException {

          System.out.println("Função <listarRecompensas> chamada!");
          int userID = (int) clrqst.getRequest()[1];
          int myProject;
          boolean status;
          int rewardID;
          int projectID = 0;
          ArrayList<String> definitivas = new ArrayList();
          ArrayList<String> temporarias = new ArrayList();
          ArrayList<String> projecto = new ArrayList();
          int flag = (int) clrqst.getRequest()[2];

          if(clrqst.getRequest()[0].equals("listar_recompensas")){
            myProject=0;
          }
          else{
            myProject = (int) clrqst.getRequest()[0];
          }

          if (flag == 1) {
              try {
                  if(myProject != 0){
                    query = "SELECT id_projecto FROM projecto_user WHERE id_projecto = " + myProject;
                  }
                  else{
                    query = "SELECT id_projecto FROM projecto_user WHERE id_user = " + userID;
                  }
                  preparedstatement = connection.prepareStatement(query);
                  rs = preparedstatement.executeQuery();
                  while (rs.next()) {
                      projectID = rs.getInt("id_projecto");
                      query = "SELECT titulo, id, valor FROM recompensas WHERE id_projecto = " + projectID;
                      preparedstatement = connection.prepareStatement(query);
                      ResultSet result = preparedstatement.executeQuery();
                      while (result.next()) {
                          projecto.add("ID: "+result.getInt("id")+ "  Titulo: " + result.getString("titulo") + "  Montante a doar: "+result.getInt("valor") +"\nPertencente ao Projecto ID: " + projectID);
                      }

                  }

              } catch (SQLException ex) {
                  System.err.print("SQLException 383: " + ex);
              }
          } else if (flag == 0) {

              try {
                  query = "SELECT id_recompensa, status FROM recompensa_user WHERE id_user = " + userID;
                  preparedstatement = connection.prepareStatement(query);
                  rs = preparedstatement.executeQuery();
                  while (rs.next()) {
                      rewardID = rs.getInt("id_recompensa");
                      status = rs.getBoolean("status");
                      if (status == true) {
                          try {
                              query = "SELECT titulo, id_projecto FROM recompensas WHERE id= " + rewardID;
                              preparedstatement = connection.prepareStatement(query);
                              ResultSet result = preparedstatement.executeQuery();
                              while (result.next()) {
                                  definitivas.add(rewardID + " " + result.getString("titulo") + "\tID Projecto: " + result.getInt("id_projecto"));

                              }
                          } catch (SQLException ex) {
                              System.err.print("SQLException 356: " + ex);
                          }
                      } else {
                          try {
                              query = "SELECT titulo, id_projecto FROM recompensas WHERE id =" + rewardID;
                              preparedstatement = connection.prepareStatement(query);
                              rs = preparedstatement.executeQuery();
                              while (rs.next()) {
                                  temporarias.add(rewardID + " " + rs.getString("titulo") + "\tID Projecto: " + rs.getInt("id_projecto"));

                              }
                          } catch (SQLException ex) {
                              System.err.print("SQLException 370: " + ex);
                          }
                      }
                  }

              } catch (SQLException ex) {
                  System.err.print("SQLException 350: " + ex);
              }
          } else{

              try {
                  query = "SELECT id_projecto FROM projecto_user WHERE id_user = " + userID;
                  preparedstatement = connection.prepareStatement(query);
                  rs = preparedstatement.executeQuery();
                  while (rs.next()) {
                      projectID = rs.getInt("id_projecto");
                      query = "SELECT descricao, id, valor FROM niveis_extra WHERE id_projecto = " + projectID;
                      preparedstatement = connection.prepareStatement(query);
                      ResultSet result = preparedstatement.executeQuery();
                      while (result.next()) {
                          projecto.add("ID: "+result.getInt("id")+ "  Titulo: " + result.getString("descricao") +"  Montante a alcançar: "+result.getInt("valor")+ "\nPertencente ao Projecto ID: " + projectID);
                      }

                  }

              } catch (SQLException ex) {
                  System.err.print("SQLException 383: " + ex);
              }

          }

          resposta[1] = definitivas;
          resposta[2] = temporarias;
          resposta[3] = projecto;
          clrqst.setResponse(resposta);
          clrqst.setStage(3);

          return clrqst;
      }


      public ClientRequest addReward(ClientRequest clrqst) throws RemoteException{
          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }
          clrqst.setStage(2);
          myRequests.add(clrqst);
          int userID = Integer.parseInt((String) clrqst.getRequest()[0]);
          String[] projectInfo = (String[]) clrqst.getRequest()[1];

          int valor = Integer.parseInt(projectInfo[0]);
          int projectID = Integer.parseInt(projectInfo[1]);
          String titulo = projectInfo[2];
          int flag = Integer.parseInt(projectInfo[3]);
          /*
          int projectID = (int)clrqst.getRequest()[1];
          String titulo = (String)clrqst.getRequest()[2];
          int valor = (int)clrqst.getRequest()[3];
          int flag = (int) clrqst.getRequest()[4];
          */
          int is_it_mine = 0;
          if (flag == 0){
              try{
                  query = "SELECT id_projecto FROM projecto_user WHERE id_user = "+userID;
                  preparedstatement = connection.prepareStatement(query);
                  rs = preparedstatement.executeQuery();
                  while (rs.next()){
                      if (projectID == rs.getInt("id_projecto")){
                          is_it_mine = 1;
                      }
                  }
              } catch(SQLException ex){
                  System.err.print("SQLException 478: "+ex);
              }

              if(is_it_mine == 1){
                  try{
                      query = "INSERT INTO recompensas (titulo, id_projecto,valor) VALUES ('"+titulo+"', "+projectID+", "+valor+")";
                      preparedstatement = connection.prepareStatement(query);
                      preparedstatement.executeUpdate();
                      resposta[1] = is_it_mine;
                      resposta[0] = "infosave";

                  } catch(SQLException ex){
                      System.err.print("SQLException 464: "+ex);
                  }
              }
              else{
                  is_it_mine = 0;
                  resposta[1] = is_it_mine;
                  resposta[0] = "infosave";
              }

          }
          else{
              try{
                  query = "SELECT id_projecto FROM projecto_user WHERE id_user = "+userID;
                  preparedstatement = connection.prepareStatement(query);
                  rs = preparedstatement.executeQuery();
                  while (rs.next()){
                      if (projectID == rs.getInt("id_projecto")){
                          is_it_mine = 1;
                      }
                  }
              } catch(SQLException ex){
                  System.err.print("SQLException 478: "+ex);
              }
              if(is_it_mine == 1){
                  try{
                      query = "INSERT INTO niveis_extra (descricao, id_projecto, valor) VALUES ('"+titulo+"', "+projectID+", "+valor+")";
                      preparedstatement = connection.prepareStatement(query);
                      preparedstatement.executeUpdate();
                      resposta[1] = is_it_mine;
                      resposta[0] = "infosave";

                  } catch(SQLException ex){
                      System.err.print("SQLException 464: "+ex);
                  }
              }
              else{
                  is_it_mine = 0;
                  resposta[1] = is_it_mine;
                  resposta[0] = "infosave";
              }
          }

          //resposta[0] = "infosave";

          clrqst.setResponse(resposta);
          clrqst.setStage(3);

          updateRequest(clrqst);
          return clrqst;


      }

      public ClientRequest deleteReward(ClientRequest clrqst) throws RemoteException{

          System.out.println("Função <deleteReward> chamada!");
          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

          //Flag - 0 delete recompensa
          //       1 delete nivel_extra

          clrqst.setStage(2);
          myRequests.add(clrqst);

          int rewardID = (int)clrqst.getRequest()[1];
          int flag = (int)clrqst.getRequest()[2];
          int userID = (int)clrqst.getRequest()[3];

          if (flag == 0){
              try{
                  query = "SELECT id_projecto FROM recompensas WHERE id = "+rewardID;
                  preparedstatement = connection.prepareStatement(query);
                  rs = preparedstatement.executeQuery();
                  if (rs.next()){
                      query = "SELECT id_user FROM projecto_user WHERE id_projecto = "+rs.getInt("id_projecto");
                      preparedstatement = connection.prepareStatement(query);
                      ResultSet result = preparedstatement.executeQuery();
                      if (result.next()){
                          query = "DELETE FROM recompensas WHERE id = "+rewardID;
                          preparedstatement = connection.prepareStatement(query);
                          preparedstatement.executeUpdate();
                      }
                      else{
                          resposta[1] = "not_yours";
                      }
                  }
                  else{
                      resposta[1] = "not_yours";
                  }
              } catch(SQLException ex){
                  System.err.print("SQLException 598: "+ex);
              }
          }
          else{
              try{
                  query = "SELECT id_projecto FROM niveis_extra WHERE id = "+rewardID;
                  preparedstatement = connection.prepareStatement(query);
                  rs = preparedstatement.executeQuery();
                  if (rs.next()){
                      query = "SELECT id_user FROM projecto_user WHERE id_projecto = "+rs.getInt("id_projecto");
                      preparedstatement = connection.prepareStatement(query);
                      ResultSet result = preparedstatement.executeQuery();
                      if (result.next()){
                          query = "DELETE FROM niveis_extra WHERE id = "+rewardID;
                          preparedstatement = connection.prepareStatement(query);
                          preparedstatement.executeUpdate();

                      }
                      else{
                          resposta[1] = "not_yours";
                      }
                  }
                  else{
                      resposta[1] = "not_yours";
                  }
              } catch(SQLException ex){
                  System.err.print("SQLException 598: "+ex);
              }
          }


          clrqst.setResponse(resposta);
          clrqst.setStage(3);

          updateRequest(clrqst);
          return clrqst;

      }

      public ClientRequest getUserProjects(ClientRequest clrqst) throws RemoteException{

          requestCheck=checkRequest(clrqst);

          if(requestCheck!=null){

              return requestCheck;
          }

          System.out.println("[RMI Server] Função <getUserProjects> chamada!");
          ArrayList<Integer> lista_ids = new ArrayList<Integer>();
          ArrayList<String> lista_projectos = new ArrayList<String>();

          int project_id = 0;
          int userID = (int) clrqst.getRequest()[1];

          clrqst.setStage(2);
          myRequests.add(clrqst);

          try {
              query = "SELECT id_projecto FROM projecto_user WHERE id_user =" + userID;
              request = connection.createStatement();
              rs = request.executeQuery(query);
              while (rs.next()) {

                  lista_ids.add(rs.getInt("id_projecto"));
              }
              resposta[0] = lista_ids;
          } catch (SQLException ex) {
              System.err.println("Erro 1:" + ex);
          }

          try {
              Iterator<Integer> it = lista_ids.iterator();
              while (it.hasNext()) {
                  project_id = it.next();
                  query = "SELECT titulo FROM projecto WHERE id=" + project_id;
                  request = connection.createStatement();
                  rs = request.executeQuery(query);
                  if (rs.next()) {
                      lista_projectos.add(rs.getString("titulo"));
                  }
              }

              resposta[1] = lista_projectos;
          } catch (SQLException ex) {
              System.err.println("Erro 2:" + ex);
          }
          clrqst.setResponse(resposta);
          clrqst.setStage(3);

          updateRequest(clrqst);

          return clrqst;

      }

      public ClientRequest veResposta(ClientRequest clrqst) throws RemoteException {
          int userID = (int) clrqst.getRequest()[1];
          System.out.println("[RMI Server] Função <veResposta> chamada!");
          ArrayList<ArrayList<String>> pr = new ArrayList<ArrayList<String>>(0);
          ArrayList<String> temp1 = new ArrayList<String>(0);

          try {

              query = "SELECT id_projecto, pergunta, resposta FROM mensagem WHERE id_user_envia = " + userID + " AND resposta IS NOT NULL";
              request = connection.createStatement();
              rs = request.executeQuery(query);

              while (rs.next()) {
                  temp1 = new ArrayList<String>(0);
                  temp1.add(rs.getString("id_projecto"));
                  temp1.add(rs.getString("pergunta"));
                  temp1.add(rs.getString("resposta"));
                  pr.add(temp1);
              }
          } catch (SQLException ex) {
              System.err.println("Erro:" + ex);

          }
          
          resposta[1] = "Checked!";

          resposta[0] = pr;
          clrqst.setResponse(resposta);
          clrqst.setStage(3);
          return clrqst;

      }

      public ClientRequest caixaCorreio(ClientRequest clrqst) throws RemoteException {
          System.out.println("[RMI Server] Função <caixaCorreio> chamada!");
          ArrayList<ArrayList<Integer>> listaPerguntas = new ArrayList<>();
          ArrayList<Integer> lista_projectos = new ArrayList<Integer>(0);
          ArrayList<ArrayList<String>> recolhePerguntas = new ArrayList<>();

          ArrayList<String> lista_perguntas = new ArrayList<String>(0);

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

          int userID = (int) clrqst.getRequest()[0];

          clrqst.setStage(2);
          //recolhe todos os projectos do user
          try {
              query = "SELECT id_projecto FROM projecto_user WHERE id_user =" + userID;
              request = connection.createStatement();
              rs = request.executeQuery(query);

              while (rs.next()) {

                  lista_projectos.add(rs.getInt("id_projecto"));
              }

          } catch (SQLException ex) {
              System.err.println("Erro336 :" + ex);

          }
          int tamanho = lista_projectos.size();

          int i;

          //recolhe todas as perguntas associadas a cada projecto
          try {
              for (i = 0; i < tamanho; i++) {
                  ArrayList<Integer> temp = new ArrayList();
                  lista_perguntas = new ArrayList<String>(0);
                  query = "SELECT id, id_projecto, pergunta FROM mensagem WHERE id_projecto = " + lista_projectos.get(i) + " AND resposta IS NULL";
                  //adicionar o indice do projecto
                  request = connection.createStatement();

                  rs = request.executeQuery(query);
                  if (rs.next()) {
                      temp.add(rs.getInt("id_projecto"));
                      temp.add(rs.getInt("id"));
                      lista_perguntas.add(" ");
                      lista_perguntas.add(rs.getString("pergunta"));
                  }
                  while (rs.next()) {
                      temp.add(rs.getInt("id"));
                      lista_perguntas.add(rs.getString("pergunta"));
                  }
                  recolhePerguntas.add(lista_perguntas);
                  listaPerguntas.add(temp);
              }
          } catch (SQLException ex) {
              System.err.println("Erro:" + ex);

          }
          resposta[0] = listaPerguntas;
          resposta[1] = recolhePerguntas;
          clrqst.setResponse(resposta);
          clrqst.setStage(3);
          return clrqst;
      }

      public ClientRequest respMensagem(ClientRequest clrqst) throws RemoteException {
          System.out.println("[RMI Server] Função <respMensagem> chamada!");
          // Flag fim de projecto: 1 - Fim de projecto
          //                       0 - Mensagem normal
          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

          int flag = (int) clrqst.getRequest()[0];
          int questionID = (int) clrqst.getRequest()[1];
          String mensagem = (String) clrqst.getRequest()[2];
          clrqst.setStage(2);
          myRequests.add(clrqst);

          if (flag == 1){
              try{
                  query = "INSERT INTO mensagem (id_user_envia,id_projecto, pergunta, resposta) VALUES (?,?,?,?)";
                  preparedstatement = connection.prepareStatement(query);
                  preparedstatement.setInt(1, questionID);
                  preparedstatement.setInt(2, -1);
                  preparedstatement.setString(3, "mensagem fim de projecto");
                  preparedstatement.setString(4, mensagem);
                  preparedstatement.executeUpdate();
              } catch(SQLException ex)
              {
                  System.err.print("SQLException 835: "+ex);
              }

          }
          else{
              try {
                  query = "UPDATE mensagem SET resposta = ? WHERE id = ?";
                  preparedstatement = connection.prepareStatement(query);
                  preparedstatement.setInt(2, questionID);
                  preparedstatement.setString(1, mensagem);
                  preparedstatement.executeUpdate();

              } catch (SQLException ex) {
                  System.err.println("Erro:" + ex);
              }

              resposta[0] = "Pergunta respondida!";
              clrqst.setResponse(resposta);
              clrqst.setStage(3);
              updateRequest(clrqst);

              }

          return clrqst;
      }

      public ClientRequest enviaMensagem(ClientRequest clrqst) throws RemoteException {
          System.out.println("[RMI Server] Função <enviaMensagem> chamada!");

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

          int userID = (int) clrqst.getRequest()[0];
          int projectID = (int) clrqst.getRequest()[1];
          String mensagem = (String) clrqst.getRequest()[2];
          clrqst.setStage(2);
          myRequests.add(clrqst);
          try {
              query = "SELECT id FROM projecto WHERE id = ? AND status = TRUE";
              preparedstatement = connection.prepareStatement(query);
              preparedstatement.setInt(1, projectID);
              rs = preparedstatement.executeQuery();
              if (rs.next()){
                  try {
                      query = "INSERT INTO mensagem(id_user_envia, id_projecto,pergunta) VALUES (?,?,?)";
                      preparedstatement = connection.prepareStatement(query);
                      preparedstatement.setInt(1, userID);
                      preparedstatement.setInt(2, projectID);
                      preparedstatement.setString(3, mensagem);
                      preparedstatement.executeUpdate();
                      resposta[0] = "Mensagem enviada";
                  } catch (SQLException ex) {
                      System.err.println("Erro:" + ex);
                  }
              }
              else{
                  resposta[0] = "Nao existe";
              }
          } catch (SQLException ex) {
              System.err.println("Erro:" + ex);
          }


          clrqst.setResponse(resposta);
          clrqst.setStage(3);
          updateRequest(clrqst);
          return clrqst;
      }

      public ClientRequest apagaProjecto(ClientRequest clrqst) throws RemoteException {
          System.out.println("[RMI Server] Função <apagaProjecto> chamada!");

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

          int valor_a_devolver = 0;
          int userID = 0;
          int projectID = (int) clrqst.getRequest()[1];
          int rewardID = 0;
          clrqst.setStage(2);
          myRequests.add(clrqst);

          try {
              query = "SELECT valor, id_user from pledge_user WHERE id_projecto = " + projectID;
              request = connection.createStatement();
              rs = request.executeQuery(query);
              while (rs.next()) {
                  valor_a_devolver = rs.getInt("valor");
                  userID = rs.getInt("id_user");

                  try {
                      query = "UPDATE utilizador SET saldo = saldo+" + valor_a_devolver + " WHERE id = " + userID;
                      request = connection.createStatement();
                      request.executeUpdate(query);
                  } catch (SQLException ex) {
                      System.out.println("SQLException 475: " + ex);
                  }

              }

          } catch (SQLException ex) {
              System.err.println("SQLException 489: " + ex);
          }
          try {
              query = "DELETE FROM pledge_user WHERE id_projecto = " + projectID;
              request = connection.createStatement();
              request.executeUpdate(query);

          } catch (SQLException ex) {
              System.err.println("Erro:" + ex);
          }

          try {
              query = "SELECT id FROM recompensas WHERE id_projecto = " + projectID;

              request = connection.createStatement();
              rs = request.executeQuery(query);
              while (rs.next()) {
                  rewardID = rs.getInt("id");
                  try {
                      query = "DELETE FROM recompensa_user WHERE id_recompensa= " + rewardID;
                      request = connection.createStatement();
                      request.executeUpdate(query);
                  } catch (SQLException ex) {
                      System.err.print("SQLException 503: " + ex);
                  }

              }
              query = "DELETE FROM recompensas WHERE id_projecto =" + projectID;
              request = connection.createStatement();
              request.executeUpdate(query);
          } catch (SQLException ex) {
              System.err.print("SQLException 511: " + ex);
          }
          try {
              query = "DELETE FROM projecto_user WHERE id_projecto =" + projectID;
              request = connection.createStatement();
              request.executeUpdate(query);
          } catch (SQLException ex) {
              System.err.println("SQLException 426:" + ex);
          }
          try {
              query = "DELETE FROM product_type WHERE id_projecto= " + projectID;
              request = connection.createStatement();
              request.executeUpdate(query);

          } catch (SQLException ex) {
              System.err.println("SQLException 434:" + ex);
          }
          try {
              query = "DELETE FROM niveis_extra WHERE id_projecto = " + projectID;
              request = connection.createStatement();
              request.executeUpdate(query);

          } catch (SQLException ex) {
              System.err.println("SQLException 442:" + ex);
          }
          try {
              query = "DELETE FROM mensagem WHERE id_projecto = " + projectID;
              request = connection.createStatement();
              request.executeUpdate(query);

          } catch (SQLException ex) {
              System.err.println("SQLException 450:" + ex);
          }

          try {
              query = "DELETE FROM niveis_extra WHERE id_projecto = " + projectID;
              request = connection.createStatement();
              request.executeUpdate(query);

          } catch (SQLException ex) {
              System.err.println("SQLException 459:" + ex);
          }

          try {
              query = "DELETE FROM niveis_extra WHERE id_projecto = " + projectID;
              request = connection.createStatement();
              request.executeUpdate(query);

          } catch (SQLException ex) {
              System.err.println("Erro:" + ex);
          }
          try {
              query = "DELETE FROM projecto WHERE id =" + projectID;
              request = connection.createStatement();
              request.executeUpdate(query);

          } catch (SQLException ex) {
              System.err.println("SQLException 418:" + ex);
          }


          resposta[0] = "projecto apagado";
          clrqst.setResponse(resposta);
          clrqst.setStage(3);
          return clrqst;
      }

      public ClientRequest getUserSaldo(ClientRequest clrqst) throws RemoteException {
          System.out.println("[RMI Server] Função <getUserSaldo> chamada!");

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

          int userID = (int) clrqst.getRequest()[1];
          clrqst.setStage(2);
          myRequests.add(clrqst);

          System.out.println("My User ID->" + userID);

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

      public ClientRequest voteForProduct(ClientRequest clrqst) throws RemoteException {

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

          myRequests.add(clrqst);
          String product_type = (String) clrqst.getRequest()[1];

          try {
              query = "UPDATE product_type SET contador = contador +1 WHERE descricao = '" + product_type + "'";
              preparedstatement = connection.prepareStatement(query);
              preparedstatement.executeUpdate();
          } catch (SQLException ex) {
              System.err.print("SQLException 395: " + ex);
          }
          clrqst.setResponse(resposta);
          updateRequest(clrqst);
          return clrqst;
      }

      public void terminaProjecto() throws RemoteException {

          Date date = new Date();

          int valor_a_devolver = 0;
          int userID = 0;
          int valor_actual = 0, valor_pretendido = 0;
          int contador = -1;
          String descricao_produto = new String();
          String descricao_reward = new String();

          int rewardID = 0;
          Date dataLimite;
          int projectID = 0;
          ArrayList<String> fim_projecto = new ArrayList();

          try {

              query = "SELECT id, data_limite, valoractual, valorpretendido, status FROM projecto";
              request = connection.createStatement();
              rs = request.executeQuery(query);

              while (rs.next()) {
                  dataLimite = rs.getDate("data_limite");
                  valor_actual = rs.getInt("valoractual");
                  valor_pretendido = rs.getInt("valorpretendido");
                  if (dataLimite.before(date) && rs.getBoolean("status")) { //Se a data limite já tiver passado
                      projectID = rs.getInt("id");
                      query = "UPDATE projecto SET status=? WHERE id=?";   // Pomos os status a FALSE, para indicar que é um projecto inactivo
                      preparedstatement = connection.prepareStatement(query);
                      preparedstatement.setBoolean(1, false);
                      preparedstatement.setInt(2, projectID);
                      preparedstatement.executeUpdate();


                      if(valor_actual >= valor_pretendido){ //Se o projecto tiver angariado o dinheiro necessário, então foi concluído com sucesso
                          try{
                              query = "UPDATE projecto SET over = TRUE WHERE id="+projectID; //Então pomos over a TRUE. O over sinaliza se o projecto foi concluido com sucesso ou não
                              preparedstatement = connection.prepareStatement(query); // ESTE UPDATE VAI DESPOLETAR O TRIGGER DA BASE DE DADOS
                              // O TRIGGER VAI EXECUTAR TODAS AS QUERIES QUE ESTAO COMENTADAS DAQUI PARA BAIXO
                              preparedstatement.executeUpdate();
                          } catch (SQLException ex) {
                              System.err.print("SQLException 732: " + ex);
                          }
                          try{
                              query = "UPDATE niveis_extra SET status = TRUE WHERE valor < "+valor_actual; //Vamos ver a que recompensas extra é que chegámos

                              preparedstatement = connection.prepareStatement(query);
                              preparedstatement.executeUpdate();

                          } catch (SQLException ex) {
                              System.err.print("SQLException 748:  " + ex);
                          }

                          try {
                              query = "SELECT descricao FROM niveis_extra WHERE status = TRUE";   //Os alcançados são marcados a TRUE
                              preparedstatement = connection.prepareStatement(query);
                              ResultSet rs1 = preparedstatement.executeQuery();
                              if (rs1.next()) {
                                  fim_projecto.add("extra_levels");
                                  fim_projecto.add(rs1.getString("descricao"));
                                  while (rs1.next()) {
                                      fim_projecto.add(rs1.getString("descricao"));
                                  }
                              } else {
                                  fim_projecto.add("no_extra_levels");
                              }

                          } catch (SQLException ex) {
                              System.err.print("SQLException 748:  " + ex);
                          }
                          try {

                              query = "SELECT contador, descricao FROM product_type WHERE id_projecto = "+projectID; //Vamos ver se o projecto tinha algum tipo de produto entre o qual escolher
                              preparedstatement = connection.prepareStatement(query);
                              ResultSet rs1 = preparedstatement.executeQuery();
                              while (rs1.next()){
                                  if (contador < rs1.getInt("contador"))
                                  {
                                      contador = rs1.getInt("contador");  //Apenas queremos o que tem mais votos!

                                      descricao_produto = rs1.getString("descricao");

                                  }

                              }
                              if (contador >= 0) {
                                  fim_projecto.add("product_type");
                                  fim_projecto.add(descricao_produto);


                                  try{
                                      query = "UPDATE product_type SET status = TRUE WHERE contador = "+contador; //Vamos ver quem ganhou a votação e por a Flag

                                      preparedstatement = connection.prepareStatement(query);
                                      preparedstatement.executeUpdate();

                                  } catch (SQLException ex) {
                                      System.err.print("SQLException 780:  " + ex);
                                  }

                              }

                          } catch (SQLException ex) {
                              System.err.print("SQLException 788:  " + ex);
                          }
                      }

                  else{ //Se não angariou dinheiro suficiente, então não foi concluido com sucesso
                      try {
                          query = "SELECT valor, id_user from pledge_user WHERE id_projecto = "+projectID; //Temos então que devolver todo o dinheiro a quem doou ao projecto
                          request = connection.createStatement();                                         // Aqui vemos quem doou e quanto doou
                          ResultSet rs1 = request.executeQuery(query);
                          while (rs1.next()){
                              valor_a_devolver = rs1.getInt("valor");
                              userID = rs1.getInt("id_user");

                              try{
                                  query = "UPDATE utilizador SET saldo = saldo+"+valor_a_devolver+" WHERE id = "+userID; //Aqui damos o dinheiro a quem doou
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
                          query = "DELETE FROM pledge_user WHERE id_projecto = "+projectID;   //Aqui cancelamos a doação
                          request = connection.createStatement();
                          request.executeUpdate(query);

                      } catch (SQLException ex) {
                          System.err.println("Erro:" + ex);
                      }

                      try{
                          query = "SELECT id FROM recompensas WHERE id_projecto = "+projectID; //Temos também que revogar as recompensas dos users
                                                                                              //Aqui vemos quais as recompensas associadas ao projecto

                          request = connection.createStatement();
                          ResultSet rs1 = request.executeQuery(query);
                          while (rs1.next()){
                              rewardID = rs1.getInt("id");
                              try{
                                  query = "DELETE FROM recompensa_user WHERE id_recompensa= "+rewardID; //Aqui removemos a recompensa ao user
                                  request = connection.createStatement();
                                  request.executeUpdate(query);
                              } catch(SQLException ex){
                                  System.err.print("SQLException 777: "+ex);
                              }

                          }
                      } catch(SQLException ex){
                          System.err.print("SQLException 782: "+ex);
                      }


                      }



                  }

              }
          } catch (SQLException e) {

              e.printStackTrace();
          }



      }

      public ClientRequest getActualProjects(ClientRequest clrqst) throws RemoteException {

          int i;
          System.out.println("[RMI Server] Função <getActualProjects> chamada!");

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

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
                      System.out.println("Não há projectos activos");
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
                  if (choice == 1) {
                      if (rs.getBoolean("over")) {
                          actual_projects[i] = "" + rs.getInt("valorpretendido") + " \tProjecto concluído";
                          i++;
                      } else {
                          actual_projects[i] = "" + rs.getInt("valorpretendido") + " \tNão conseguiu angariar o dinheiro necessário";
                          i++;
                      }
                  } else {
                      actual_projects[i] = "" + rs.getInt("valorpretendido");
                      i++;
                  }

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

      public ClientRequest checkRequest(ClientRequest clrqst) throws RemoteException {

          for (int i = 0; i < myRequests.size(); i++) {
              if (clrqst.getRequestID().equals(myRequests.get(i).getRequestID()) && clrqst.getTimestamp().equals(myRequests.get(i).getTimestamp())) {
                  if (myRequests.get(i).getStage().equals("rmiout")) {
                      return myRequests.get(i);
                  }
              }
          }

          return null;
      }

      public ClientRequest getProjectDetails(ClientRequest clrqst) throws RemoteException {
          System.out.println("[RMI Server] Função <getProjectDetails> chamada!");

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

          int i = 0, j = 0, pointer = 0;
          String[] project_details = new String[2000];
          Object[] objecto = clrqst.getRequest();

          int id_projecto = (int) objecto[1];
          int num_recompensas = 0, num_niveis_extra = 0, num_product_type = 0;

          try {
              query = "SELECT titulo, descricao, valorpretendido, valoractual, data_limite "
                      + "FROM projecto "
                      + "WHERE projecto.id =" + id_projecto
                      + " AND status = TRUE";

              request = connection.createStatement();
              rs = request.executeQuery(query);
              if (rs.next()) {
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
                	  System.out.println("O ID-PROJ E " + id_projecto);
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

      public ClientRequest pledgeToProject(ClientRequest clrqst) throws RemoteException {
          System.out.println("[RMI Server] Função <pledgeToProject> chamada!");

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

          Object[] objecto = clrqst.getRequest();
          int[] how_much__to_who = (int[]) (objecto[1]);
          int how_much = how_much__to_who[0];
          int to_who = how_much__to_who[1];

          int user_id = (int) objecto[0];
          int novo_valor_actual = 0, novo_saldo = 0;
          ArrayList<String> product_type = new ArrayList();
          int saldo = 0, i = 0;
          String recompensas = new String();
          int id_recompensa = 0;

          try {
              query = "SELECT saldo "
                      + "FROM utilizador "
                      + "WHERE id =" + user_id;
              request = connection.createStatement();
              rs = request.executeQuery(query);
              if (rs.next()) {
                  saldo = rs.getInt("saldo");
              }
              if (saldo >= how_much) {

                  try {
                      System.out.println(to_who);
                      query = "SELECT valoractual FROM projecto WHERE id = " + to_who;
                      request = connection.createStatement();
                      rs = request.executeQuery(query);
                      rs.next();
                      novo_valor_actual = rs.getInt("valoractual");
                      novo_valor_actual = novo_valor_actual + how_much;
                  } catch (SQLException ex) {
                      System.err.println("SQLException 622: " + ex);
                  }
                  try {
                      query = "UPDATE projecto SET valoractual = " + novo_valor_actual + " WHERE id = " + to_who;
                      request = connection.createStatement();
                      request.executeUpdate(query);

                  } catch (SQLException ex) {
                      System.err.println("SQLException: How much " + ex);
                  }

                  try {
                      query = "SELECT saldo FROM utilizador WHERE id = " + user_id;
                      request = connection.createStatement();
                      rs = request.executeQuery(query);
                      rs.next();
                      novo_saldo = rs.getInt("saldo") - how_much;
                  } catch (SQLException ex) {
                      System.out.println("SQLException 630: " + ex);
                  }
                  try {
                      query = "UPDATE utilizador SET saldo = " + novo_saldo + " WHERE id = " + user_id;
                      request = connection.createStatement();
                      request.executeUpdate(query);

                  } catch (SQLException ex) {
                      System.err.println("SQLException 640 :" + ex);
                  }

                  try {
                      query = "INSERT INTO pledge_user (id_user, id_projecto, valor) VALUES (" + user_id + ", " + to_who + ", " + how_much + ")";
                      request = connection.createStatement();
                      request.executeUpdate(query);

                  } catch (SQLException ex) {
                      System.err.println("SQLException 650 :" + ex);
                  }

                  try {

                      query = "SELECT * "
                              + "FROM recompensas "
                              + "WHERE id_projecto =" + to_who + " "
                              + "AND valor <=" + how_much + " "
                              + "ORDER BY valor DESC";
                      request = connection.createStatement();
                      rs = request.executeQuery(query);
                      if (rs.next()) {
                          i++;
                          recompensas = rs.getString("titulo");
                          id_recompensa = rs.getInt("id");
                          resposta[2] = i;
                          resposta[3] = recompensas;

                          try {

                              query = "INSERT INTO recompensa_user (id_recompensa, id_user) VALUES (CAST(? AS integer),CAST(? AS integer))";
                              preparedstatement = connection.prepareStatement(query);
                              preparedstatement.setInt(1, (int) (id_recompensa));
                              preparedstatement.setInt(2, (int) (user_id));
                              preparedstatement.executeUpdate();

                          } catch (SQLException ex) {
                              System.out.println("SQLException: 665 " + ex);
                          }

                      } else {
                          resposta[2] = i;
                          resposta[3] = "No reward";
                      }

                  } catch (SQLException ex) {
                      System.err.println("SQLException:" + ex);
                  }

                  try {
                      query = "SELECT descricao FROM product_type WHERE id_projecto =" + to_who;
                      request = connection.createStatement();
                      rs = request.executeQuery(query);
                      while (rs.next()) {
                          product_type.add(rs.getString("descricao"));
                      }

                  } catch (SQLException ex) {
                      System.err.println("SQLException 784:" + ex);
                  }

                  resposta[0] = "pledged";
                  resposta[1] = saldo - how_much;
                  resposta[4] = product_type;
              } else {

                  resposta[0] = "Sem saldo";
                  resposta[1] = saldo;
                  resposta[2] = i;
                  resposta[3] = "No reward";
                  resposta[4] = product_type;
              }
          } catch (SQLException ex) {
              System.err.println("SQLException 791:" + ex);
          }
          clrqst.setResponse(resposta);
          clrqst.setStage(3);
          return clrqst;
      }

      public ClientRequest addAdminToProject(ClientRequest clrqst) throws RemoteException {

          System.out.println("[RMI Server] Função <addAdminToProject> chamada!");

          requestCheck = checkRequest(clrqst);

          if (requestCheck != null) {
              return requestCheck;
          }

          Object[] objecto = clrqst.getRequest();
          String user = (String) (objecto[1]);
          int id_projecto = (int) (objecto[2]);
          int id_user = 0;

          try {
              query = "SELECT id FROM utilizador WHERE username= '" + user + "'";
              request = connection.createStatement();
              rs = request.executeQuery(query);
              if (rs.next()) {
                  id_user = rs.getInt("id");
                  try {
                      query = "INSERT INTO projecto_user (id_projecto, id_user) VALUES (" + id_projecto + "," + id_user + ")";
                      preparedstatement = connection.prepareStatement(query);

                      preparedstatement.executeUpdate();
                      resposta[2] = "done";

                  } catch (SQLException ex) {
                      System.err.println("SQLException:" + ex);
                  }
              } else {
                  resposta[2] = "no_user";
              }

          } catch (SQLException ex) {
              System.err.println("SQLException:" + ex);
          }
          if (resposta[2] == null) {
              resposta[2] = "bode";
          }
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

          new cronoThread(this);

      }

      /**
       * Função responsável para actualizar os pedidos do cliente, a medida que eles vão sendo tratados.
       */
      private void updateRequest(ClientRequest clrqst) {

          int requestIndex = myRequests.indexOf(clrqst);

          myRequests.get(requestIndex).setStage(4);
      }


      public String checkDataBaseConection() throws RemoteException {

          if (connection == null) {
              return "notdone";
          } else {
              return "done";
          }

      }

      public String addProductType(ClientRequest clrqst) throws RemoteException{

        int projectId = (int)clrqst.getRequest()[0];
        String productDesc = (String)clrqst.getRequest()[1];
        try{
          query = "INSERT INTO product_type (descricao, id_projecto, contador) VALUES (?,?,?)";
          preparedstatement = connection.prepareStatement(query);
          preparedstatement.setString(1, productDesc);
          preparedstatement.setInt(2, projectId);
          preparedstatement.setInt(3, 0);
          preparedstatement.executeUpdate();

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return "";

        }
        return "product_save";

      }





  }



class cronoThread extends Thread {

	private RMIServer rmi;
	    public cronoThread(RMIServer rmi) {
	    	this.rmi = rmi;
	        this.start();

	    }

	    public void run() {

	        while (true) {
	        	System.out.println("Estou a correr");
	        	try {
					rmi.terminaProjecto();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
	            try {
	                Thread.sleep(3600000);
	            } catch (InterruptedException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }

	}
