package terminal;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.Scanner;
import java.util.InputMismatchException;

class TCPClient {

	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private InputStreamReader input;
	private BufferedReader reader;
	private ClientRead receiver;
	private Object lock = new Object();
	private int tries = 0;
	private InetAddress aH;
	private int aP;
	private InetAddress bH;
	private int bP;
	private String userName = "";

	public TCPClient (String [] args) {

		if (args.length == 0) {
			System.out.println("java TCPClient hostname port hostname2 port2");
			System.exit(0);
		}

		try {
			this.aH = InetAddress.getByName(args[0]);
			this.aP = Integer.parseInt(args[1]);
			this.bH = InetAddress.getByName(args[2]);
			this.bP = Integer.parseInt(args[3]);
		} catch (UnknownHostException e) {
		}

		connect(false);

		//p = new Ping(this, aH, aP, bH, bP);
		
		Utils.print("SOCKET=" + s);

		String texto = "";
		input = new InputStreamReader(System.in);
		reader = new BufferedReader(input);

		welcomeMenu();
	}

	public static void main (String [] args) {

		new TCPClient(args);
	}

	public void connect (Boolean reconnect) {

		while (tries < 10000000) {
			try {
				this.s = new Socket(aH, aP);
				oos = new ObjectOutputStream(s.getOutputStream());
				ois = new ObjectInputStream(s.getInputStream());

				if (reconnect){
					receiver.setInputStream(ois); 
					Data data = new Data();
					data.setOrder(-1);
					data.addData(userName);
					Utils.writeData(data, oos);
					return;	
				} 
				else{
					receiver = new ClientRead(this, ois, lock);
					return;
				}

			} catch (SocketException e) {
				try {
					this.s = new Socket(bH, bP);
					oos = new ObjectOutputStream(s.getOutputStream());
					ois = new ObjectInputStream(s.getInputStream());

					if (reconnect){
						receiver.setInputStream(ois);
						Data data = new Data();
						data.setOrder(-1);
						data.addData(userName);
						Utils.writeData(data, oos);
						return;	
					} 
					else{
						receiver = new ClientRead(this, ois, lock);
						return;
					}

				} catch (SocketException se) {
					tries++;
				} catch (IOException ie) {
				}
			}catch (IOException e) {
			}
		}
		System.out.println("Servidores em baixo. Tente mais tarde!");
		System.exit(0);
	}

	private void welcomeMenu () {
		int op;
		Boolean running = true;
		while (running) {
			Utils.clearConsole();
			System.out.print("********************************************\n*                IdeaBroker                *\n********************************************\n\n");
			System.out.print("1 - Entrar\n2 - Registar\n\n0 - Sair\n\nOpção: ");
			op = readOption();

			switch (op) {
				case 1: login(); break;
				case 2: register(); break;
				case 0: running = false; exit(); 
			}
		}
		return;
	}

	private void exit () {
		/*Data data2 = new Data();
		userName = "";
		data2.setOrder(-1);
		data2.addData(userName);
		Utils.writeData(data2, oos);
		locker();*/
		
		Data data = new Data();
		data.setOrder(-2);
		Utils.writeData(data, oos);
		locker();
		try {
			receiver.join();
			oos.close();
			ois.close();
			s.close();
		} catch (IOException e) {
		} catch (InterruptedException ie) {
		}
		return;
	}

	private void register () {
		Data data = new Data();
		Data answer = new Data();
		data.setOrder(0);
		Utils.clearConsole();
		System.out.println("Registo\n");
		System.out.print("Username: ");
		data.addData(readKeyboard());
		System.out.print("Password: ");
		data.addData(readKeyboard());

		Utils.writeData(data, oos);
		while ((answer = receiver.getData()) == null) {
			locker();
		}

		if (answer.getOrder() == 1) {
			System.out.println("\nRegisto efectuado com sucesso.\n");
		}
		else {
			System.out.println("\nErro ao efectuar o registo - username já existente.\n");
		}
		cont();
		return;
	}

