
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

    public Client(String[] args) throws ClassNotFoundException {
        /**
         * Passa o argumentos para as variáveis para podermos fazer as ligações
         * mais à frente
         */

        ipServer1 = args[0];
        port1 = Integer.parseInt(args[1]);
        ipServer2 = args[2];
        port2 = Integer.parseInt(args[3]);

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

        while (true) {
            try {

                /**
                 * cria ligação com o servidor que está com o IP->ipServer1 e
                 * Porto->port1
                 */
                System.out.println("Fase 1->Conecção com servidor\n");
                conectionToServer = new Socket(ipServer1, port1);

                /**
                 * vai iniciar os streams de input e output para partilhar
                 * mensagens com o servidor
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

        /**
         * se o utilizador iniciar o programa e não meter ip's e portas dos
         * servidores o programa entra neste if, imprime a mensagem de erro e
         * termina
         */
        if (args.length != 4) {
            System.out.println("java Client <IP1> <P1> <IP2> <P2>");
            System.exit(0);
        } else {
            new Client(args);
        }
    }

    public boolean LogIn(boolean flag) throws ClassNotFoundException {/* Ainda não tem o failover a funcionar aqui*/

        Scanner sc = new Scanner(System.in);
        Object[] serverMessage = null;
        String[] person = new String[2];

        try {

            /**
             * Vai pedir credenciais ao cliente para fazer o login. Se os dados
             * não estiverem na base de dados vai voltar a chamar esta função.
             * Caso contrário vai avançar para o menu inicial
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

            /*vai mandar o user ao server para ver se ele está na base de dados*/
            sender.writeUnshared(postCard);

            serverMessage = (Object[]) reciver.readObject();

            return !serverMessage[0].equals("usernotrec");
        } catch (IOException e) {
            connectionFunction();
        }

        return flag;
    }

    public boolean criaConta() throws ClassNotFoundException {

        Scanner sc = new Scanner(System.in);
        String[] newUserData = new String[4];
        Object[] resposta;

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

            postCard[0] = "new_project";
            postCard[1] = newUserData;

            sender.writeUnshared(postCard);

            resposta = (Object[]) reciver.readObject();

            if (resposta[0].equals("infosave")) {
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

        Object[] resposta;

        try {
            postCard[0] = "seesal";
            postCard[1] = null;

            sender.writeUnshared(postCard);

            resposta = (Object[]) reciver.readObject();

            System.out.println("\t\tO seu saldo é de " + resposta[0] + " euros.");
        } catch (IOException e) {
            connectionFunction();
        }

        return true;
    }

    public boolean criaProjecto() throws IOException, ClassNotFoundException {
        
        Object[] resposta;
        Scanner sc = new Scanner(System.in);
        String[] newProjectData = new String[3];
        
        try{
            
            
            System.out.println("\n\t\tNovo Projecto");
            System.out.println("\n\tNome do Projecto: ");
            newProjectData[0] = sc.nextLine(); //Titulo do Projecto
            System.out.println("\n\tDescrição do Projecto: ");
            newProjectData[1] = sc.nextLine(); //Descrição do Projecto
            System.out.println("\n\tValor Pretendido: ");
            newProjectData[2] = sc.nextLine(); //Valor Pretendido para o Projecto

            postCard[0] = "new_project";
            postCard[1] = newProjectData;

            sender.writeUnshared(postCard);

            resposta = (Object[]) reciver.readObject();

            if (resposta[0].equals("infosave")) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            connectionFunction();
        }

        
        return true;
    }

    public void mainMenu() throws IOException, ClassNotFoundException {/*se isto for int posso mandar 0 ou 1 para tratar das falhas????*/


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
             * vai chamar a função para fazer o login se ela returnar null muda
             * o argumento e volta a chamar a função
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
