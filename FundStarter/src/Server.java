
import java.net.*;
import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;
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

            /**
             * Cria um socket para ligação com clientes no porto indicado no
             * serverPort
             */
            int serverPort = 6000;//Integer.parseInt(args[1]);
            ServerSocket conectionToClient = new ServerSocket(serverPort);
            Socket cliente;
            String rmiLocation = "//localhost:7777/fundStarter";//o localhost tem de mudar para ser o ip da máquina onde está o RMI

            /**
             * COMETÁRIO EM FALTA
             */
            UDPServer conectServer = new UDPServer();
            Thread t = new Thread(conectServer);
            t.start();
            /**
             * COMETÁRIO EM FALTA
             */
            RMIServerInterface remoteConection = (RMIServerInterface) Naming.lookup(rmiLocation);

            System.out.println("[Server] Servidor à escuta no porto " + serverPort);

            while (true) {

                /**
                 * Espera que um cliente se ligue
                 */
                cliente = conectionToClient.accept();

                /**
                 * Por cada cliente que se liga, vai criar uma thread que fica
                 * encarregue de lidar com ele
                 */
                new NewClient(cliente, remoteConection);

            }

        } catch (IOException e) {
            /**
             * Quando apanhar um IOException quer dizer que já há um servidor
             * activo e que vai ter de ficar como servidor de backup
             */
            if (e.getMessage().equals("Address already in use")) {
                new BackupServer("localhost"); /*isto só está aqui como teste*/

            } else {
                System.out.println("[Server] Encotrei esta excepção: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("[Server] Erro no Servidor Principal: " + e.getMessage());
        }
    }

    public void run() {

    }

}

class NewClient extends Thread {

    Socket myClient;
    ObjectInputStream reciver;
    ObjectOutputStream sender;
    ClientRequest postCard;
    ClientRequest myMail;//podera ter de ser Object[] dependendo do que for preciso enviar ao cliente
    RMIServerInterface remoteConection;
    int myUserID;
    int myProjectID;
    String alterRequest;

    NewClient(Socket cliente, RMIServerInterface rmiConection) {

        try {
            /**
             * Guarda o cliente que se ligou anteriormente ao servidor e também
             * a ligação com o servidor RMI
             */
            myClient = cliente;
            remoteConection = rmiConection;

            /**
             * cria canais de comunicação com os clientes
             */
            reciver = new ObjectInputStream(myClient.getInputStream());
            sender = new ObjectOutputStream(myClient.getOutputStream());
            sender.flush();

            /**
             * vai iniciar a thread
             */
            this.start();

        } catch (IOException e) {
            System.out.println("[Server] Erro no Client Conection: " + e.getMessage());
        }
    }

    public void run() {

        try {
            while (true) {
                postCard = null;
                postCard = (ClientRequest) reciver.readObject();/*está a dar erro nesta merda por causa de não ser serializable*/

                alterRequest = postCard.getRequestID() + ("_" + myUserID);
                postCard.setRequestID(alterRequest);

                System.out.println("[Server] Li a mensagem do cliente na boa.");
                //mudar depois para um switch
                if (postCard.getRequest()[0].equals("log")) {

                    postCard.setStage(1);
                    myMail = remoteConection.verificaLogIn(postCard);

                    if (myMail.getResponse()[0].equals("userrec")) {
                        myUserID = (int) myMail.getResponse()[1];
                    }
                    
                    myMail.setStage(4);
                /*
                } else if (((String) postCard[0]).equals("new")) {

                    String[] newPerson = (String[]) postCard[1];
                    myMail = remoteConection.novoUtilizador(newPerson);

                    if (myMail[0].equals("infosave")) {
                        System.out.println("myUserID:" + (int) myMail[1]);
                        myUserID = (int) myMail[1];
                    }

                } else if (((String) postCard[0]).equals("new_project")) {

                    String[] newProject = (String[]) postCard[1];
                    myMail = remoteConection.novoProjecto(newProject);

                    if (myMail[0].equals("infosave")) {
                        System.out.println("myProjectID:" + (int) myMail[1]);
                        myProjectID = (int) myMail[1];
                    }

                } else if (postCard[0].equals("seesal")) {

                    myMail = remoteConection.getUserSaldo(myUserID);
                */
                }
                
                sender.writeUnshared(myMail);

            }
        } catch (Exception e) {
            System.out.println("[Server] Erro no Client Conection: " + e.getMessage());
        }
        }
}

class BackupServer extends Thread {

    String primaryServer;
    DatagramSocket udpConection;
    byte[] pingMessage;
    int conectionFail = 0;
    InetAddress hostConection;
    int serverPort = 6060;
    DatagramPacket sender, reciver;
    int failOverCounter;
    String[] args = null;

    BackupServer(String hostIp) { /*vai precisar de argumentos diferentes para iniciar o novo servidor quando o pricipal for abaixo*/

        /**
         * Vai guradar o ip do Servidor principal para se poder ligar como
         * backup e inicializa o contador para o caso de ocorrer um FailOver
         */

        primaryServer = hostIp;
        failOverCounter = 0;
        this.start();

    }

    public void run() {

        try {

            /*+
             Vai criar uma socket para as ligações UDP
             */
            udpConection = new DatagramSocket();

            while (true) {

                try {

                    /**
                     * vai fazer a ligação com o Servidor principal e mandar a
                     * mensagem 'ping'. Caso o servidor principal vai abaixo e
                     * espera até à terceira mensagem de erro para se tornar
                     * servdor principal
                     */
                    pingMessage = "ping".getBytes();
                    hostConection = InetAddress.getByName(primaryServer);
                    sender = new DatagramPacket(pingMessage, pingMessage.length, hostConection, serverPort);
                    udpConection.send(sender);
                    udpConection.setSoTimeout(2000);
                    pingMessage = new byte[1000];
                    reciver = new DatagramPacket(pingMessage, pingMessage.length);
                    udpConection.receive(reciver);
                    System.out.println("[Backup Server] Recebi esta mensagem do Serviodr Principal: " + new String(reciver.getData(), 0, reciver.getLength()));

                    Thread.sleep(2000);

                } catch (SocketTimeoutException e) {

                    if (failOverCounter < 3) {

                        System.out.println("[Backup Server] Não recebi ping do Servidor Principal.");
                        failOverCounter++;

                    } else {

                        System.out.println("[Backup Server] O Servidor principal está em baixo, vouassumir o controlo.");
                        udpConection.close();
                        Server.main(args);

                    }
                }

            }

        } catch (Exception e) {

        }

    }
}

class UDPServer implements Runnable {

    DatagramSocket conection;
    byte[] buffer = new byte[1000];
    DatagramPacket sender, reciver;
    int serverPort = 6060;
    String pingMessage;

    /*UDPServer() {

     this.start();

     }*/
    public void run() {
        try {

            /**
             * cria ligação UDP no porto indicado na variável serverPort
             */
            conection = new DatagramSocket(serverPort);

            while (true) {

                /**
                 * recebe mensagem do Servidor Backup e volta a mandar a mesma
                 * mensagem
                 */
                reciver = new DatagramPacket(buffer, buffer.length);
                conection.receive(reciver);
                pingMessage = new String(reciver.getData(), 0, reciver.getLength());
                System.out.println("[UDPServer]Message from Backup Server: " + pingMessage);
                sender = new DatagramPacket(reciver.getData(), reciver.getLength(), reciver.getAddress(), reciver.getPort());
                conection.send(sender);

            }

        } catch (Exception e) {
            System.out.println("[Server] Recebi esta mensagem de erro: " + e.getMessage());
        }
    }

}
