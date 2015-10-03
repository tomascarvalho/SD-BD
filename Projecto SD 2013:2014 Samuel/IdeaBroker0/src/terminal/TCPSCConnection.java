package terminal;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.rmi.*;
import java.rmi.server.*;

public class TCPSCConnection extends Thread {
	
	Socket clientSocket;
	int threadNumber;
	RMIServerInterface rmi_server;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	String username = "";
	Notifier not;
	Client c;
	Data backup;

	public TCPSCConnection (Socket cliSocket, int n, RMIServerInterface rmisi, Notifier notify) {

		this.clientSocket = cliSocket;
		this.threadNumber = n;
		this.rmi_server = rmisi;
		this.not = notify;

		try {

			oos = new ObjectOutputStream(cliSocket.getOutputStream());
			ois = new ObjectInputStream(cliSocket.getInputStream());

			this.start();

		} catch (IOException e) {
			Utils.print("[TCPSCConnection] Connection:" + e.getMessage());
		}
	}

	private void getNotifications() throws RemoteException {
		Utils.print("Pedido de notificações offline");
		Data answer = new Data();
		answer.setOrder(-3);
		String temp = new String();

		//fazer query
		DBData ask = new DBData();
		ask.setOrder(105);
		DBData got = rmi_server.db_query_answer(ask,"SELECT * FROM history WHERE ((user2=? AND read=?) OR (user1=? AND read=?)) AND done=1",username,1,username,2);
		if(got.getRow() > 0){
			temp += ("\n\n************************* NOTIFICAÇÕES *************************\n\n");
			int i;
			for(i=0;i<got.getRow();i++) temp += (got.pollData()+"\n\n");
			temp += ("****************************************************************\n\nOpção: ");
		}
		rmi_server.db_query("UPDATE history SET read=3 WHERE ((user2=? AND read=?) OR (user1=? AND read=?)) AND done=1",username,1,username,2);
		answer.addData(temp);
		Utils.writeData(answer,oos);
		Utils.print("Resposta ao pedido enviado!");
		return;
	}

	private void seeIfPossible(int id_idea) throws RemoteException {
		Utils.print("Checking for scheduled buys... It can take a while...");

		DBData ask = new DBData();
		ask.setOrder(100);
		// recebe o preço do pack mais barato daquela ideia com accoes disponiveis
		DBData got = rmi_server.db_query_answer(ask,"SELECT min(price) as return_value FROM packs WHERE id_idea=? AND num_av>0",id_idea);
		int preco_min = Integer.parseInt(got.pollData());

		DBData ask2 = new DBData();
		ask2.setOrder(100);
		// recebe os ids das compras (ainda nao efectuadas) em que o preço é superior ou igual ao preço minimo (acima definido)
		DBData got2 = rmi_server.db_query_answer(ask2,"SELECT id as return_value FROM history WHERE done=0 AND id_idea=? AND price>=? ORDER BY id",id_idea,preco_min);
		int i;
		int id_hist;
		int id_pack;
		int num_acc;
		String buyer = new String();
		for(i=0;i<got2.getRow();i++){
			id_hist = Integer.parseInt(got2.pollData());
			DBData ask3 = new DBData();
			ask3.setOrder(100);
			// recebe o numero de accoes que foram agendadas
			DBData got3 = rmi_server.db_query_answer(ask3,"SELECT num_acc as return_value FROM history WHERE id=?",id_hist);
			num_acc = Integer.parseInt(got3.pollData());

			DBData ask5 = new DBData();
			ask5.setOrder(100);
			// recebe o id do pack com o preco mais barato (mas acima do minimo) para poder efectuar a compra agendada
			DBData got5 = rmi_server.db_query_answer(ask5,"SELECT id as return_value FROM packs WHERE id_idea=? AND price>=? AND num_av>? ORDER BY price LIMIT 1",id_idea,preco_min,num_acc);
			id_pack = Integer.parseInt(got5.pollData());

			if(got5.getRow() > 0){
				DBData ask4 = new DBData();
				ask4.setOrder(99);
				// recebe o nome do utilizador que agentou a compra
				DBData got4 = rmi_server.db_query_answer(ask4,"SELECT user1 as return_value FROM history WHERE id=?",id_hist);
				buyer = got4.pollData();

				Data d = new Data();
				d.addData(id_pack+"");
				d.addData(id_idea+"");
				d.addData(num_acc+"");
				d.addData(buyer);
				d.addData(id_hist+"");
				buyShares(d,1);
			}

		}
	}

