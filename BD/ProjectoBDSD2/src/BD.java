import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import org.omg.Messaging.SyncScopeHelper;


public class BD {
	Scanner sc=new Scanner(System.in);
	
	
	Connection connection;
	PreparedStatement preparedstatement = null;
	ResultSet rs = null; 
	String query;
	
	public static void main(String[] argv) throws SQLException {
		
		new BD();	
	}
	
	public BD() throws SQLException{
		DB();
		cria_user();
//		novoProjecto();
		//nova_mensagem();
		//login();																	TO BE CONTINUED
	}
	
	
	//Rewards
	public void rewards(int id, String tituloProj){
		
		System.out.println("Valor da recompensa:");
		int r = sc.nextInt();
		try{
			query = "INSERT INTO recompensas (recompensa, id, titulo) VALUES (?, ?, ?)";
            preparedstatement = connection.prepareStatement(query);
            preparedstatement.setInt(1, r);
            preparedstatement.setInt(2, id);
            preparedstatement.setString(3, tituloProj);
            preparedstatement.executeUpdate();
            
            
            System.out.println("Rewards atribuidas!");
		}
		catch(SQLException e)
        {
			System.err.println("SQLException:"+e);
        }
		
	}
	
	
	//fazer login---------------------------------------------------NAO ESTA TERMINADO
	public void login(){
		String username, pass;
		System.out.println("Login\nUsername:");
		username = sc.next();
		
		System.out.println("Password:");
		pass = sc.next();
		
		int iduser;
		
		try{
			query = "SELECT id, username, pass FROM utilizador WHERE username = ? AND pass = ?";
            preparedstatement = connection.prepareStatement(query);
            preparedstatement.setString(1, username);
            preparedstatement.setString(2, pass);
    		rs = preparedstatement.executeQuery();
            
            
            Statement request = connection.createStatement();
            rs = request.executeQuery(query);
            rs.next();
    		iduser = rs.getInt("id");
            
    
    		

		}
		catch(SQLException e)
        {
			System.err.println("SQLException:"+e);
        }

	}
	
	//envia recebe uma mensagem
	public void nova_mensagem(){
		
		String pergunta, username;
		String resposta;
		
		//só para inicializar as variáveis
		System.out.println("O que deseja enviar, caro utilizador?");
		pergunta = sc.nextLine();
		
		System.out.println("Quem sois?");
		username = sc.nextLine();
		
		System.out.println("Responde lá então:");
		resposta = sc.nextLine();
		
		
		try{
            query = "INSERT INTO mensagem (pergunta, username, resposta) VALUES (?,?,?)";
            preparedstatement = connection.prepareStatement(query);
            preparedstatement.setString(1, pergunta);
            preparedstatement.setString(2, username);
            preparedstatement.setString(3, resposta);
            preparedstatement.executeUpdate();
		}
        catch(SQLException e)
        {
            System.err.println("SQLException:"+e);
        }
		
	}
	
	
	
	//info do novo user
	public void cria_user() throws SQLException{
		
		String nome, apelido, username, password; 
		System.out.println("Primeiro nome:");
		nome = sc.next();
		
		System.out.println("Apelido:");
		apelido = sc.next();
		
		System.out.println("Username:");
		username = sc.next();
		
		System.out.println("Password:");
		password = sc.next();

		try{
            query = "INSERT INTO utilizador (nome, apelido, username, pass, saldo) VALUES (?,?,?,?,?)";
            preparedstatement = connection.prepareStatement(query);
            preparedstatement.setString(1,nome);
            preparedstatement.setString(2,apelido);
            preparedstatement.setString(3,username);
            preparedstatement.setString(4, password);
            preparedstatement.setInt(5, 100);
            preparedstatement.executeUpdate();
      
            
		}
        catch(SQLException e)
        {
            System.err.println("SQLException:"+e);
        }

		System.out.println("Registo feito!\n");
		
		 System.out.println("Passar à criação de um novo projecto");
		 try{
	         query = "SELECT id FROM utilizador WHERE username = ? AND pass = ?";
	
	 		preparedstatement = connection.prepareStatement(query);
	 		preparedstatement.setString(1, username);
	 		preparedstatement.setString(2, password);
	 		
	 		rs = preparedstatement.executeQuery();
	 		rs.next();
	 		int getid = rs.getInt("id");
	 		System.out.println(getid);
	        novoProjecto(getid);
		 }catch(SQLException e)
	        {
	            System.err.println("SQLException:"+e);
	        }

		 }
	

	public void novoProjecto(int idUser) throws SQLException{
		System.out.println("Criar um novo projecto:\n");
		
		System.out.println("Titulo: ");
		String tituloProjecto = sc.nextLine();
		
		System.out.println("Descriçao: ");
		String descricao = sc.nextLine();
			
		System.out.println("Valor Pretendido: ");
		int valorPretendido = sc.nextInt();
		
		System.out.println("Valor actual: ");
		int valorActual = sc.nextInt();
				
		try{			
			//inserção na tabela
			query = "INSERT INTO projecto (titulo, id_utilizador, descricao, valorPretendido, valorActual) VALUES(?,?,?,?,?)";
            preparedstatement = connection.prepareStatement(query);
            preparedstatement.setString(1, tituloProjecto);
            preparedstatement.setInt(2, idUser);
            preparedstatement.setString(3, descricao);
            preparedstatement.setInt(4, valorPretendido);
            preparedstatement.setInt(5, valorActual);
            preparedstatement.executeUpdate();
		}
        catch(SQLException e)
        {
            System.err.println("SQLException:"+e);
        }

		System.out.println("Dados guardados!\n");
		
		System.out.println("Deseja associar rewards?\n(1)-Sim	(2)-Não");
		int aux = sc.nextInt();
		
		query = "SELECT id FROM projecto WHERE titulo = ?";
		preparedstatement = connection.prepareStatement(query);
		preparedstatement.setString(1, tituloProjecto);
		rs = preparedstatement.executeQuery();
		rs.next();
		int getid = rs.getInt("id");
		
		if(aux == 1){

			rewards(getid, tituloProjecto);
		}
		
	}
		    
	
	public void DB(){
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
					"jdbc:postgresql://127.0.0.1:5432/Projecto", "postgres",
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
		
	}
		
	    
