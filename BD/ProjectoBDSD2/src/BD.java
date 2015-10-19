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
	
	public static void main(String[] argv) {
		//cria_user();
		new BD();	
	}
	
	public BD(){
		DB();
		//cria_user();
		novoProjecto();
	}
	
	//info do novo user
	public void cria_user(){
		
		String nome, apelido, username, password; 
		System.out.println("Primeiro nome:");
		nome = sc.next();
		
		System.out.println("Apelido:");
		apelido = sc.next();
		
		System.out.println("Username:");
		username = sc.next();
		
		System.out.println("Password:");
		password = sc.next();
		User novoUser = new User (nome, apelido, username, password);
		register(novoUser);
	}
	
	//Fazer o registo de um Utilizador 
	public void register(User info){

		try{
            query = "INSERT INTO utilizador (nome, apelido, username, pass) VALUES (?,?,?,?)";
            preparedstatement = connection.prepareStatement(query);
            preparedstatement.setString(1,info.getNome());
            preparedstatement.setString(2,info.getApelido());
            preparedstatement.setString(3,info.getUsername());
            preparedstatement.setString(4, info.getPassword());
            preparedstatement.executeUpdate();
//            info = Login(info);
            
		}
        catch(SQLException e)
        {
            System.err.println("SQLException:"+e);
        }

		System.out.println("Registo feito!\n");
	}

	public void novoProjecto(){
		System.out.println("Criar um novo projecto:\n");
		
		System.out.println("Titulo:");
		String tituloProjecto = sc.next();
		
		System.out.println("Descriçao:");
		String descricao = sc.next();
		
/*		System.out.println("Data Limite");
		Date dataLimite = ;*/
		
		System.out.println("Valor Pretendido");
		int valorPretendido = sc.nextInt();
		
		System.out.println("Valor actual");
		int valorActual = sc.nextInt();
				
		try{
			//inserção na tabela
			query = "INSERT INTO projecto (titulo, descricao, valorPretendido, valorActual) VALUES(?,?,?,?)";
            preparedstatement = connection.prepareStatement(query);

            preparedstatement.setString(1, tituloProjecto);
            //preparedstatement.setDate(2, dataLimite);
            preparedstatement.setString(2, descricao);
            preparedstatement.setInt(3, valorPretendido);
            preparedstatement.setInt(4, valorActual);
            preparedstatement.executeUpdate();
		}
        catch(SQLException e)
        {
            System.err.println("SQLException:"+e);
        }

		System.out.println("Dados guardados!\n");
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
		
		
		/*Statement stmt = null;
	    String query = "INSERT INTO utilizador (id, nome, apelido, username, pass) VALUES("+100+",\'diana\', \'diana\', \'diana\', \'diana\');";
		try {
		    stmt = connection.createStatement();
		    stmt.executeUpdate(query);
	    } catch (SQLException e ) {
	    	System.out.println("Failed query!"+e);
	    }*/
	}
		
	}
		
	    
