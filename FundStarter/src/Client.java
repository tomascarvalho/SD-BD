
import java.net.*;
import java.util.*;
import java.io.*;

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
public class Client {

    private Socket conectionToServer;
    private ObjectInputStream reciver;
    private ObjectOutputStream sender;
    private int conectionError = 0;
    private String ipServer1, ipServer2;
    private int port1, port2;
    private Object[] postCard = new Object[4];
    private ArrayList<ClientRequest> myRequest = new ArrayList<ClientRequest>();
    private PropertiesReaderClient properties;
    private int myId;
    private Scanner sc = new Scanner(System.in);
    private String[] myCredentials = new String[2];

    public Client() throws ClassNotFoundException {
        /**
         * Cria uma nova intancia do PropertiesReader para aceder aos dados que estão no ficheiro configClient.properties
         */

        properties = new PropertiesReaderClient();
        ipServer1 = properties.getPrimaryIP();
        ipServer2 = properties.getSecundaryIP();
        port1 = properties.getPrimaryPort();
        port2 = properties.getSecundaryPort();

        connectionFunction(false);

        try {

            mainMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Função responsável por fazer a conecção do servidor
     */
    public void connectionFunction(boolean reconnect) {

        int portTemp;
        String ipTemp;
        while (true) {
            try {

                /**
                 * cria ligação com o servidor que está com o os dados retirados do ficheiro de propriedades
                 */
                System.out.println("[Cliente] Conectando ao Servidor");
                conectionToServer = new Socket(ipServer1, port1);

                /**
                 * vai iniciar os streams de input e output para partilhar mensagens com o servidor
                 */
                sender = new ObjectOutputStream(conectionToServer.getOutputStream());
                sender.flush();

                reciver = new ObjectInputStream(conectionToServer.getInputStream());

                if (reconnect) {
                    System.out.println("[Cliente]Vou fazer o login de novo");//apagar depois
                    reLog();
                   // System.out.println("[Cliente]Vou pedir ao rmi para ver os meus pedidos");//apagar depois
                   // checkRequest();
                }

                return;

            } catch (IOException e) {

                if (conectionError == 3) {
                    System.out.println("[Cliente] Não me consigo ligar ao servidor, vou-me ligar ao secundário");
                    try {
                        /**
                         * Ao fim de 3 tentativas de ligação ao servidor principal o cliente tenta ligar ao servidor de backup
                         */
                        conectionToServer = new Socket(ipServer2, port2);
                        ipTemp = ipServer2;
                        portTemp = port2;
                        ipServer2 = ipServer1;
                        port1 = port2;
                        ipServer1 = ipTemp;
                        port1 = portTemp;
                        /**
                         * Vai alterar os ips dos servidores no ficheiro de propriedades
                         */
                        properties.writeOnFile();
                        conectionError = 0;

                        if (reconnect) {
                            System.out.println("[Cliente]Vou fazer o login de novo");//apagar depois
                            reLog();
                            System.out.println("[Cliente]Vou pedir ao rmi para ver os meus pedidos");//apagar depois
                            checkRequest();
                        }

                        return;
                    } catch (IOException exp) {
                        System.out.println("[Cliente] Não me consigo ligar ao Servidor de Backup.");
                    }
                } else {
                    conectionError++;
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

    }

    public void reLog() throws IOException {
        System.out.println("[reLog]Fui chamdo");
        while (true) {
            System.out.println("[reLog]Passei aqui");
            postCard[0] = "log";
            postCard[1] = myCredentials;

            postCard = postOffice(postCard);
            System.out.println("Fiz o login....");
            if(postCard[0].equals("log_in_correcto")){
                System.out.println("I'm Back Bitches...");
                break;
            }
        }
    }

    public void checkRequest() throws IOException {

        int lastRequestIndex = myRequest.size() - 1;

        System.out.println("Last Request->" + myRequest.get(lastRequestIndex).getRequest()[0]);

        if (!myRequest.get(lastRequestIndex).getStage().equals("done")) {
            postCard[0] = "see_last_request";
            postCard[1] = myRequest.get(lastRequestIndex).getRequestID();

            postCard = postOffice(postCard);
        } else {
            try {
                menuConta();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("Resposta do RMI->" + postCard[1]);
    }

    /**
     * Funçao responsável para eviar os request para o servidor
     */
    public Object[] postOffice(Object[] mail) {

        ClientRequest newRequest;
        ClientRequest newResponse;
        String requestID;
        Date requestDate = new Date();

        try {

            if (myRequest.size() == 0) {
                requestID = "1_" + myId;
            } else {
                requestID = "" + (myRequest.size() + 1) + "_" + myId;
            }

            newRequest = new ClientRequest(requestID, postCard, requestDate.toString());
            myRequest.add(newRequest);
            newRequest.setStage(0);
            System.out.println("Vou enviar cenas");
            sender.reset();
            sender.writeUnshared(newRequest);

            newResponse = (ClientRequest) reciver.readObject();

            newResponse.setStage(5);
            updateRequest(newRequest, newResponse);

            return newResponse.getResponse();

        } catch (IOException ex) {
            connectionFunction(true);
        } catch (ClassNotFoundException ex) {
            System.out.println("Classe não encontrada!");
        }

        return null;
    }

    public static void main(String[] args) throws ClassNotFoundException {

        new Client();

    }

    /**
     * Função responsável por actualizar os pedidos depois de eles serem tratdos pelo servidor RMI
     */
    private void updateRequest(ClientRequest oldrqst, ClientRequest newrqst) {

        int requestIndex = myRequest.indexOf(oldrqst);

        myRequest.set(requestIndex, newrqst);

    }

    public boolean LogIn(boolean flag) {

        String[] person = new String[2];
        sc.nextLine();

        /**
         * Vai pedir credenciais ao cliente para fazer o login. Se os dados não estiverem na base de dados vai voltar a chamar esta função. Caso contrário vai avançar para o menu inicial
         */
        if (!flag) {
            System.out.println("Utilizador não reconhecido!!!");
            /*meter uma opção para voltar ao menu inicial e/ou fazer inscrição*/
        }

        System.out.println("\n\t\tLogIn");
        System.out.print("\tUsername:");
        person[0] = sc.nextLine();
        System.out.print("\tPassword:");
        person[1] = sc.nextLine();

        postCard[0] = "log";
        postCard[1] = person;

        postCard = postOffice(postCard);

        if (postCard[0].equals("log_in_correcto")) {
            myId = (int) postCard[1];
            myCredentials[0] = person[0];
            myCredentials[1] = person[1];
            System.out.println("Credenciais Correctas");
            return true;
        } else {
            return false;
        }

    }

    public boolean criaConta() {

        String[] newUserData = new String[2];

        System.out.print("\t\tUsername:");
        newUserData[0] = sc.nextLine();
        System.out.print("\t\tPassword:");
        newUserData[1] = sc.nextLine();

        postCard[0] = "new";
        postCard[1] = newUserData;

        postCard = postOffice(postCard);

        if (postCard[0].equals("infosave")) {
            myId = (int) postCard[1];
            myCredentials[0] = newUserData[0];
            myCredentials[1] = newUserData[1];
            return true;
        } else {
            return false;
        }

    }
    
      public void enviaMensagem (int idProjecto){
        System.out.println("Digite a sua mensagem:\n");
        Scanner sc = new Scanner(System.in);
        String mensagem = sc.nextLine();
          
          
        postCard[0] = "send_mess";
        postCard[1] = idProjecto;
        postCard[2] = mensagem;
        postCard = postOffice(postCard);
        System.out.println(postCard[0]);
        
    }
      
      public void respondeMensagem(int idResposta){
        System.out.println("Digite a sua mensagem:\n");
        Scanner sc = new Scanner(System.in);
        String resposta = sc.nextLine();

        postCard[0] = "resp_mess";
        postCard[1] = idResposta;
        postCard[2] = resposta;
        postCard = postOffice(postCard);
        System.out.println(postCard[0]);
      }
      
      public void caixaCorreio(){
          System.out.println("Caixa de Correio:");
          
          postCard[0] = "mailbox";
          postCard = postOffice(postCard);
          
          Object[] objecto = postCard;
          ArrayList<ArrayList<Integer>>  listaPerguntas= (ArrayList<ArrayList<Integer>>)postCard[0];
          ArrayList <String> perguntas = (ArrayList<String>) postCard[1];
          int tamanho = listaPerguntas.size();
          int i,j;
          
          for(i=0; i<tamanho; i++){
              if(listaPerguntas.get(i).isEmpty()==false){
                System.out.println("ID do Projecto:  " + listaPerguntas.get(i).get(0));
                int novotam = listaPerguntas.get(i).size();
                System.out.println("Perguntas associadas:");
                for(j=1; j<novotam; j++){
                    System.out.print(listaPerguntas.get(i).get(j) + ":\t" + perguntas.get(j) + "\n");
                }
            }
          }
          
          
          System.out.print("ID da mensagem a responder: ");
          int idResposta = sc.nextInt();
          
          respondeMensagem(idResposta);
             
      }
    

    public boolean consultaSaldo() {

        postCard[0] = "seesal";
        postCard[1] = null;

        postCard = postOffice(postCard);

        System.out.println("\t\tO seu saldo é de " + postCard[0] + " euros.");

        return true;
    }

    public boolean criaProjecto() {

        String[] newProjectData = new String[2000];
        String constroi_data_limite;
        int num_recompensas = 0;
        int num_niveis_extra = 0;
        int num_produto = 0;
        int i = 0;
        int j = 0;
        String montante;
        System.out.println("\n\t\tNovo Projecto");
        System.out.println("\n\tNome do Projecto: ");
        newProjectData[0] = sc.nextLine(); //Titulo do Projecto
        System.out.println("\n\tDescrição do Projecto: ");
        newProjectData[1] = sc.nextLine(); //Descrição do Projecto
        System.out.println("\n\tValor Pretendido: ");
        newProjectData[2] = sc.nextLine(); //Valor Pretendido para o Projecto
        System.out.println("\tData limite para a conclusão do Projecto(yyyy-mm-dd): ");
        newProjectData[3] = sc.nextLine(); //Data Limite
        System.out.println("\n\tQuantas Recompensas quer oferecer: ");
        //while(!sc.hasNextInt()) sc.next();
        num_recompensas = sc.nextInt();
        sc.nextLine();
        newProjectData[4] = "" + num_recompensas;
        while (i < num_recompensas) {
            i++;
            j++;
            System.out.println("Recompensa nº" + i);
            System.out.println("Montante a doar: ");
            montante = sc.nextLine();
            newProjectData[4 + j] = montante;
            j++;
            System.out.println("Recompensa associada: ");
            newProjectData[4 + j] = sc.nextLine();
        }
        j++;
        System.out.println("\n\t Quantos níveis extra existem: ");
        //while(!sc.hasNextInt()) sc.next();
        num_niveis_extra = sc.nextInt();
        sc.nextLine();
        newProjectData[4 + j] = "" + num_niveis_extra;
        i = 0;
        while (i < num_niveis_extra) {
            i++;
            j++;
            System.out.println("Nivel Extra nº" + i + "\nValor: ");
            //while(!sc.hasNextInt()) sc.next();
            montante = sc.nextLine();
            newProjectData[4 + j] = montante;
            j++;
            System.out.println("Nivel Extra: ");
            newProjectData[4 + j] = sc.nextLine();
        }
        j++;
        System.out.println("\n\t Quantos tipos diferentes de produto existem: ");
        //while(!sc.hasNextInt()) sc.next();
        num_produto = sc.nextInt();
        sc.nextLine();
        newProjectData[4 + j] = "" + num_produto;
        i = 0;
        while (i < num_produto) {
            i++;
            j++;
            System.out.println("Tipo de Produto: ");

            newProjectData[4 + j] = sc.nextLine();

        }
        postCard[0] = "new_project";
        postCard[1] = newProjectData;

        postCard = postOffice(postCard);

        if (postCard[0].equals("infosave")) {
            return true;
        } else {
            return false;
        }
    }
    
  

    public void listarProjectosActuais(int choice, int logged) throws IOException, ClassNotFoundException { // if choice 0 -> active projects
        // if choice 1 -> old projects

        // 0 - I am not logged
        // 1 - I am logged
        String array_projectos[];
        String titulo, id, valoractual, valorpretendido;
        if (choice == 0) {
            postCard[0] = "list_actual_projects";
        } else {
            postCard[0] = "list_old_projects";
        }
        postCard[1] = choice;

        postCard = postOffice(postCard);

        String numero_de_iteracoes;
        numero_de_iteracoes = "" + postCard[1];
        int i = Integer.parseInt(numero_de_iteracoes);
        int j = 0;

        array_projectos = (String[]) postCard[0];

        if ((!array_projectos[0].equals("error_no_active_projects")) || (!array_projectos[0].equals("error_no_old_projects"))) {
            if (choice == 0) {
                System.out.println("Projectos Actuais: ");
            } else {
                System.out.println("Projectos Antigos: ");
            }

            while (j < i) {

                id = array_projectos[j];
                j++;
                titulo = array_projectos[j];
                j++;
                valoractual = array_projectos[j];
                j++;
                valorpretendido = array_projectos[j];
                j++;
                if (choice == 0) {
                    System.out.println("ID: " + id + " Titulo: " + titulo + " Progresso: " + valoractual + " euros angariados / " + valorpretendido + " euros pretendidos");
                } else {
                    System.out.println("ID: " + id + " Titulo: " + titulo);
                }
            }

            if (choice == 0) {
                System.out.println("1 - Consultar detalhes de um projcto");

                System.out.println("2 - Voltar ao Menu");
                System.out.print(">>>");
                choice = sc.nextInt();

                while ((choice != 1) && (choice != 2) ) {


                    System.out.println("1 - Consultar detalhes de um projecto");
                    System.out.println("2 - Enviar mensagem a um projecto");
                    System.out.println("3 - Voltar ao Menu");
                    choice = sc.nextInt();
                }
                if (choice == 1) {
                    System.out.println("ID do projecto a consultar: ");
                    choice = sc.nextInt();
                    consultarDetalhesProjecto(choice, logged);
                }

                if (logged == 0) {
                    mainMenu();
                } else {
                    menuConta();
                }

            }
        } else {
            if (choice == 1) {
                System.out.println("Não há projectos activos!");
            } else {
                System.out.println("Não há projectos antigos!");
            }
        }

    }

    //public void consultarDetalhesProjecto(int id) {

    
    public void consultarDetalhesProjecto(int id, int logged) throws IOException, ClassNotFoundException{
        
        // 0 - I am not logged
        // 1 - I am logged
        int choice = 0;
        postCard[0] = "list_project_details";
        postCard[1] = id;
        postCard = postOffice(postCard);
        Object[] objecto = postCard;
        String[] project_details = new String[2000];
        project_details = (String[]) objecto[0];
        String titulo_projecto, descricao_projecto, valor_pretendido, valor_actual, data, nome_recompensa, valor;
        int num_dados = (int) (postCard[1]);
        int i = 0, num_recompensas = 0, j = 0;

        if (!project_details[0].equals("no_project_to_show")) {
            System.out.println("Detalhes do Projecto " + id);
            titulo_projecto = project_details[i];
            i++;
            descricao_projecto = project_details[i];
            i++;
            valor_pretendido = project_details[i];
            i++;
            valor_actual = project_details[i];
            i++;
            data = project_details[i];
            i++;
            System.out.println("Titulo: " + titulo_projecto + "\n"
                    + "Descricao: " + descricao_projecto + "\n"
                    + "Angariados " + valor_actual + " euros dos " + valor_pretendido + " euros pretendidos\n"
                    + "Data Limite: " + data);

            if (project_details[i].equals("0")) {
                System.out.println("Não há recompensas associadas a este projecto");
            } else {
                num_recompensas = Integer.parseInt(project_details[i]);
                System.out.println("Recompensas associadas a este projecto: ");
                j = 0;
                while (j < num_recompensas) {
                    i++;
                    j++;
                    nome_recompensa = project_details[i];
                    i++;
                    j++;
                    valor = project_details[i];
                    System.out.println("Se doar " + valor + " euros terá direito a...\nRecompensa: " + nome_recompensa);

                }
            }
            i++;
            if (project_details[i].equals("0")) {
                System.out.println("Não há niveis extra associados a este projecto");

            } else {
                num_recompensas = Integer.parseInt(project_details[i]);
                System.out.println("Niveis extra associados a este projecto: ");
                j = 0;
                while (j < num_recompensas) {
                    i++;
                    j++;
                    nome_recompensa = project_details[i];
                    i++;
                    j++;
                    valor = project_details[i];
                    System.out.println("Se o projecto chegar aos " + valor + " euros em doações, implementaremos...\n" + nome_recompensa);
                }

            }
            i++;
            if (project_details[i].equals("0")) {
                System.out.println("Não há diferentes tipos de producto");

            } else {
                num_recompensas = Integer.parseInt(project_details[i]);
                System.out.println("Diferentes possibilidades para o nosso produto: ");
                j = 0;
                while (j < num_recompensas) {
                    i++;
                    j++;
                    nome_recompensa = project_details[i];
                    i++;
                    j++;
                    valor = project_details[i];
                    System.out.println(nome_recompensa + " conta com " + valor + " votos!");
                }

            }
            if (logged == 1){
                System.out.println("\n1 - Doar ao Projecto"
                    + "\n2 - Enviar Mensagem ao Projecto"
                    + "\n3 - Voltar ao Menu");
                System.out.print(">>");
                choice = sc.nextInt();
                
                while ((choice != 1)&&(choice != 2)&&(choice != 3)){
                    System.out.println("\n 1 - Doar ao Projecto"
                    + "\n2 - Enviar Mensagem ao Projecto"
                    + "\n3 - Voltar ao Menu");
                    System.out.print(">>");
                    choice = sc.nextInt();
                }
                if (choice == 1){
                    pledgeToProject(id);
                    
                }
                else if(choice == 2){
                    enviaMensagem(id);
                }
                menuConta();
            }
            
            
            

        } else {
            System.out.println("Sem projectos para mostrar");
        }

    }
    
    public void pledgeToProject(int id){
        int[] how_much__to_who= new int[2];
        System.out.println("Quanto quer doar a este projecto: ");
        how_much__to_who[0] = sc.nextInt();
        how_much__to_who[1] = id;
        postCard[0] = "pledge";
        postCard[1] = how_much__to_who;
        postCard = postOffice(postCard);
        Object[] resposta = postCard;
        String answer = (String)resposta[0];
        int boolean_recompensa = (int)resposta[2];
        String recompensa = (String)(postCard[3]);
        int saldo = (int)(postCard[1]);
        ArrayList <String> product_type = (ArrayList)resposta[4];
        Iterator <String> it = product_type.iterator();
        int conta = 0;
        String produto_votado;
   
        
        if (answer.equals("pledged")){
            System.out.println("Doou com sucesso");
            if (boolean_recompensa == 1){
                System.out.println("Ganhou a seguinte recompensa: "+recompensa);
                System.out.print("Se desejar manter a sua recompensa seleccione 1. Caso a deseje oferecer a outro utilizador seleccione 2\n>>");
                conta = sc.nextInt();
                if (conta == 2){
                    //IMPLEMENTAR DOAR RECOMPENSA donateReward(id_projecto);
                }
            } else{
                System.out.println("Não ganhou recompensas");
            }
            
        }
        else if (answer.equals("sem saldo")){
            System.out.println("O seu saldo não é suficiente para doar!");
            System.out.println("Tem "+saldo+" euros na sua conta!");
        }
        if (product_type.size()>0){
            conta = 0;
            System.out.println("Tem a possibilidade de votar no tipo de produto que o projecto vai produzir: ");
            while(it.hasNext()){
                conta++;
                System.out.println(conta+ " - "+it.next());
            }
            System.out.println("Em que produto deseja votar? Escolher 0 se em nenhum: ");
            conta = sc.nextInt();
            
            if(conta != 0 && conta < product_type.size()+1){
                produto_votado = product_type.get(conta-1);
                System.out.println(produto_votado);
                voteForProduct((String)produto_votado);
            }
        }
    }
    
    public void voteForProduct(String produto_votado){
        
        postCard[0] = "vote_for_product";
        postCard[1] = produto_votado;
        postCard = postOffice(postCard);
    }
    
    public void consultarProjectosUser() throws IOException, ClassNotFoundException{
        
        postCard[0] = "list_my_projects";
        
        postCard = postOffice(postCard);
        
        String userPick;
        int id_projecto_pick;
        int i;
        Object[] objecto = postCard;
        ArrayList<Integer> id_projecto = (ArrayList)postCard[0];
        ArrayList<Integer> titulo_projecto = (ArrayList)postCard[1];
        int tamanho = id_projecto.size();
        

        if (tamanho>0){
            
            System.out.println("Os meus projectos: ");
            
        
            System.out.println("\t\tID\t\t\tTítulo");
            for(i=0; i<tamanho; i++){
                System.out.println(id_projecto.get(i) + "\t\t" + titulo_projecto.get(i));
            }
            System.out.println("\t\t\tMenu Admin\n\n");
            System.out.print("\t\t1 - Adicionar Administrador ao Projecto\n\t\t2 - Cancelar Projectos\n\t\t3 - Consultar Mensagens de um Projecto\n\t\t4 -Voltar ao Menu\n\n\n\t\t>>");
            userPick = sc.nextLine();
            //Verificar Escolhas
            while ((userPick.equals("1") == false) && (userPick.equals("2") == false) && (userPick.equals("3") == false) && (userPick.equals("4") == false)) {
                System.out.println("\nERRO - Escolher uma das opções dadas!!\n");
                System.out.print("\t\t1 - Adicionar Administrador ao Projecto\n\t\t2 - Cancelar Projectos\n\t\t3 - Consultar Mensagens de um Projecto\n\t\t4 -Voltar ao Menu\n\n\n\t\t>>");
                userPick = sc.nextLine();
            }
            if (userPick.equals("1")) {
                System.out.println("ID do projecto ao qual quer adicionar um administrador: ");
                id_projecto_pick = sc.nextInt();
                while(!id_projecto.contains(id_projecto_pick)){
                    System.out.println("Não é admin do projecto que escolheu!");
                    System.out.print("ID do projecto ao qual quer adicionar um administrador: ");
                    id_projecto_pick = sc.nextInt();

                }
                addAdminToProject(id_projecto_pick);

            }
            else if (userPick.equals("2")) {
                System.out.println("ID do projecto a cancelar: ");
                id_projecto_pick = sc.nextInt();
             
                while(!id_projecto.contains(id_projecto_pick)){
                    System.out.println("Não é admin do projecto que escolheu!");
                    System.out.print("ID do projecto a cancelar: ");
                    id_projecto_pick = sc.nextInt();
                    

                }
                cancelarProjecto(id_projecto_pick);
            }
            

        }
        else System.out.println("Não é Administrador de nenhum projecto...");
        menuConta();
    }
    
    public void addAdminToProject(int id_projecto) {
        
        String username;
        sc.nextLine();
        System.out.println("Username do Administrador a acrescentar ao seu projecto: ");
        username = sc.nextLine();
      
        postCard[0] = "add_Admin";
        postCard[1] = username;
        postCard[2] = id_projecto;
        postCard = postOffice(postCard);
        

        Object[] objecto = postCard;
        String status = (String)(objecto[2]);
        if (status.equals("done")){
            System.out.println(username+" foi adicionado como administrador ao projecto id: "+id_projecto);
        }
        else{
            System.out.println("O utilizador "+username+" não existe!");

        }
        
    }
    
    public void cancelarProjecto(int cancelaID) throws IOException, ClassNotFoundException{        

        
        postCard[0] = "delete_project";
        postCard[1] = cancelaID;
        postCard = postOffice(postCard);
        
        System.out.println(postCard[0]);
        
    }


    public void mainMenu() throws IOException, ClassNotFoundException {
        // 0 - I am not logged
        // 1 - I am logged

        boolean logResult = true;
        int userPick;

        conectionError = 0;

        System.out.println("\t\t\tMenu Inicial\n\n");
        System.out.print("\t\t1 - Criar Conta\n\t\t2 - LogIn\n\t\t3 - Consultar Projectos Actuais\n\t\t4 - Consultar Projectos Antigos\n\n\n\t\t>>");
        userPick = sc.nextInt();
        //Verificar Escolhas
        while ((userPick == 1) && (userPick == 2) && (userPick == 3) && (userPick == 4)) {
            System.out.println("\nERRO - Escolher uma das opções dadas!!\n");
            System.out.print("\t\t1 - Criar Conta\n\t\t2 - LogIn\n\t\t3 - Consultar Projectos Actuais\n\t\t4 - Consultar Projectos Antigos\n\n\n\t\t>>");
            userPick = sc.nextInt();
        }
        if (userPick == 1) {
            while (criaConta() != true) {
                System.out.println("Erro ao criar novo user!");
            }
        } else if (userPick == 2) {

            if (!LogIn(logResult)) {
                logResult = false;
                System.out.println("Username ou Password erradas");
                mainMenu();
                

            }
            menuConta();

        } else if (userPick == 3) {
            listarProjectosActuais(0, 0);
        } else if (userPick == 4) {
            listarProjectosActuais(1, 0);
        }
        mainMenu();

    }

    public void menuConta() throws IOException, ClassNotFoundException {
        
        // 0 - I am not logged
        // 1 - I am logged
        
        
        
        int userPick;

        System.out.println("\t\t\tMenu Inicial\n\n");
        System.out.print("\t\t1 - Consultar Saldo\n\t\t2 - Criar Projecto\n\t\t3 - Listar Projectos Actuais\n\t\t4 - Listar Projectos Antigos\n\t\t5 - Listar os meus projectos\n\t\t6 - Caixa de Correio\n\n\n\t\t>>");
        userPick = sc.nextInt();

        //Verificar Escolhas. Inserir novos casos quando forem inseridas novas funções
        while ((userPick!= 1) && (userPick != 2) && (userPick != 3) && (userPick!= 4) && (userPick != 5) && (userPick!=6)) {
            System.out.println("\nERRO - Escolher uma das opções dadas!!\n");
            System.out.print("\t\t1 - Consultar Saldo\n\t\t2 - Criar Projecto\n\t\t3 - Listar Projectos Actuais\n\t\t4 - Listar Projectos Antigos\n\t\t5 - Listar os meus projectos\n\t\t6 - Caixa de Correio\n\n\n\t\t>>");
            userPick = sc.nextInt();

        }
        if (userPick == 1) {
            consultaSaldo();
        } else if (userPick == 2) {
            criaProjecto(); //Caso de sucesso/falha?
        } else if (userPick == 3) {
            listarProjectosActuais(0, 1);
        } else if (userPick == 4) {
            listarProjectosActuais(1,1);
        } else if (userPick == 5){
            consultarProjectosUser();
            listarProjectosActuais(1, 1);
        }else if (userPick == 6){
            caixaCorreio();
        }

        menuConta();

    }
}
