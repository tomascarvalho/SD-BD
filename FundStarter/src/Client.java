
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

    public static void main(String[] args) throws IOException {

        String ipServer1, ipServer2;
        int port1, port2;
        Socket conectionToServer;
        User message;
        ObjectInputStream reciver;
        ObjectOutputStream sender;
        Scanner sc = new Scanner(System.in);
        int conectionError = 0;
        String teste1, teste2;

        /*
         se o utilizador iniciar o programa e não meter ip's e portas dos
         servidores o programa entra neste if, imprime a mensagem de erro
         e termina
           
         PS: SE QUISEREM CORRER SÓ UM SERVIDOR PARA TERSTE METAM OS ULTIMOS
         CAMPOS COM localhost e 0, ASSIM NÃO DÁ ERRO. :D
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
                    
                    System.out.println("Liguei ao Servidor");

                    /*
                     vai iniciar os streams de input e output para partilhar
                     mensagens com o servidor
                     */
                    sender = new ObjectOutputStream(conectionToServer.getOutputStream());
                    sender.flush();/*perceber porque é que esta linha tem de estar aqui*/
                    reciver = new ObjectInputStream(conectionToServer.getInputStream());
                    
                    
                    System.out.println("Passou desta linha");

                    /*
                     vai mandar uma mensagem de teste ao servidor
                     */
                    System.out.print("Insira o seu username:");
                    teste1 = sc.nextLine();
                    System.out.print("Insira a sua password:");
                    teste2 = sc.nextLine();

                    message = new User(teste1, teste2);

                    System.out.println("Fase 2->Envio de mensagem ao servidro\n");
                    sender.writeObject(message);
                    try {
                        System.out.println("Fase 3->Receber mensagem do servidor\n");
                        System.out.println((String) reciver.readObject());
                    } catch (Exception eae) {

                    }

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
}
