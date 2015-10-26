
import java.net.*;
import java.io.*;
import java.rmi.*;

/*
 * FundStart
 *  Projecto para a cadeira de Sistemas Distribuidos
 *  Ano Lectivo 2015/1016
 *  Carlos Pinto 2011143469
 *  Diana Umbelino 2012******
 *  Tomás Carvalho 2012******
 */
/**
 *
 * @author gabrieloliveira
 */
public class Server {

    private String serverIP;
    private int serverPort;
    private int UDPPort;
    private int backupPort;
    private String backupIP;
    private String rmiLocation;

    public Server(boolean flag) {
        try {

            PropertiesReaderServer properties = new PropertiesReaderServer();

            /**
             * Cria uma nova intancia do PropertiesReader para aceder aos dados que estão no ficheiro configServer.properties. Depois vai criar um socket para fazer a ligação com os clientes.
             */
            serverIP = properties.getPrimaryIp();
            serverPort = properties.getPrimaryPort();
            UDPPort = properties.getUDPPort();
            backupPort = properties.getBackupPort();
            backupIP = properties.getBackupIP();
            rmiLocation = properties.getRmiLocation();



            if (flag) {
                new BackupServer(serverIP, UDPPort);
            } else {
               
                ServerSocket conectionToClient = new ServerSocket(serverPort);
                Socket cliente;

                /**
                 * Cria uma intância da classe UDPServer para receber ping's do servidor backup e responder para mostrar ao backup que ainda está vivo.
                 */
                UDPServer conectServer = new UDPServer(UDPPort);

                /**
                 * Cria ligação com o servidor RMI.
                 */
                RMIServerInterface remoteConection = (RMIServerInterface) Naming.lookup(rmiLocation);

                System.out.println("[Server] Servidor à escuta no porto " + serverPort);

                while (true) {

                    /**
                     * Espera que um cliente se ligue.
                     */
                    cliente = conectionToClient.accept();

                    /**
                     * Por cada cliente que se liga, vai criar uma thread que fica encarregue de lidar com ele.
                     */
                    new NewClient(cliente, remoteConection);

                }
            }

        } catch (IOException e) {
            /**
             * Esta excepção é apanhada quando já existe um servidor activo. Neste caso o servidor vai agir como servidor backup.
             */
            if (e.getMessage().equals("Address already in use")) {
                new BackupServer(serverIP, UDPPort);

            } else {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.print("[Server]");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        new Server(true);

    }
}

class NewClient extends Thread {

    Socket myClient;
    ObjectInputStream reciver;
    ObjectOutputStream sender;
    ClientRequest postCard;
    ClientRequest myMail;
    RMIServerInterface remoteConection;
    int myUserID;
    int myProjectID;
    String alterRequest;

    NewClient(Socket cliente, RMIServerInterface rmiConection) {

        try {
            /**
             * Guarda o cliente que se ligou anteriormente ao servidor e também a ligação com o servidor RMI.
             */
            myClient = cliente;
            remoteConection = rmiConection;

            /**
             * cria canais de comunicação com os clientes.
             */
            reciver = new ObjectInputStream(myClient.getInputStream());
            sender = new ObjectOutputStream(myClient.getOutputStream());
            sender.flush();

            /**
             * vai iniciar a thread.
             */
            this.start();

        } catch (IOException e) {
            System.out.print("[Server]");
            e.printStackTrace();
        }
    }

    public void run() {

        try {
            while (true) {

                postCard = null;
                postCard = (ClientRequest) reciver.readUnshared();

                alterRequest = postCard.getRequestID() + ("_" + myUserID);
                postCard.setRequestID(alterRequest);
                System.out.println(postCard.getRequest()[0]);

                System.out.println("[Server] Li a mensagem do cliente na boa.");
                //mudar depois para um switch
                if (postCard.getRequest()[0].equals("log")) {

                    postCard.setStage(1);
                    myMail = remoteConection.verificaLogIn(postCard);

                    if (myMail.getResponse()[0].equals("userrec")) {
                        myUserID = (int) myMail.getResponse()[1];
                    }

                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("new")) {
                    System.out.println("Fui chamado!");
                    if (postCard.getResponse() != null) {
                        System.out.println("Não és null por que caralho!&");
                    }

                    postCard.setStage(1);

                    myMail = remoteConection.novoUtilizador(postCard);

                    if (myMail.getResponse()[0].equals("infosave")) {
                        System.out.println("myUserID:" + (int) myMail.getResponse()[1]);
                        myUserID = (int) myMail.getResponse()[1];
                    } else if (myMail.getResponse()[0].equals("erro")) {
                        System.out.println("ERRO!\n"); //Temos que tratar o erro
                    } else if (myMail.getResponse()[0].equals("user_already_exists")) {
                        System.out.println("User: " + (String) myMail.getResponse()[1] + " already exists!");
                    }

                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("new_project")) {

                    postCard.setStage(1);

                    myMail = remoteConection.novoProjecto(postCard);

                    if (myMail.getResponse()[0].equals("infosave")) {
                        System.out.println("myProjectID:" + (int) myMail.getResponse()[1]);
                        myProjectID = (int) myMail.getResponse()[1];
                    }

                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("seesal")) {

                    System.out.println("Vim consultar o meu saldo\n");

                    postCard.getRequest()[1] = myUserID;
                    postCard.setStage(1);

                    myMail = remoteConection.getUserSaldo(postCard);

                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("list_actual_projects")) {
                    System.out.print("Vim consultar os projectos actuais!\n");
                    postCard.setStage(1);

                    myMail = remoteConection.getActualProjects(postCard);

                    myMail.setStage(4);

                }

                sender.writeUnshared(myMail);
            }
        } catch (Exception e) {
            System.out.print("[Server]");
            e.printStackTrace();
        }
    }
}

class BackupServer extends Thread {

    String primaryServer;
    DatagramSocket udpConection;
    byte[] pingMessage;
    int conectionFail = 0;
    InetAddress hostConection;
    int serverPort;
    DatagramPacket sender, reciver;
    int failOverCounter;
    String[] args = null;

    BackupServer(String hostIp, int udpPort) {

        /**
         * Vai guradar o ip do Servidor principal para se poder ligar como backup e inicializa o contador para o caso de ocorrer um FailOver.
         */
        primaryServer = hostIp;
        serverPort = udpPort;
        failOverCounter = 0;
        this.start();

    }

    public void run() {

        try {

            /*+
             Vai criar uma socket para as ligações UDP.
             */
            udpConection = new DatagramSocket();

            while (true) {

                try {

                    /**
                     * vai fazer a ligação com o Servidor principal e mandar a mensagem 'ping'. Caso o servidor principal vai abaixo e espera até à terceira mensagem de erro para se tornar servdor principal.
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

                        PropertiesReaderServer prop = new PropertiesReaderServer();
                        prop.switchIPS();
                        new Server(false);
                    }
                }

            }

        } catch (Exception e) {
            System.out.print("[BackupServer]");
            e.printStackTrace();
        }

    }
}

class UDPServer extends Thread {

    DatagramSocket conection;
    byte[] buffer = new byte[1000];
    DatagramPacket sender, reciver;
    int serverPort;
    String pingMessage;

    UDPServer(int port) {

        this.serverPort = port;
        this.start();

    }

    public void run() {
        try {

            /**
             * cria ligação UDP no porto indicado na variável serverPort.
             */
            conection = new DatagramSocket(serverPort);

            while (true) {

                /**
                 * recebe mensagem do Servidor Backup e volta a mandar a mesma mensagem.
                 */
                reciver = new DatagramPacket(buffer, buffer.length);
                conection.receive(reciver);
                pingMessage = new String(reciver.getData(), 0, reciver.getLength());
                System.out.println("[UDPServer]Message from Backup Server: " + pingMessage);
                sender = new DatagramPacket(reciver.getData(), reciver.getLength(), reciver.getAddress(), reciver.getPort());
                conection.send(sender);

            }

        } catch (Exception e) {
            System.out.print("[UDPServer]");
            e.printStackTrace();
        }
    }

}
