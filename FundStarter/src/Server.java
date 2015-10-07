
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
public class Server {

    public static void main(String[] args) {

        try {

            /*
             Cria um socket para ligação com clientes no porto indicado no
             serverPort
             */
            
            int serverPort = 6000;//Integer.parseInt(args[1]);
            ServerSocket conectionToClient = new ServerSocket(serverPort);
            Socket cliente;

            System.out.println("[Server] Servidor à escuta no porto " + serverPort);

            while (true) {
                
                /*
                 Espera que um cliente se ligue
                 */
                cliente = conectionToClient.accept();
                /*
                 Por cada cliente que se liga, vai criar uma thread que fica
                 encarregue de lidar com ele
                 */
                new NewClient(cliente);
                
            }

        } catch (IOException e) {
            /*
             Quando apanhar um IOException quer dizer que já há um servidor
             activo e que vai ter de ficar como servidor de backup
             */

            new BackupServer("localhost"); /*isto só está aqui como teste*/

        } catch (Exception e) {
            System.out.println("[Server] Recebi esta mensagem de erro: " + e.getMessage());
        }
    }

}

class NewClient extends Thread {

    Socket myClient;
    DataInputStream reciver;
    DataOutputStream sender;
    String message;

    NewClient(Socket cliente) {

        try {
            /*
             Guarda o cliente que se ligou anteriormente ao servidor
             */
            myClient = cliente;

            /*
             cria canais de comunicação com os clientes
             */
            reciver = new DataInputStream(myClient.getInputStream());
            sender = new DataOutputStream(myClient.getOutputStream());

            /*
             vai iniciar a thread
             */
            this.start();

        } catch (IOException e) {
            System.out.println("[ClientConection] Recebi esta mensagem de erro: " + e.getMessage());
        }
    }

    public void run() {

        try {
            message = reciver.readUTF();

            if (message.equals("Teste")) {
                message = "Mesagem de teste recebida";
            } else {
                message = "Error!!";
            }

            sender.writeUTF(message);
        } catch (Exception e) {
            System.out.println("[ClientConection] Recebi esta mensagem de erro: " + e.getMessage());
        }
    }
}

class BackupServer extends Thread {

    String primaryServer;
    DatagramSocket udpConection;
    byte[] pingMessage;
    int conectionFail = 0;
    InetAddress hostConection;
    int serverPort=6060;
    DatagramPacket sender, reciver;

    BackupServer(String hostIp) { /*vai precisar de argumentos diferentes para iniciar o novo servidor quando o pricipal for abaixo*/

        primaryServer = hostIp;
        this.start();

    }

    public void run() {

        try {

            udpConection = new DatagramSocket();

            while (true) {

                try {
                    
                    pingMessage="ping".getBytes();
                    hostConection=InetAddress.getByName(primaryServer);
                    sender=new DatagramPacket(pingMessage,pingMessage.length,hostConection,serverPort);
                    udpConection.send(sender);
                    udpConection.setSoTimeout(2000);
                    pingMessage=new byte[1000];
                    reciver=new DatagramPacket(pingMessage,pingMessage.length);
                    udpConection.receive(reciver);
                    Thread.sleep(5000);
                    
                } catch (Exception e) {
                    System.out.println("[BackupServer] Recebi esta mensagem de erro: " + e.getMessage());
                }

            }

        } catch (Exception e) {

        }

    }
}