	private void regist(Data data) throws RemoteException {					// id = 0
		String data_username = data.pollData();
		String data_password = data.pollData();

		Utils.print("Cliente a fazer registo");
		Data answer = new Data();
		// fazer query para saber se o username ja está a ser usado
		DBData ask = new DBData();
		ask.setOrder(100);
		DBData got = rmi_server.db_query_answer(ask,"SELECT count(*) as return_value FROM users WHERE username=?",data_username);
		// se ja existe -> answer.setOrder = -1;
		// se nao existe-> answer.setOrder = 1;
		if(got.pollData().equals("0")){
			answer.setOrder(1);
			rmi_server.db_query("INSERT INTO users (username,pass,bal) VALUES (?,?,?)",data_username,data_password,10000);
			Utils.print("Novo utilizador criado!");
		}
		else{
			answer.setOrder(-1);
			Utils.print("Utilizador ja existente!");
		}
		Utils.writeData(answer,oos);
		Utils.print("Resposta ao pedido de registo enviado!");
		return;
	}

	private void login(Data data) throws RemoteException {					// id = 1
		String data_username = data.pollData();
		String data_password = data.pollData();

		Utils.print("Cliente a fazer login");
		Data answer = new Data();
		// fazer query para saber se o username e pass estao correctos
		DBData ask = new DBData();
		ask.setOrder(100);
		DBData got = rmi_server.db_query_answer(ask,"SELECT count(*) as return_value FROM users WHERE username=? AND pass=?",data_username,data_password);
		// se correcto   -> answer.setOrder = 1;
		// se nao corecto-> answer.setOrder = -1;
		if(got.pollData().equals("1")){
			answer.setOrder(1);
			changeUsername(data_username);
			Utils.print("Login com sucesso!");
		}
		else{
			answer.setOrder(-1);
		}
		Utils.writeData(answer,oos);
		Utils.print("Resposta ao pedido de login enviado!");
		try{
			sleep(1);
		} catch (InterruptedException e){}
		getNotifications();
		return;
	}

	private void wallet(Data data) throws RemoteException {					// id = 2
		Utils.print("Pedido de informacoes da carteira");
		Data answer = new Data();
		answer.setOrder(-3);
		String info = new String();
		
		// fazer query
		DBData ask = new DBData();
		ask.setOrder(100);
		DBData got = rmi_server.db_query_answer(ask,"SELECT bal as return_value FROM users WHERE username=?",username);
		info = "\t-> O Seu saldo é de: "+Integer.parseInt(got.pollData())+" deiCoins! <-\n\n";

		ask.setOrder(104); // receber packs
		got = rmi_server.db_query_answer(ask,"SELECT * FROM packs WHERE id_user=?",username);
		if(got.getRow()>0){
			int i;
			for(i=0;i<got.getRow();i++){
				info += ((got.pollData())+"\n\n");
			}
			info += "\n1 - Alterar preço de acções";
			info += "\n2 - Tornar acções disponíveis para venda";
			info += "\n3 - Tornar acções indisponíveis para venda\n";
		}

		answer.addData(info);
		Utils.writeData(answer,oos);
		Utils.print("Informacoes da carteira enviadas!");
		return;
	}

	private void histTrans(Data data) throws RemoteException {				// id = 3
		Utils.print("Pedido de historico de transacoes");
		Data answer = new Data();
		answer.setOrder(-3);
		String info = new String();
		
		// fazer query
		DBData ask = new DBData();
		ask.setOrder(105); // receber historico
		DBData got = rmi_server.db_query_answer(ask,"SELECT * FROM history WHERE (user1=? OR user2=?) AND done=1",username,username);
		int i;
		for(i=0;i<got.getRow();i++){
			info += ((got.pollData())+"\n\n");
		}

		answer.addData(info);
		Utils.writeData(answer,oos);
		Utils.print("Historico de transacoes enviado!");
		return;
	}

