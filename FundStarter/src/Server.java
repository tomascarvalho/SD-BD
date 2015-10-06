
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

    public static void main(String args[]) {

        try {

            /*
             Cria um socket para ligação com clientes no porto indicado no
             serverPort
             */
            int serverPort = 6000;
            ServerSocket conectionToClient = new ServerSocket(serverPort);
            Socket cliente;

            System.out.println("Servidor à eescuta no porto 6000");

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

        } catch (Exception e) {
            System.out.println("Recebi esta mensagem de erro: " + e.getMessage());
        }
    }

}

class NewClient implements Runnable {

    Socket myClient;
    DataInputStream reciver;
    DataOutputStream sender;
    Thread me;
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
            me.start();

        } catch (Exception e) {
            System.out.println("Recebi esta mensagem de erro: " + e.getMessage());
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
            System.out.println("Recebi esta mensagem de erro: " + e.getMessage());
        }
    }
}