	private void login () {
		Data data = new Data();
		Data answer = new Data();
		String user;
		Utils.clearConsole();
		data.setOrder(1);
		System.out.println("Login\n");
		System.out.print("Username: ");
		data.addData(user = readKeyboard());
		System.out.print("Password: ");
		data.addData(readKeyboard());

		Utils.writeData(data, oos);
		while ((answer = receiver.getData()) == null) {
			locker();
		}

		if (answer.getOrder() == 1) {
			System.out.println("\nLogin efectuado com sucesso.\n");
			userName = user;
			mainMenu();
		}
		else {
			System.out.println("\nErro ao efectuar login.\n");
			//login();
			cont();
		}
		return;
	}

	private void mainMenu () {
		Boolean running = true;
		while (running) {
			Utils.clearConsole();
			int op;
			System.out.println("Menu Principal\n");
			System.out.println("1 - Carteira");
			System.out.println("2 - Histórico de Transacções");
			System.out.println("3 - Criar Tópico");
			System.out.println("4 - Criar Ideia");
			System.out.println("5 - Eliminar Ideia");
			System.out.println("6 - Navegar");
			System.out.println("\n0 - Logout");
			System.out.print("\nOpção: ");
			op = readOption();
			switch (op) {
				case 1: walletMenu(); break;
				case 2: transactions(); break;
				case 3: newTopic(); break;
				case 4: newIdea(); break;
				case 5: deleteIdea(); break;
				case 6: navigate(); break;
				case 0: running = false;
			}
		}
	}

	private void transactions () {
		Data data = new Data();
		data.setOrder(3);
		Utils.clearConsole();
		Utils.writeData(data, oos);
		locker();
		System.out.println("Prima 0 para voltar: ");
		while(readOption() != 0);
		return;
	}

	private void newTopic () {
		Data data = new Data();
		Utils.clearConsole();
		data.setOrder(4);
		System.out.println("Criar Novo Tópico\n");
		System.out.print("Nome do novo tópico: ");
		data.addData(readKeyboard());

		Utils.writeData(data, oos);
		while ((data = receiver.getData()) == null) {
			locker();
		}

		if (data.getOrder() == 1) {
			System.out.println("Tópico Criado com Sucesso");
		}
		else {
			System.out.println("Nome do tópico já existente");
		}
		back();
		return;
	}

	private void newIdea () {
		String nAccoes, nAAccoes;
		File f = null;
		Data data = new Data();
		Utils.clearConsole();
		topicList();

		System.out.println("Criar Nova Ideia\n");
		data.setOrder(5);
		System.out.print("ID do(s) tópico(s) ao qual pertende associar a ideia (separar com espaços): ");
		data.addData(readKeyboard());
		data.addData("-1"); // -1 porque nao tem ideia superior!
		System.out.print("Nome/título da nova ideia: ");
		data.addData(readKeyboard());
		System.out.print("Descrição da nova ideia: ");
		data.addData(readKeyboard());
		System.out.print("Número de acções a atribuir à ideia: ");
		data.addData(nAccoes = readKeyboard());
		System.out.print("Preço de cada acção: ");
		data.addData(readKeyboard());
		System.out.print("Numero de acções disponíveis para venda: ");
		nAAccoes = readKeyboard();
		while (Integer.parseInt(nAAccoes) > Integer.parseInt(nAccoes)){
			System.out.println("ERRO: o número de acções disponíveis para venda não deve ser maior que o número total de acções.");
			System.out.println("Numero de acções disponíveis para venda: ");
			nAAccoes = readKeyboard();
		}
		data.addData(nAAccoes);
		data.addData("0"); //relaçao com main idea
		System.out.print("Deseja anexar um ficheiro à sua ideia? (0 - Não; 1 - Sim): ");
		if(readOption() == 1) {
			data.addData("1");
			Utils.writeData(data, oos);
			System.out.print("Insira a directoria do ficheiro: ");
			String directory = readKeyboard();
			getFile(directory);
		}
		else {
			data.addData("0");
			Utils.writeData(data, oos);
		}

		
		while ((data = receiver.getData()) == null) {
			locker();
		}
		if (data.getOrder() == 1) {
			System.out.println("Ideia Criada com Sucesso");
		}
		else {
			System.out.println("Erro ao criar a ideia");
		}
		back();
		return;
	}