	private void createTopic(Data data) throws RemoteException {			// id = 4
		String topic_name = data.pollData();
		Utils.print("Pedido de criacao de um topico");
		Data answer = new Data();

		// fazer query para ver se o topico ja existe :)
		DBData ask = new DBData();
		ask.setOrder(100);
		DBData got = rmi_server.db_query_answer(ask,"SELECT count(*) as return_value FROM topics WHERE lower(title)=lower(?)",topic_name);
		// se ja existe -> answer.setOrder = -1;
		// se nao existe-> answer.setOrder = 1;
		if(got.pollData().equals("0")){
			answer.setOrder(1);
			rmi_server.db_query("INSERT INTO topics (title,creator,num_ideas) VALUES (?,?,0)",topic_name,username);
			Utils.print("Novo topico criado!");
		}
		else{
			answer.setOrder(-1);
			Utils.print("Topico ja existente!");
		}

		Utils.writeData(answer,oos);
		Utils.print("Resposta ao pedido enviada!");
		return;
	}

	private void createIdea(Data data) throws RemoteException {				// id = 5
		String ids_topics = data.pollData();
		int id_main = Integer.parseInt(data.pollData());
		String idea_title = data.pollData();
		String desc = data.pollData();
		int num_acc = Integer.parseInt(data.pollData());
		int ppa = Integer.parseInt(data.pollData());
		int num_acc_disp = Integer.parseInt(data.pollData());
		int pos = Integer.parseInt(data.pollData());
		int file = Integer.parseInt(data.pollData());
		Utils.print("Pedido de criacao de uma ideia");
		Data answer = new Data();

		// fazer query para submeter a ideia
		rmi_server.db_query("INSERT INTO ideas (id_main,title,description,creator,num_shares,pos) VALUES (?,?,?,?,?,?)",id_main,idea_title,desc,username,num_acc,pos);
		DBData ask = new DBData();
		ask.setOrder(100);
		DBData got = rmi_server.db_query_answer(ask,"SELECT id as return_value FROM ideas WHERE title=? AND creator=?",idea_title,username);
		int id_idea = Integer.parseInt(got.pollData());

		int id_topic_temp;
		if(!ids_topics.equals("-1")){
			StringTokenizer st = new StringTokenizer(ids_topics);
			while (st.hasMoreTokens()) {
				id_topic_temp = Integer.parseInt(st.nextToken());
				rmi_server.db_query("INSERT INTO relation (id_idea,id_topic) VALUES (?,?)",id_idea,id_topic_temp);
				rmi_server.db_query("UPDATE topics SET num_ideas=num_ideas+1 WHERE id=?",id_topic_temp);
			}
		}
		if (file == 1) {
			try {
				Utils.print("Vai ser adicionado um ficheiro!");
				byte[] f = (byte[]) ois.readObject();
				File someFile = new File("/home/samuthekid/Dropbox/JP&Samu/files/"+idea_title+"_file.bin");
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(someFile));
				os.writeObject(this);
        		os.flush();
        		os.close();
			} catch (IOException io) {
			} catch (ClassNotFoundException cnf) {
			}
		}

		rmi_server.db_query("INSERT INTO packs (id_user,id_idea,num_av,num_un,price) VALUES (?,?,?,?,?)",username,id_idea,num_acc_disp,num_acc-num_acc_disp,ppa);

