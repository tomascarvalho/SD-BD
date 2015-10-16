
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
public class Client extends Thread {

    static Socket conectionToServer;
    static ObjectInputStream reciver;
    static ObjectOutputStream sender;
    static int conectionError = 0;
    static String ipServer1, ipServer2;
    static int port1, port2;
    static Object[] postCard = new Object[2];

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
            /**
             * Passa o argumentos para as variáveis para podermos fazer as
             * ligações mais à frente
             */

            ipServer1 = args[0];
            port1 = Integer.parseInt(args[1]);
            ipServer2 = args[2];
            port2 = Integer.parseInt(args[3]);

            while (true) {
                try {

                    /**
                     * cria ligação com o servidor que está com o IP->ipServer1
                     * e Porto->port1
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

                    MainMenu();

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

    }

    public static boolean LogIn(boolean flag) throws IOException, ClassNotFoundException {/* Ainda não tem o failover a funcionar aqui*/

        Scanner sc = new Scanner(System.in);
        String serverMessage;
        String[] person=new String[2];

        /**
         * Vai pedir credências ao cliente para fazer o login se os dados não
         * estiverem a base de dados, vai voltar a chamar esta função caso
         * contrário vai avançar para o menu inicial
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
        sender.writeObject(postCard);

        serverMessage = (String) reciver.readObject();

        return !serverMessage.equals("Utilizador não reconhecido");
    }

    public static boolean criaConta() throws IOException, ClassNotFoundException {

        Scanner sc = new Scanner(System.in);
        String[] newUserData = new String[2];
        String resposta;

        System.out.println("\t\t\tNovo Utilizador\n\n");
        System.out.print("\t\tUsername:");
        newUserData[0] = sc.nextLine();
        System.out.print("Password:");
        newUserData[1] = sc.nextLine();

        postCard[0] = "new";
        postCard[1] = newUserData;

        sender.writeObject(postCard);

        resposta = (String) reciver.readObject();

        System.out.println(resposta);
        return true;

    }

    public static void MainMenu() throws IOException, ClassNotFoundException {/*se isto for int posso mandar 0 ou 1 para tratar das falhas????*/


        Scanner sc = new Scanner(System.in);
        boolean logResult = true;
        String userPick;

        conectionError = 0;

        System.out.println("\t\t\tMenu Inicial\n\n");
        System.out.print("\t\t1 - Criar Conta\n\t\t2 - LogIn\n\n\n\t\t>>");
        userPick = sc.nextLine();

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
    }
}