	private void newIdea (int ideaID) {
		String nAccoes, nAAccoes;
		Data data = new Data();
		int op;
		File f = null;

		Utils.clearConsole();

		System.out.println("Criar Nova Ideia\n");
		data.setOrder(5);
		data.addData("-1"); // -1 porque as subideias nao estao directamente ligadas ao topico
		data.addData(Integer.toString(ideaID)); //ID da ideia - a ser atribuido automaticamente
		System.out.print("Nome/título da nova ideia: ");
		data.addData(readKeyboard());
		System.out.print("Descrição da nova ideia: ");
		data.addData(readKeyboard());
		System.out.print("Número de acções a atribuir à ideia: ");
		data.addData(nAccoes = readKeyboard());
		System.out.print("Preço de cada acção: ");
		data.addData(readKeyboard());
		System.out.print("Numero de acções disponíveis para venda: ");
		nAAccoes = readKeyboard();
		while (Integer.parseInt(nAAccoes) > Integer.parseInt(nAccoes)){
			System.out.println("ERRO: o número de acções disponíveis para venda não deve ser maior que o número total de acções.");
			System.out.println("Numero de acções disponíveis para venda: ");
			nAAccoes = readKeyboard();
		}
		data.addData(nAAccoes);
		System.out.println("Relação com a main idea:");
		System.out.println("1 - Contra");
		System.out.println("2 - A favor");
		System.out.println("3 - Neutra");
		data.addData(readKeyboard());

		System.out.print("Deseja anexar um ficheiro à sua ideia? (0 - Não; 1 - Sim): ");
		if(readOption() == 1) {
			data.addData("1");
			Utils.writeData(data, oos);
			System.out.print("Insira a directoria do ficheiro: ");
			String directory = readKeyboard();
			getFile(directory);
		}
		else {
			data.addData("0");
			Utils.writeData(data, oos);
		}

		while ((data = receiver.getData()) == null) {
			locker();
		}
		if (data.getOrder() == 1) {
			System.out.println("Ideia Criada com Sucesso");
		}
		else {
			System.out.println("Erro ao criar a ideia");
		}
		back();
		return;
	}

	private void deleteIdea () {
		Data data = new Data();
		Utils.clearConsole();
		data.setOrder(15);
		Utils.writeData(data, oos);
		locker();

		data.setOrder(6);
		System.out.print("Introduza o ID da ideia que pertende eliminar: ");
		data.addData(readKeyboard());
		Utils.writeData(data, oos);
		while ((data = receiver.getData()) == null) {
			locker();
		}

		if (data.getOrder() == 1) {
			System.out.println("Ideia eliminada com sucesso");
		}
		else {
			System.out.println("Erro ao eliminar ideia - o user não é dono de 100% das acções.");
		}
		back();
		return;
	}

	private void navigate () {
		int op;
		int top;
		//pede os topicos
		topicList();
		//pede id do topico
		System.out.println("ID do tópico que pertende consultar (0 para voltar): ");
		top = readOption();
		while (top != 0) {
			//pede lista ideias do topico
			ideaList(top);
			System.out.println("ID da ideia que pertende consultar (0 para voltar): ");
			op = readOption();
			while (op != 0) {
				//comprar|responder|ver subideias
				ideaOption (op);
				Utils.clearConsole();
				ideaList(top);
				//pede id da ideia
				System.out.println("ID da ideia que pertende consultar (0 para voltar): ");
				op = readOption();
			}
			topicList();
			System.out.println("ID do tópico que pertende consultar (0 para voltar): ");
			top = readOption();
		}
		return;
	}

