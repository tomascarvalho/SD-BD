
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


    public static void main(String[] args) throws IOException, InterruptedException {

        
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

    public static void LogIn() {

        Scanner sc = new Scanner(System.in);
        String name;
        String pass;
        User person;
        
        conectionError=0;

        System.out.println("\t\t\tFundStater\n\t\tLog In\n");
        System.out.print("\t\t\tUsername:");
        name = sc.nextLine();
        System.out.print("Password:");
        pass = sc.nextLine();

        person = new User(name, pass);

        try {
            sender.writeObject(person);

            System.out.println("[Cliente] Mensagem do Server: " + (String) reciver.readObject());
        } catch (IOException ex) {
            
            if(ex.getMessage().equals("Broken pipe")){
                System.out.println("[Cliente]O servidor foi abaixo");
                if (conectionError == 3) {
                        System.out.println("[Cliente] Não me consigo ligar ao servidor, vou-me ligar ao secundário");
                        try {
                            conectionToServer = new Socket(ipServer2, port2);
                            conectionError = 0;
                        } catch (IOException e) {
                            System.out.println("[Cliente] Não me consigo ligar ao Servidor de Backup.");
                        }
                    } else {
                        conectionError++;
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void MainMenu() {/*se isto for int posso mandar 0 ou 1 para tratar das falhas????*/


        Scanner sc = new Scanner(System.in);

        LogIn();
        
        System.out.println("\t\t\tFundStarter\n\t\tMenu Inicial");
        
        

    }
}
