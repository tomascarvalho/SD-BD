package terminal;

import beans.*;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

	private static final long serialVersionUID = 1L;

	public DBConnection dbc;

	public RMIServer() throws Exception {
		
		super();
		this.dbc = new DBConnection("postgres", "samu123", "SDDB");

	}

	public void db_query(String query, Object... args) throws RemoteException {

		try{
			this.dbc.noResponseQuery(query,args);
		} catch (Exception e) {
			Utils.print(e.toString());
		}
	}

	// ------------------------------------------ Metodos da WEB

	public int db_query_integer_WEB(String query, Object... args) throws RemoteException {

		DBResult res = null;
		ResultSet rs = null;
		int value = 0;
		try {
			res = this.dbc.query(query,args);
			if(res.getResultSet().next()){
				rs = res.getResultSet();
				value = rs.getInt("return_value");
			}
		} catch (SQLException e1) {
			Utils.print("Deu MERDAAA!!!");
			e1.printStackTrace();
		}
		Utils.print("Got "+value);
		return value;
		 
	}

	public ArrayList<UserBean> db_query_user_WEB(String query, Object... args) throws RemoteException, SQLException {

		ArrayList<UserBean> result = new ArrayList<UserBean>();
		DBResult res = this.dbc.query(query,args);
		while (res.getResultSet().next()) {
			UserBean tmp = new UserBean();
			ResultSet rs = res.getResultSet();
			tmp.setId(rs.getInt("id"));
			tmp.setUsername(rs.getString("username"));
			tmp.setSaldo(rs.getInt("bal"));
			result.add(tmp);
		}

		return result;

	}

	public ArrayList<TopicBean> db_query_topic_WEB(String query, Object... args) throws RemoteException, SQLException {

		ArrayList<TopicBean> result = new ArrayList<TopicBean>();
		DBResult res = this.dbc.query(query,args);
		while (res.getResultSet().next()) {
			TopicBean tmp = new TopicBean();
			ResultSet rs = res.getResultSet();
			tmp.setId(rs.getInt("id"));
			tmp.setName(rs.getString("title"));
			tmp.setAuthor(rs.getString("creator"));
			result.add(tmp);
		}

		return result;

	}

	public ArrayList<PackBean> db_query_pack_WEB(String query, Object... args) throws RemoteException, SQLException {

		ArrayList<PackBean> result = new ArrayList<PackBean>();
		DBResult res = this.dbc.query(query,args);
		while (res.getResultSet().next()) {
			PackBean tmp = new PackBean();
			ResultSet rs = res.getResultSet();
			tmp.setId(rs.getInt("id"));
			tmp.setOwner(rs.getString("id_user"));
			tmp.setIdea(rs.getInt("id_idea"));
			tmp.setTitle(rs.getString("title"));
			tmp.setShares(rs.getInt("num_av"));
			tmp.setPrice(rs.getInt("price"));
			result.add(tmp);
		}
		
		return result;

	}

	public ArrayList<IdeaBean> db_query_idea_WEB(String query, Object... args) throws RemoteException, SQLException {

		ArrayList<IdeaBean> result = new ArrayList<IdeaBean>();
		DBResult res = this.dbc.query(query,args);
		while (res.getResultSet().next()) {
			IdeaBean tmp = new IdeaBean();
			ResultSet rs = res.getResultSet();
			tmp.setId(rs.getInt("id"));
			tmp.setName(rs.getString("title"));
			tmp.setDescription(rs.getString("description"));
			tmp.setAuthor(rs.getString("creator"));
			result.add(tmp);
		}

		return result;

	}

	public ArrayList<HistoricBean> db_query_historic_WEB(String query, Object... args) throws RemoteException, SQLException {

		ArrayList<HistoricBean> result = new ArrayList<HistoricBean>();
		DBResult res = this.dbc.query(query,args);
		while (res.getResultSet().next()) {
			HistoricBean tmp = new HistoricBean();
			ResultSet rs = res.getResultSet();
			tmp.setId(rs.getInt("id"));
			tmp.setBuyer(rs.getString("user1"));
			tmp.setSeller(rs.getString("user2"));
			tmp.setTitle(rs.getString("title"));
			tmp.setShares(rs.getInt("num_acc"));
			tmp.setPrice(rs.getInt("price"));
			result.add(tmp);
		}

		return result;

	}

	// ------------------------------------------ Metodos do FTP

	public DBData db_query_answer(DBData data, String query, Object... args) throws RemoteException {

		try{
			DBResult res = this.dbc.query(query,args);

			if(data.getOrder() == 101) {						// DBData para receber users		// ISTO NAO É USADO -.- foi só para testar...
				String temp = "";
				int rows = 0;
				while (res.getResultSet().next()) {
					rows++;
					ResultSet rs 	 = res.getResultSet();
					temp += (rs.getInt("id")+"\t");
					temp += (rs.getString("username")+"\t");
					temp += (rs.getString("pass")+"\t");
					temp += (rs.getInt("bal")+"");
					data.addData(temp);
					temp = "";
				}
				data.setRow(rows);
			} else if(data.getOrder() == 102) {					// DBData para receber topics
				String temp = "";
				int rows = 0;
				while (res.getResultSet().next()) {
					rows++;
					ResultSet rs 	 = res.getResultSet();
					temp += (" ID:"+rs.getInt("id"));
					temp += ("\tTítulo: "+rs.getString("title")+"\n");
					temp += ("\tCriado por: "+rs.getString("creator")+"\t");
					temp += ("Contém "+rs.getInt("num_ideas")+" ideias");
					data.addData(temp);
					temp = "";
				}
				data.setRow(rows);
			} else if(data.getOrder() == 103) {					// DBData para receber ideas
				String temp = "";
				int rows = 0;
				while (res.getResultSet().next()) {
					rows++;
					ResultSet rs 	 = res.getResultSet();
					temp += (" ID:"+rs.getInt("id"));
					temp += ("\tTitulo: "+rs.getString("title"));
					if(rs.getInt("pos")==1) temp += "\t\tCONTRA!";
					if(rs.getInt("pos")==2) temp += "\t\tA FAVOR!";
					if(rs.getInt("pos")==3) temp += "\t\tNEUTRO!";
					temp += ("\n\tDividida em "+rs.getInt("num_shares")+" shares");
					temp += (" e criada por: "+rs.getString("creator")+"\n");
					temp += ("\tDescrição: "+rs.getString("description")+"\n\n");
					data.addData(temp);
					temp = "";
				}
				data.setRow(rows);
			} else if(data.getOrder() == 104) {					// DBData para receber packs
				String temp = "";
				int rows = 0;
				while (res.getResultSet().next()) {
					rows++;
					ResultSet rs 	 = res.getResultSet();
					temp += (" ID:"+rs.getInt("id"));
					temp += ("\tDono: "+rs.getString("id_user")+"\t");
					temp += ("Ideia: "+rs.getInt("id_idea")+"\n");
					temp += ("\tAcções privadas: "+rs.getInt("num_un")+"\n");
					temp += ("\tAcções  à venda: "+rs.getInt("num_av")+"\n");
					temp += ("\tPreço de venda: "+rs.getInt("price")+" deiCoins/unid.");
					data.addData(temp);
					temp = "";
				}
				data.setRow(rows);
			} else if(data.getOrder() == 105) {					// DBData para receber historico
				String temp = "";
				int rows = 0;
				while (res.getResultSet().next()) {
					rows++;
					ResultSet rs 	 = res.getResultSet();
					temp += ("\tComprador: "+rs.getString("user1"));
					temp += ("\n\tVendedor: "+rs.getString("user2")+"\n");
					temp += ("\tNumero de acções compradas: "+rs.getInt("num_acc"));
					temp += ("\tID da ideia: "+rs.getInt("id_idea"));
					temp += ("\tPreço: "+rs.getInt("price")+" deiCoins/acção");
					data.addData(temp);
					temp = "";
				}
				data.setRow(rows);
			} else if(data.getOrder() == 100) {					// DBData para receber VALORES
				int rows = 0;
				int value;
				while (res.getResultSet().next()) {
					rows++;
					ResultSet rs 	 = res.getResultSet();
					value = rs.getInt("return_value");
					data.addData(Integer.toString(value));
					Utils.print("Got "+value);
				}
				data.setRow(rows);
			} else if(data.getOrder() == 99) {					// DBData para receber STRINGS
				int rows = 0;
				String str;
				while (res.getResultSet().next()) {
					rows++;
					ResultSet rs 	 = res.getResultSet();
					str = rs.getString("return_value");
					data.addData(str);
					Utils.print("Got "+str);
				}
				data.setRow(rows);
			}
			res.close();

		} catch (Exception e) {
			Utils.print(e.toString());
		}
		return data;
	}

	public static void main(String args[]) throws RemoteException {
		
		try {
			System.setProperty("java.rmi.server.hostname","10.16.2.119");
			RMIServer h = new RMIServer();
			Registry r = LocateRegistry.createRegistry(7000);
			r.rebind("RMIServer", h);
			Utils.print("RMI Server is on...");
			System.getProperties().put("java.security.policy", "policy.all");
	        //System.setSecurityManager(new RMISecurityManager());

		} catch (Exception re) {
			Utils.print("Exception in RMI Server main: " + re);
		}
	}

}