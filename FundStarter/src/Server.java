
import java.net.*;
import java.io.*;
import java.rmi.Naming;

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
            String rmiLocation = "//localhost:7777/fundStarter";//o localhost tem de mudar para ser o ip da máquina onde está o RMI

            /*
             COMETÁRIO EM FALTA
             */
            new UDPServer();
            /*
             COMETÁRIO EM FALTA
             */
            RMIServerInterface remoteConection = (RMIServerInterface) Naming.lookup(rmiLocation);

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
                new NewClient(cliente, remoteConection);

            }

        } catch (IOException e) {
            /*
             Quando apanhar um IOException quer dizer que já há um servidor
             activo e que vai ter de ficar como servidor de backup
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
    DataInputStream reciver;
    DataOutputStream sender;
    String message;
    RMIServerInterface remoteConection;

    NewClient(Socket cliente, RMIServerInterface rmiConection) {

        try {
            /*
             Guarda o cliente que se ligou anteriormente ao servidor
             e também a ligação com o servidor RMI
             */
            myClient = cliente;
            remoteConection = rmiConection;

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
            System.out.println("[Server] Erro no Client Conection: " + e.getMessage());
        }
    }

    public void run() {

        try {
            message = reciver.readUTF();

            String teste = remoteConection.testeMessage(message);

            /*if (message.equals("Teste")) {
             message = "Mesagem de teste recebida";
             } else {
             message = "Error!!";
             }*/
            sender.writeUTF(teste);
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

        primaryServer = hostIp;
        failOverCounter = 0;
        this.start();

    }

    public void run() {

        try {

            udpConection = new DatagramSocket();

            while (true) {

                try {

                    pingMessage = "ping".getBytes();
                    hostConection = InetAddress.getByName(primaryServer);
                    sender = new DatagramPacket(pingMessage, pingMessage.length, hostConection, serverPort);
                    udpConection.send(sender);
                    udpConection.setSoTimeout(2000);
                    pingMessage = new byte[1000];
                    reciver = new DatagramPacket(pingMessage, pingMessage.length);
                    udpConection.receive(reciver);
                    System.out.println("[Backup Server] Recebi esta mensagem do Serviodr Principal: " + new String(reciver.getData(), 0, reciver.getLength()));

                    Thread.sleep(5000);

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

class UDPServer extends Thread {

    DatagramSocket conection;
    byte[] buffer = new byte[1000];
    DatagramPacket sender, reciver;
    int serverPort = 6060;
    String pingMessage;

    UDPServer() {

        this.start();

    }

    public void run() {
        try {

            conection = new DatagramSocket(serverPort);

            while (true) {

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
