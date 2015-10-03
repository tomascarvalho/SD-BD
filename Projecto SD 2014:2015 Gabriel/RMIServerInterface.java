import java.rmi.*;


public interface RMIServerInterface extends Remote {

	public void db_query(String query, Object... args) throws java.rmi.RemoteException;
	public DBData db_query_answer(DBData data, String query, Object... args) throws java.rmi.RemoteException;
	public int db_query_integer_WEB(String query, Object... args) throws java.rmi.RemoteException;

	public ArrayList<UserBean>     db_query_user_WEB(String query, Object... args) throws java.rmi.RemoteException;
	public ArrayList<TopicBean>    db_query_topic_WEB(String query, Object... args) throws java.rmi.RemoteException;
	public ArrayList<PackBean>     db_query_pack_WEB(String query, Object... args) throws java.rmi.RemoteException;
	public ArrayList<IdeaBean>     db_query_idea_WEB(String query, Object... args) throws java.rmi.RemoteException;
	public ArrayList<HistoricBean> db_query_historic_WEB(String query, Object... args) throws java.rmi.RemoteException;
}