	private void topicList () {
		Data data = new Data();
		Utils.clearConsole();
		System.out.println("Lista dos tópicos existentes:\n");
		data.setOrder(7);
		Utils.writeData(data, oos);
		locker();
		return;
	}

	private void ideaList (int topicID) {
		Data data = new Data();
		Utils.clearConsole();
		System.out.println("Lista de ideias associadas ao tópico:\n");
		data.setOrder(8);
		data.addData(Integer.toString(topicID));
		Utils.writeData(data, oos);
		locker();
		return;
	}

	private void ideaOption (int ideaID) {
		int op;
		Boolean running = true;
		Data data = new Data();
		Data data2 = new Data();
		while (running) {
			Utils.clearConsole();
			data.setOrder(10);
			data.addData(Integer.toString(ideaID));
			Utils.writeData(data, oos);
			locker();
			System.out.println("\n\n\tPacks de acções desta ideia:\n\n");
			data2.setOrder(16);
			data2.addData(Integer.toString(ideaID));
			Utils.writeData(data2, oos);
			locker();

			System.out.println("\n1 - Comprar acções desta ideia ");
			System.out.println("2 - Responder a esta ideia ");
			System.out.println("3 - Ver sub ideias ");
			System.out.println("4 - Agendar uma compra mais barata ");
			System.out.println("\n0 - Voltar ");
			System.out.print("\nOpção: ");
			op = readOption();
			switch (op) {
				case 1: buyShares(ideaID); break;
				case 2: reply(ideaID); break;
				case 3: subIdeas(ideaID); break;
				case 4: setFutureBuy(ideaID); break;
				case 0: running = false;
			}
		}
		return;
	}

	private void buyShares (int ideaID) {
		Data data = new Data(); 
		data.setOrder(12);
		System.out.println("Comprar acções da ideia " + ideaID + "\n");
		System.out.print("Introduza o ID do pack onde pertende comprar acções: ");
		data.addData(readKeyboard());
		data.addData(Integer.toString(ideaID));
		System.out.print("Introduza o número de acçõe que pertende comprar: ");
		data.addData(readKeyboard());

		Utils.writeData(data, oos);

		while ((data = receiver.getData()) == null) {
			locker();
		}
		if (data.getOrder() == 1) {
			System.out.println("Acçoẽs compradas com sucesso");
		}
		else {
			System.out.println("Erro ao comprar acções");
			System.out.println(data.pollData());
		}
		back();
		return;
	}

	private void setFutureBuy (int ideaID) {
		Data data = new Data(); 
		data.setOrder(17);
		System.out.println("Comprar acções da ideia " + ideaID + "\n");
		data.addData(Integer.toString(ideaID));
		System.out.print("Introduza o número de acçõe que pertende comprar: ");
		data.addData(readKeyboard());
		System.out.println("Esta funcionalidade tem o objectivo de permitir ao utilizador comprar acções mais baratas do que as disponiveis!");
		System.out.println("Atenção!!! Mesmo que introduza um preço superior ao mais barato,\n"+
						   "a compra só vai ser efectuada quando houver modificações nos packs desta idea!");
		System.out.print("Introduza o preço a que deseja comprar: ");
		data.addData(readKeyboard());

		Utils.writeData(data, oos);

		while ((data = receiver.getData()) == null) {
			locker();
		}
		if (data.getOrder() == 1) {
			System.out.println("Compra agendada com sucesso");
		}
		else {
			System.out.println("Erro ao agendar compra");
		}
		back();
		return;
	}

	private void reply (int ideaID) {
		newIdea(ideaID);
		return;
	}

	private void subIdeas (int ideaID) {
		int op;
		Data data = new Data();
		Utils.clearConsole();
		data.setOrder(9);
		data.addData(Integer.toString(ideaID));
		Utils.writeData(data, oos);
		locker();

		System.out.print("ID da ideia que pertende consultar (0 para voltar): ");
		op = readOption();
		//comprar|responder|ver subideias
		if (op == 0) return;
		
		ideaOption (op);
		subIdeas(ideaID);
		Utils.clearConsole();
	}

