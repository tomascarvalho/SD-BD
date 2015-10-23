
import java.lang.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    public ClientRequest newRequest;
    public ClientRequest newResponse;

    public Client() throws ClassNotFoundException {
        /**
         * Passa o argumentos para as variáveis para podermos fazer as ligações mais à frente
         */

        properties = new PropertiesReaderClient();
        ipServer1 = properties.getPrimaryIP();
        ipServer2 = properties.getSecundaryIP();
        port1 = properties.getPrimaryPort();
        port2 = properties.getSecundaryPort();

        connectionFunction();
        try {

            mainMenu();
        } catch (IOException e) {
            System.out.println("Error!");
        }

    }

    /**
     * Função responsável por fazer a conecção do servidor
     */
    public void connectionFunction() {

        int portTemp;
        String ipTemp;

        while (true) {
            try {

                /**
                 * cria ligação com o servidor que está com o IP->ipServer1 e Porto->port1
                 */
                System.out.println("Fase 1->Conecção com servidor\n");
                conectionToServer = new Socket(ipServer1, port1);

                /**
                 * vai iniciar os streams de input e output para partilhar mensagens com o servidor
                 */
                sender = new ObjectOutputStream(conectionToServer.getOutputStream());
                sender.flush();/*perceber porque é que esta linha tem de estar aqui*/

                reciver = new ObjectInputStream(conectionToServer.getInputStream());
                return;

            } catch (IOException e) {

                if (conectionError == 3) {
                    System.out.println("[Cliente] Não me consigo ligar ao servidor, vou-me ligar ao secundário");
                    try {
                        conectionToServer = new Socket(ipServer2, port2);
                        ipTemp = ipServer2;
                        portTemp = port2;
                        ipServer2 = ipServer1;
                        port1 = port2;
                        ipServer1 = ipTemp;
                        port1 = portTemp;
                        conectionError = 0;
                    } catch (IOException exp) {
                        System.out.println("[Cliente] Não me consigo ligar ao Servidor de Backup.");
                    }
                } else {
                    conectionError++;
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }

    public static void main(String[] args) throws ClassNotFoundException {

        new Client();

    }

    private void updateRequest(ClientRequest oldrqst, ClientRequest newrqst) {

        int requestIndex = myRequest.indexOf(oldrqst);

        myRequest.set(requestIndex, newrqst);

    }

    public boolean LogIn(boolean flag) throws ClassNotFoundException {

        Scanner sc = new Scanner(System.in);
        String[] person = new String[2];
        String requestId;

        try {

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

            if (myRequest.size() == 0) {
                requestId = "1";
            } else {
                requestId = "" + myRequest.size();
            }

            newRequest = new ClientRequest(requestId, postCard);
            newRequest.setStage(0);
            myRequest.add(newRequest);

            sender.writeUnshared(newRequest);

            newResponse = (ClientRequest) reciver.readObject();
            updateRequest(newRequest, newResponse);

            return !newResponse.getResponse()[0].equals("usernotrec");
        } catch (IOException e) {
            connectionFunction();
        }

        return flag;
    }

    public boolean criaConta() throws ClassNotFoundException {

        Scanner sc = new Scanner(System.in);
        String requestId;
        String[] newUserData = new String[4];

        try {

            System.out.println("\t\t\tNovo Utilizador\n\n");
            System.out.print("\t\tPrimeiro Nome:");
            newUserData[0] = sc.nextLine();
            System.out.print("\t\tApelido:");
            newUserData[1] = sc.nextLine();
            System.out.print("\t\tUsername:");
            newUserData[2] = sc.nextLine();
            System.out.print("\t\tPassword:");
            newUserData[3] = sc.nextLine();

            postCard[0] = "new";
            postCard[1] = newUserData;

            if (myRequest.size() == 0) {
                requestId = "" + 1;
            } else {
                requestId = "" + myRequest.size();
            }
            newRequest = new ClientRequest(requestId, postCard);
            newRequest.setStage(0);
            myRequest.add(newRequest);

            sender.writeUnshared(newRequest);

            newResponse = (ClientRequest) reciver.readObject();

            updateRequest(newRequest, newResponse);

            if (newResponse.getResponse()[0].equals("infosave")) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            connectionFunction();
        }

        return true;
    }

    public boolean consultaSaldo() throws IOException, ClassNotFoundException {

        String requestId;

        try {
            postCard[0] = "seesal";
            postCard[1] = null;

            if (myRequest.size() == 0) {
                requestId = "1";
            } else {
                requestId = "" + myRequest.size();
            }

            newRequest = new ClientRequest(requestId, postCard);
            newRequest.setStage(0);
            myRequest.add(newRequest);

            sender.writeUnshared(newRequest);

            newResponse = (ClientRequest) reciver.readObject();
            updateRequest(newRequest, newResponse);

            System.out.println("\t\tO seu saldo é de " + newResponse.getResponse()[1] + " euros.");

        } catch (IOException e) {
            connectionFunction();
        }

        return true;
    }

    public boolean criaProjecto() throws IOException, ClassNotFoundException {

        Scanner sc = new Scanner(System.in);
        String[] newProjectData = new String[3];
        String requestId;

        try {

            System.out.println("\n\t\tNovo Projecto");
            System.out.println("\n\tNome do Projecto: ");
            newProjectData[0] = sc.nextLine(); //Titulo do Projecto
            System.out.println("\n\tDescrição do Projecto: ");
            newProjectData[1] = sc.nextLine(); //Descrição do Projecto
            System.out.println("\n\tValor Pretendido: ");
            newProjectData[2] = sc.nextLine(); //Valor Pretendido para o Projecto

            postCard[0] = "new_project";
            postCard[1] = newProjectData;

            if (myRequest.size() == 0) {
                requestId = "0";
            } else {
                requestId = "" + myRequest.size();
            }

            newRequest = new ClientRequest(requestId, postCard);
            newRequest.setStage(0);
            myRequest.add(newRequest);

            sender.writeUnshared(newRequest);

            newResponse = (ClientRequest) reciver.readObject();
            updateRequest(newRequest, newResponse);

            if (newResponse.getResponse()[0].equals("infosave")) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            connectionFunction();
        }

        return true;
    }

    public void mainMenu() throws IOException, ClassNotFoundException {

        Scanner sc = new Scanner(System.in);
        boolean logResult = true;
        String userPick;

        conectionError = 0;

        System.out.println("\t\t\tMenu Inicial\n\n");
        System.out.print("\t\t1 - Criar Conta\n\t\t2 - LogIn\n\n\n\t\t>>");
        userPick = sc.nextLine();
        //Verificar Escolhas
        while ((userPick.equals("1") == false) && (userPick.equals("2") == false)) {
            System.out.println("\nERRO - Escolher uma das opções dadas!!\n");
            System.out.print("\t\t1 - Criar Conta\n\t\t2 - LogIn\n\n\n\t\t>>");
            userPick = sc.nextLine();
        }
        if (userPick.equals("1")) {
            criaConta();//falta apanhar o que ela retorna

        } else if (userPick.equals("2")) {
            /**
             * vai chamar a função para fazer o login se ela returnar null muda o argumento e volta a chamar a função
             */
            while (!LogIn(logResult)) {
                logResult = false;
            }

        }

        menuConta();

    }

    public void menuConta() throws IOException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        String userPick;
        System.out.println("\t\t\tMenu Inicial\n\n");
        System.out.print("\t\t1 - Consultar Saldo\n\n\n\t\t2 - Criar Projecto\n\n\n\t\t");
        userPick = sc.nextLine();

        //Verificar Escolhas. Inserir novos casos quando forem inseridas novas funções
        while ((userPick.equals("1") == false) && (userPick.equals("2") == false)) {
            System.out.println("\nERRO - Escolher uma das opções dadas!!\n");
            System.out.print("\t\t1 - Criar Conta\n\t\t2 - LogIn\n\n\n\t\t>>");
            userPick = sc.nextLine();

        }
        if (userPick.equals("1")) {
            consultaSaldo();
        } else if (userPick.equals("2")) {
            criaProjecto(); //Caso de sucesso/falha?
        }

    }
}
