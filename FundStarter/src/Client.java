
import java.net.*;
import java.util.*;
import java.io.*;

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
public class Client {

    private Socket conectionToServer;
    private ObjectInputStream reciver;
    private ObjectOutputStream sender;
    private int conectionError = 0;
    private String ipServer1, ipServer2;
    private int port1, port2;
    private Object[] postCard = new Object[2];
    private ArrayList<ClientRequest> myRequest = new ArrayList<ClientRequest>();
    private PropertiesReaderClient properties;
    private Scanner sc = new Scanner(System.in);

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
            
            if(reconnect){
                System.out.println("[Cliente]Vou pedir ao rmi para ver os meus pedidos");
            }

            
    }

    /**
     * Funçao responsável para eviar os request para o servidor
     */
    public Object[] postOffice(Object[] mail) {

        ClientRequest newRequest;
        ClientRequest newResponse;
        String requestID;

        try {
            
            if (myRequest.size() == 0) {
                requestID = "1";
            } else {
                requestID = "" + (myRequest.size()+1);
            }

            newRequest = new ClientRequest(requestID, postCard);
            myRequest.add(newRequest);
            newRequest.setStage(0);

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
            return true;
        } else {
            return false;
        }

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
        num_recompensas= sc.nextInt();
        sc.nextLine();
        newProjectData[4] = ""+num_recompensas;
        while (i<num_recompensas){
            i++;
            j++;
            System.out.println("Recompensa nº"+i);
            System.out.println("Montante a doar: ");
            montante = sc.nextLine();
            newProjectData[4+j] = montante;
            j++;
            System.out.println("Recompensa associada: ");
            newProjectData[4+j] = sc.nextLine();   
        }
        j++;
        System.out.println("\n\t Quantos níveis extra existem: ");
        //while(!sc.hasNextInt()) sc.next();
        num_niveis_extra = sc.nextInt();
        sc.nextLine();
        newProjectData[4+j] = ""+num_niveis_extra;
        i = 0;
        while (i<num_niveis_extra){
            i++;
            j++;
            System.out.println("Nivel Extra nº"+i+"\nValor: ");
            //while(!sc.hasNextInt()) sc.next();
            montante = sc.nextLine();
            newProjectData[4+j] = montante;
            j++;
            System.out.println("Nivel Extra: ");
            newProjectData[4+j] = sc.nextLine();   
        }
        j++;
        System.out.println("\n\t Quantos tipos diferentes de produto existem: ");
        //while(!sc.hasNextInt()) sc.next();
        num_produto = sc.nextInt();
        sc.nextLine();
        newProjectData[4+j] = ""+num_produto;
        i = 0;
        while(i< num_produto){
            i++;
            j++;
            System.out.println("Tipo de Produto: ");
            newProjectData[4+j] = sc.nextLine();
            
            
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
    
    public void listarProjectosActuais(int choice) throws IOException, ClassNotFoundException { // if choice 0 -> active projects
                                                     // if choice 1 -> old projects
        
        String array_projectos[];
        String titulo, id, valoractual, valorpretendido;
        if (choice == 0){
            postCard[0] = "list_actual_projects";  
        }
        else{
            postCard[0] = "list_old_projects";
        }
        postCard[1] = choice;
        
        postCard = postOffice(postCard);
        
        String teste;
        teste = ""+postCard[1];
        int i = Integer.parseInt(teste); 
        int j = 0;
       
        array_projectos = (String[])postCard[0]; //ISTO ESTA A NULL PORQUE?
        
        if ((!array_projectos[0].equals("error_no_active_projects")) || (!array_projectos[0].equals("error_no_old_projects")))
        {
            if(choice == 0) {
            System.out.println("Projectos Actuais: ");
            }
            else{
                System.out.println("Projectos Antigos: ");
            }
        
            while (j<i){
                id = array_projectos[j];
                j++;
                titulo = array_projectos[j];
                j++;
                valoractual = array_projectos[j];
                j++;
                valorpretendido = array_projectos[j];
                j++;
                if (choice == 0)
                    System.out.println("ID: "+id+" Titulo: "+titulo+" Progresso: "+valoractual+" euros angariados / "+valorpretendido+" euros pretendidos");
                else
                    System.out.println("ID: "+id+" Titulo: "+titulo);
            }
            if (choice == 0){
                System.out.println("1 - Consultar detalhes de um projcto");
                System.out.println("2 - Voltar ao Menu de Conta");
                choice = sc.nextInt();
                while (choice!=0 && choice != 1){
                    System.out.println("1 - Consultar detalhes de um projcto");
                    System.out.println("2 - Voltar ao Menu de Conta");
                    
                }
                if (choice == 0){
                    System.out.println("ID do projecto a consultar: ");
                    choice = sc.nextInt();
                    consultarDetalhesProjecto(choice);
                }
                menuConta();
              
            }
        }
        else{
            if (choice == 1)
                System.out.println("Não há projectos activos!");
            else
                System.out.println("Não há projectos antigos!");
        }
        
        
    }
    
    public void consultarDetalhesProjecto(int id){
        
        postCard[0] = "list_project_details";
        postCard[1] = id;
        postCard = postOffice(postCard);
        
        
    }
    public void mainMenu() throws IOException, ClassNotFoundException {

        boolean logResult = true;
        String userPick;

        conectionError = 0;

        System.out.println("\t\t\tMenu Inicial\n\n");
        System.out.print("\t\t1 - Criar Conta\n\t\t2 - LogIn\n\t\t3 - Consultar Projectos Actuais\n\t\t4 - Consultar Projectos Antigos\n\n\n\t\t>>");
        userPick = sc.nextLine();
        //Verificar Escolhas
        while ((userPick.equals("1") == false) && (userPick.equals("2") == false) && (userPick.equals("3") == false) && (userPick.equals("4")== false)) {
            System.out.println("\nERRO - Escolher uma das opções dadas!!\n");
            System.out.print("\t\t1 - Criar Conta\n\t\t2 - LogIn\n\t\t3 - Consultar Projectos Actuais\n\t\t4 - Consultar Projectos Antigos\n\n\n\t\t>>");
            userPick = sc.nextLine();
        }
        if (userPick.equals("1")) {
            while (criaConta() != true) {
                System.out.println("Erro ao criar novo user!");
            }
        } else if (userPick.equals("2")) {
            /**
             * vai chamar a função para fazer o login se ela returnar null muda o argumento e volta a chamar a função
             */
            while (!LogIn(logResult)) {
                logResult = false;
            }

        } else if (userPick.equals("3")) {
            listarProjectosActuais(0);
        } else if(userPick.equals("4")){
            listarProjectosActuais(1);
        }
            

        menuConta();

    }

    public void menuConta() throws IOException, ClassNotFoundException {

        String userPick;
        System.out.println("\t\t\tMenu Inicial\n\n");
        System.out.print("\t\t1 - Consultar Saldo\n\t\t2 - Criar Projecto\n\t\t3 - Listar Projectos Actuais\n\t\t4 - Listar Projectos Antigos\n\n\t>>");
        userPick = sc.nextLine();

        //Verificar Escolhas. Inserir novos casos quando forem inseridas novas funções
        while ((userPick.equals("1") == false) && (userPick.equals("2") == false) && (userPick.equals("3") == false) && (userPick.equals("4") == false)) {
            System.out.println("\nERRO - Escolher uma das opções dadas!!\n");
            System.out.print("\t\t1 - Consultar Saldo\n\t\t2 - Criar Projecto\n\t\t3 - Listar Projectos Actuais\n\t\t4 - Listar Projectos Antigos\n\n\t\t>>");
            userPick = sc.nextLine();

        }
        if (userPick.equals("1")) {
            consultaSaldo();
        } else if (userPick.equals("2")) {
            criaProjecto(); //Caso de sucesso/falha?
        } else if (userPick.equals("3")) {
            listarProjectosActuais(0);
        } else if(userPick.equals("4")){
            listarProjectosActuais(1);
        }
        
        menuConta();

    }
}