		answer.setOrder(1);
		Utils.writeData(answer,oos);
		Utils.print("Resposta ao pedido enviada!");
		return;
	}

	private void deleteIdea(Data data) throws RemoteException {				// id = 6
		int id_idea = Integer.parseInt(data.pollData());
		Utils.print("Pedido de eliminacao de uma ideia");
		Data answer = new Data();

		// fazer query para receber ids das ideias
		DBData ask = new DBData();
		ask.setOrder(100);
		DBData got = rmi_server.db_query_answer(ask,"SELECT id as return_value FROM ideas WHERE creator=? AND num_shares=(SELECT sum(num_av+num_un) FROM packs WHERE id_user=? AND id_idea = ideas.id)",username,username);
		int rows = got.getRow();
		int [] meus_ids = new int[rows];
		int i;
		int j;
		for(i=0;i<rows;i++){
			meus_ids[i] = Integer.parseInt(got.pollData());
		}

		answer.setOrder(-1);
		
		for(i=0;i<rows;i++){
			if(meus_ids[i] == id_idea){
				answer.setOrder(1);
				DBData ask2 = new DBData();
				ask2.setOrder(100);
				DBData got2 = rmi_server.db_query_answer(ask2,"SELECT id_topic as return_value FROM relation WHERE id_idea=?",id_idea);
				if(got2.getRow() > 0){
					for(j=0;j<got2.getRow();j++) rmi_server.db_query("UPDATE topics SET num_ideas=num_ideas-1 WHERE id=?",Integer.parseInt(got2.pollData()));
				}
				rmi_server.db_query("DELETE FROM relation WHERE id_idea=?",id_idea);
				rmi_server.db_query("DELETE FROM ideas WHERE id=?",id_idea);
				rmi_server.db_query("DELETE FROM packs WHERE id_idea=?",id_idea);
			}
		}
		
		Utils.writeData(answer,oos);
		Utils.print("Resposta ao pedido enviada!");
		return;
	}

	private void seeTopics(Data data) throws RemoteException {				// id = 7
		Utils.print("Pedido para ver lista de topicos");
		Data answer = new Data();
		answer.setOrder(-3);
		String info = new String();

		// fazer query
		DBData ask = new DBData();
		ask.setOrder(102); // receber topicos
		DBData got = rmi_server.db_query_answer(ask,"SELECT * FROM topics");
		int i;
		for(i=0;i<got.getRow();i++){
			info += (got.pollData())+"\n\n";
		}

		answer.addData(info);
		Utils.writeData(answer,oos);
		Utils.print("Lista de topicos enviada!");
		return;
	}

	private void seeIdeas(Data data) throws RemoteException {				// id = 8
		int topic_id = Integer.parseInt(data.pollData());
		Utils.print("Pedido para ver lista de ideias");
		Data answer = new Data();
		answer.setOrder(-3);
		String info = new String();

		// fazer query com o ID do topico
		DBData ask = new DBData();
		ask.setOrder(100); // receber id_ideas
		DBData got = rmi_server.db_query_answer(ask,"SELECT id_idea as return_value FROM relation WHERE id_topic=?",topic_id);
		int i;
		DBData got2;
		DBData ask2 = new DBData();
		ask2.setOrder(103);
		for(i=0;i<got.getRow();i++){
			got2 = rmi_server.db_query_answer(ask2,"SELECT * FROM ideas WHERE id=?",Integer.parseInt(got.pollData()));
			info += got2.pollData();
		}

		answer.addData(info);
		Utils.writeData(answer,oos);
		Utils.print("Lista de ideias enviada!");
		return;
	}

	private void seeSubIdeas(Data data) throws RemoteException {			// id = 9
		int id_main_idea = Integer.parseInt(data.pollData());
		Utils.print("Pedido para ver lista de sub ideias");
		Data answer = new Data();
		answer.setOrder(-3);
		String info = new String();

		// fazer query
		DBData ask = new DBData();
		ask.setOrder(103); // receber ideas
		DBData got = rmi_server.db_query_answer(ask,"SELECT * FROM ideas WHERE id_main=?",id_main_idea);
		int i;
		for(i=0;i<got.getRow();i++){
			info += (got.pollData())+"\n\n";
		}

		answer.addData(info);
		Utils.writeData(answer,oos);
		Utils.print("Lista de sub ideias enviada!");
		return;
	}

	private void infoIdea(Data data) throws RemoteException {				// id = 10
		int idea_id = Integer.parseInt(data.pollData());
		Utils.print("Pedido para ver info de uma ideia");
		Data answer = new Data();
		answer.setOrder(-3);
		String info = new String();

		// fazer query
		DBData ask = new DBData();
		ask.setOrder(103); // receber info da ideia
		DBData got = rmi_server.db_query_answer(ask,"SELECT * FROM ideas WHERE id=?",idea_id);
		info += (got.pollData());

		answer.addData(info);
		Utils.writeData(answer,oos);
		Utils.print("Info de uma ideia enviada!");
		return;
	}

	private void changeSharesValue(Data data) throws RemoteException {		// id = 11
		int id_pack = Integer.parseInt(data.pollData());
		int novo_preco = Integer.parseInt(data.pollData());
		Utils.print("Pedido para mudar preco das accoes");
		Data answer = new Data();

		// fazer query para alterar os packs
		rmi_server.db_query("UPDATE packs SET price=? WHERE id=? AND id_user=?",novo_preco,id_pack,username);
		answer.setOrder(1);
		DBData ask = new DBData();
		ask.setOrder(100); // receber ints
		DBData got = rmi_server.db_query_answer(ask,"SELECT id_idea as return_value FROM packs WHERE id=?",id_pack);
		seeIfPossible(Integer.parseInt(got.pollData()));
		Utils.writeData(answer,oos);
		Utils.print("Resposta ao pedido enviada!");
		return;
	}

	private void buyShares(Data data, int auto) throws RemoteException {	// id = 12
		int id_pack = Integer.parseInt(data.pollData());
		int id_idea = Integer.parseInt(data.pollData());
		int num_acc = Integer.parseInt(data.pollData());
		int id_hist = 0;
		String buyer = new String();
		if(auto==0){
			buyer = this.username;
		}
		else{ // auto == 1
			buyer = data.pollData();
			id_hist = Integer.parseInt(data.pollData());
		}
		Utils.print("Pedido para comprar accoes");
		Data answer = new Data();

		// fazer query para ver se o numero de accoes a comprar é superior às disponiveis
		DBData ask = new DBData();
		ask.setOrder(100);
		DBData got = rmi_server.db_query_answer(ask,"SELECT num_av as return_value FROM packs WHERE id=?",id_pack);
		int acc_disp = Integer.parseInt(got.pollData()); // accoes a venda

		if(acc_disp >= num_acc){ // ha accoes suficientes para efectuar a compra

			// fazer queries para ver se o valor da compra é menor que o saldo do comprador
			DBData ask1 = new DBData();
			ask1.setOrder(100);
			DBData got1 = rmi_server.db_query_answer(ask1,"SELECT price as return_value FROM packs WHERE id=?",id_pack);
			int price = Integer.parseInt(got1.pollData()); // preco de cada accao
			DBData ask2 = new DBData();
			ask2.setOrder(100);
			DBData got2 = rmi_server.db_query_answer(ask2,"SELECT bal as return_value FROM users WHERE username=?",buyer);
			int saldo = Integer.parseInt(got2.pollData()); // saldo do comprador

			int total = num_acc * price; // preco da compra total

			if(saldo >= total){ // user tem dinheiro suficiente
				DBData ask3 = new DBData();
				ask3.setOrder(99);
				DBData got3 = rmi_server.db_query_answer(ask3,"SELECT id_user as return_value FROM packs WHERE id=?",id_pack);
				String seller = got3.pollData(); // nome do vendedor
				if(auto==1) seller = username;
				// aumentar saldo do vendedor
				rmi_server.db_query("UPDATE users SET bal=bal+? WHERE username=?",total,seller);
				// tirar accoes ao vendedor
				rmi_server.db_query("UPDATE packs SET num_av=num_av-? WHERE id=?",num_acc,id_pack);
				// notificar o vendedor

				int seller_online = not.isOnline(seller);
				int buyer_online = not.isOnline(buyer);

				Utils.print("Seller = "+seller_online);
				Utils.print("Buyer = "+buyer_online);

				if(auto==0) rmi_server.db_query("INSERT INTO history (user1,user2,num_acc,id_idea,price,done,read) VALUES (?,?,?,?,?,?,?)",buyer,seller,num_acc,id_idea,price,1,1+2*seller_online);
				else rmi_server.db_query("UPDATE history SET user2=?, price=?, done=1, read=? WHERE id=?",seller,price,2+buyer_online,id_hist);
				
				Data d = new Data();
				d.setOrder(-3);
				String temp = new String();

				temp += ("\n\n************************ NOTIFICAÇÃO ************************");
				temp += ("\n\n\tComprador: "+buyer+"\n");
				temp += ("\tVendedor: "+seller+"\n");
				temp += ("\tNumero de acções compradas: "+num_acc);
				temp += ("\tID da ideia: "+id_idea);
				temp += ("\tPreço: "+price+" deiCoins/acção\n\n");
				temp += ("*************************************************************\n\nOpção: ");
				d.addData(temp);

				Utils.print("A enviar notificaçao para "+seller+"!");
				not.sendNotification(seller, d);
				Utils.print("A enviar notificaçao para "+buyer+"!");
				not.sendNotification(buyer, d);

				// diminuir saldo do comprador
				rmi_server.db_query("UPDATE users SET bal=bal-? WHERE username=?",total,buyer);
				// ver se ja existe algum pack daquela ideia
				DBData ask4 = new DBData();
				ask4.setOrder(100);
				DBData got4 = rmi_server.db_query_answer(ask4,"SELECT count(*) as return_value FROM packs WHERE id_user=? AND id_idea=?",buyer,id_idea);
				int existe = Integer.parseInt(got4.pollData());

				if(existe == 0){ // ainda nao existe um pack com aquele user / idea
					rmi_server.db_query("INSERT INTO packs (id_user,id_idea,num_av,num_un,price) VALUES (?,?,?,?,?)",buyer,id_idea,0,num_acc,price);
				} else { // ja existe um pack daquele user para aquela idea
					rmi_server.db_query("UPDATE packs SET num_un=num_un+? WHERE id_user=? AND id_idea=?",num_acc,buyer,id_idea);
				}

				answer.setOrder(1);

			} else { // user nao tem dinheiro para esta compra
				answer.addData("Saldo insuficiente!\n");
				answer.setOrder(-1);
			}

		} else { // nao existem tantas accoes para vender
			answer.addData("Não existem tantas acções disponiveis neste pack!\n");
			answer.setOrder(-1);
		}

		if(auto==0) Utils.writeData(answer,oos);
		Utils.print("Resposta ao pedido enviada!");
		return;
	}

	private void makeAvailable(Data data) throws RemoteException {			// id = 13
		int id_pack = Integer.parseInt(data.pollData());
		int num_av = Integer.parseInt(data.pollData());
		Utils.print("Pedido para tornar accoes disponiveis");
		Data answer = new Data();

		// fazer query para alterar os packs
		DBData ask = new DBData();
		ask.setOrder(100);
		DBData got = rmi_server.db_query_answer(ask,"SELECT num_un as return_value FROM packs WHERE id=?",id_pack);
		int num_un = Integer.parseInt(got.pollData());

		if(num_av > num_un){
			answer.setOrder(-1);
		} else {
			rmi_server.db_query("UPDATE packs SET num_un=num_un-? , num_av=num_av+? WHERE id=?",num_av,num_av,id_pack);
			answer.setOrder(1);
		}

		DBData ask2 = new DBData();
		ask2.setOrder(100); // receber ints
		DBData got2 = rmi_server.db_query_answer(ask2,"SELECT id_idea as return_value FROM packs WHERE id=?",id_pack);
		seeIfPossible(Integer.parseInt(got2.pollData()));
		Utils.writeData(answer,oos);
		Utils.print("Resposta ao pedido enviada!");
		return;
	}

	private void makeUnvailable(Data data) throws RemoteException {			// id = 14
		int id_pack = Integer.parseInt(data.pollData());
		int num_un = Integer.parseInt(data.pollData());
		Utils.print("Pedido para tornar accoes indisponiveis");
		Data answer = new Data();

		// fazer query para alterar os packs
		DBData ask = new DBData();
		ask.setOrder(100);
		DBData got = rmi_server.db_query_answer(ask,"SELECT num_av as return_value FROM packs WHERE id=?",id_pack);
		int num_av = Integer.parseInt(got.pollData());

		if(num_av < num_un){
			answer.setOrder(-1);
		} else {
			rmi_server.db_query("UPDATE packs SET num_un=num_un+? , num_av=num_av-? WHERE id=?",num_un,num_un,id_pack);
			answer.setOrder(1);
		}

		Utils.writeData(answer,oos);
		Utils.print("Resposta ao pedido enviada!");
		return;
	}

	private void getMyIdeas(Data data) throws RemoteException {				// id = 15
		Utils.print("Pedido para receber ideias");
		Data answer = new Data();
		answer.setOrder(-3);
		String info = new String();

		DBData ask = new DBData();
		ask.setOrder(103); // receber ideas
		DBData got = rmi_server.db_query_answer(ask,"SELECT * FROM ideas WHERE creator=? AND num_shares=(SELECT sum(num_av+num_un) FROM packs WHERE id_user=? AND id_idea = ideas.id)",username,username);
		if(got.getRow()>0){
			int i;
			for(i=0;i<got.getRow();i++){
				info += ((got.pollData())+"\n\n");
			}
		}

		answer.addData(info);
		Utils.writeData(answer,oos);
		Utils.print("Resposta ao pedido enviada!");
		return;
	}

	private void seePacks(Data data) throws RemoteException {				// id = 16
		int id_idea = Integer.parseInt(data.pollData());
		Utils.print("Pedido de informacoes de packs");
		Data answer = new Data();
		answer.setOrder(-3);
		String info = new String();

		DBData ask = new DBData();
		ask.setOrder(104); // receber packs
		DBData got = rmi_server.db_query_answer(ask,"SELECT * FROM packs WHERE id_idea=?",id_idea);
		if(got.getRow()>0){
			int i;
			for(i=0;i<got.getRow();i++){
				info += ((got.pollData())+"\n\n");
			}
		}

		answer.addData(info);
		Utils.writeData(answer,oos);
		Utils.print("Informacoes dos packs enviadas!");
		return;
	}

	private void buyLater(Data data) throws RemoteException {				// id = 17
		int id_idea = Integer.parseInt(data.pollData());
		int num_acc = Integer.parseInt(data.pollData());
		int price = Integer.parseInt(data.pollData());
		Utils.print("Pedido de agendamento de compra de accoes");
		Data answer = new Data();
		answer.setOrder(1);

		rmi_server.db_query("INSERT INTO history (user1,user2,num_acc,id_idea,price,done,read) VALUES (?,?,?,?,?,?,?)",username,"",num_acc,id_idea,price,0,0);
		
		Utils.writeData(answer,oos);
		Utils.print("Agendamento efectuado com sucesso!");
		return;
	}


	private void clientByeBye(){											// id = -2
		Data answer = new Data();
		answer.setOrder(-2);
		answer.addData("CONNECTION_TERMINATED_BY_USER");
		Utils.writeData(answer,oos);
		changeUsername("");
		return;
	}

	private void changeUsername (String u) {
		if (u.equals("")) {
			this.not.removeClient(c);
			this.c = null;
			username = "";
		}
		else {
			this.not.addClient(new Client(u, oos));
			Utils.print("Adicionei-me à lista!");
			username = u;
		}
	}

	public void run () {

		Boolean exit = true;
		Data data = new Data();

		Utils.print("Servico inicializado para o cliente!");

		try{
			while(exit) {

				data = (Data) ois.readObject();
				if (!data.equals(backup)) {

					switch (data.getOrder()) {
						case -2: clientByeBye(); exit = false; break;
						case -1: changeUsername(data.pollData()); break;
						case 0: regist(data); break;
						case 1: login(data); break;
						case 2: wallet(data); break;
						case 3: histTrans(data); break;
						case 4: createTopic(data); break;
						case 5: createIdea(data); break;
						case 6: deleteIdea(data); break;
						case 7: seeTopics(data); break;
						case 8: seeIdeas(data); break;
						case 9: seeSubIdeas(data); break;
						case 10: infoIdea(data); break;
						case 11: changeSharesValue(data); break;
						case 12: buyShares(data,0); break;
						case 13: makeAvailable(data); break;
						case 14: makeUnvailable(data); break;
						case 15: getMyIdeas(data); break;
						case 16: seePacks(data); break;
						case 17: buyLater(data); break;
					}
					backup = data;
				}

			}
		} catch(ClassNotFoundException e) {
		} catch(EOFException e){
			Utils.print("CLIENT_DISCONECTED : SOCKET_CLOSED");
		} catch(IOException e){
			System.out.println("IO:" + e);
		}

		Utils.print("Thread is going to die!");
	}
}



