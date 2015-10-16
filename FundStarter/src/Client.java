
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

    public static void main(String[] args) throws ClassNotFoundException {

        /*
         se o utilizador iniciar o programa e não meter ip's e portas dos
         servidores o programa entra neste if, imprime a mensagem de erro
         e termina
         */
        if (args.length != 4) {
            System.out.println("java Client <IP1> <P1> <IP2> <P2>");
            System.exit(0);
        } else {
            /*
             Passa o argumentos para as variáveis para podermos fazer as
             ligações mais à frente
             */

            ipServer1 = args[0];
            port1 = Integer.parseInt(args[1]);
            ipServer2 = args[2];
            port2 = Integer.parseInt(args[3]);

            while (true) {
                try {

                    /*
                     cria ligação com o servidor que está com o IP->ipServer1 e
                     Porto->port1
                     */
                    System.out.println("Fase 1->Conecção com servidor\n");
                    conectionToServer = new Socket(ipServer1, port1);


                    /*
                     vai iniciar os streams de input e output para partilhar
                     mensagens com o servidor
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
        String name;
        String pass;
        String serverMessage;
        User person;

        /*
         Vai pedir credências ao cliente para fazer o login
         se os dados não estiverem a base de dados, vai voltar a chamar esta função
         caso contrário vai avançar para o menu inicial
         */
        if (!flag) {
            System.out.println("Utilizador não reconhecido!!!");
            /*meter uma opção para voltar ao menu inicial e/ou fazer inscrição*/
        }

        System.out.println("\n\t\tLogIn");
        System.out.print("\tUsername:");
        name = sc.nextLine();
        System.out.print("\tPassword:");
        pass = sc.nextLine();

        person = new User(name, pass);

        /*vai mandar o user ao server para ver se ele está na base de dados*/
        sender.writeObject(person);

        serverMessage = (String) reciver.readObject();

        return !serverMessage.equals("Utilizador não reconhecido");
    }

    public static void MainMenu() throws IOException, ClassNotFoundException {/*se isto for int posso mandar 0 ou 1 para tratar das falhas????*/


        Scanner sc = new Scanner(System.in);
        boolean logResult = true;

        conectionError = 0;

        /*
         vai chamar a função para fazer o login se ela returnar null
         muda o argumento e volta a chamar a função
         */
        if (!LogIn(logResult)) {
            logResult = false;
        }

        System.out.println("Menu Inicial");

    }
}