	private void walletMenu () {
		int op;
		Boolean running = true;
		Data data = new Data();
		while (running) {
			Utils.clearConsole();
			data.setOrder(2);
			Utils.writeData(data, oos);

			locker();

			System.out.println("\n0 - Voltar");
			System.out.print("\nOpção: ");
			op = readOption();

			switch (op) {
				case 1: alterPrice(); break;
				case 2: trunAvailable(); break;
				case 3: turnUnvailable(); break;
				case 0: running = false;
			}
		}
		return;
	}

	private void alterPrice () {
		Data data = new Data();
		System.out.println("Alterar preço de venda de um pacote de acções\n\n");
		data.setOrder(11);
		System.out.print("Introduza o ID do pacote: ");
		data.addData(readKeyboard());
		System.out.print("Introduza o preço a que pertende vender as acções: ");
		data.addData(readKeyboard());
		Utils.writeData(data, oos);
		while ((data = receiver.getData()) == null) {
			locker();
		}
		if (data.getOrder() == 1) {
			System.out.println("Preço das acções alterado com sucesso");
		}
		else {
			System.out.println("Erro ao alterar o preço das acções");
		}
		back();
		return;
	}

	private void trunAvailable () {
		Data data = new Data();
		System.out.println("Tornar acções disponíveis para venda\n\n");
		data.setOrder(13);
		System.out.print("Introduza o ID do pacote: ");
		data.addData(readKeyboard());
		System.out.print("Introduza o número de acções que pertende disponibilizar para venda: ");
		data.addData(readKeyboard());
		Utils.writeData(data, oos);
		while ((data = receiver.getData()) == null) {
			locker();
		}
		if (data.getOrder() == 1) {
			System.out.println("Acções colocadas à venda com sucesso");
		}
		else {
			System.out.println("Erro ao colocar acções à venda - Nota: Número de acções indisponível");
		}
		back();
		return;
	}

	private void turnUnvailable () {
		Data data = new Data();
		System.out.println("Tornar acções indisponíveis para venda\n\n");
		data.setOrder(14);
		System.out.print("Introduza o ID do pacote: ");
		data.addData(readKeyboard());
		System.out.print("Introduza o número de acções que pertende indisponibilizar para venda: ");
		data.addData(readKeyboard());
		Utils.writeData(data, oos);
		while ((data = receiver.getData()) == null) {
			locker();
		}
		if (data.getOrder() == 1) {
			System.out.println("Acções colocadas à venda com sucesso");
		}
		else {
			System.out.println("Erro ao colocar acções à venda - Nota: Número de acções indisponível");
		}
		back();
		return;
	}

	private int readOption () {
		Scanner sc = new Scanner(System.in);
		try{
			return sc.nextInt();
		} catch (InputMismatchException e){
			System.out.print("Opção Inválida.\nOpção: ");
			return readOption();
		}
	}

	private String readKeyboard () {
		try {
			return reader.readLine();
		} catch (Exception e) {
		}
		return "fail";
	}

	private void locker () {
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {}
		}
	}

	private void back () {
		System.out.println("Insira 0 para voltar");
		while(readOption() != 0) {
			System.out.println("Insira 0 para voltar");
		}
	}

	private void cont () {
		System.out.println("Insira 1 para continuar");
		while(readOption() != 1) {
			System.out.println("Insira 1 para continuar");
		}
	}

	public Socket getSocket () {
		return this.s;
	}

	public void getFile (String path) {
		File f = new File(path);
		byte[] bFile = new byte[(int) f.length()];
		try {
			InputStream inputStream = new FileInputStream(f);
			inputStream.read(bFile);
			inputStream.close();
			oos.writeObject(bFile);
		} catch (FileNotFoundException e) {
			System.out.println("O ficheiro que tentou carregar não existe. Indique o directório correcto do ficheiro");
			System.out.print("Insira a directoria do ficheiro: ");
			getFile(readKeyboard());
			return;
		} catch (IOException ioe) {
		} 
		return;
	}
}