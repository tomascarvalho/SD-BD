package code;

/*
 * FundStart
 *  Projecto para a cadeira de Sistemas Distribuidos
 *  Ano Lectivo 2015/1016
 *  Carlos Pinto 2011143469
 *  Diana Umbelino 2012169525
 *  Tomás Carvalho 2012138578
 */

/**
 *
 * @author gabrieloliveira
 */

import java.net.*;
import java.io.*;
import static java.lang.Thread.sleep;
import java.rmi.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmiServer.*;


public class Server {

    private String serverIP;
    private int serverPort;
    private int UDPPort;
    private int backupPort;
    private String backupIP;
    private String rmiLocation;
    private Socket tryConnectToServer;
    private boolean backup = true;
    private int userIDCounter;

    public Server(String flag) {

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
            userIDCounter = 0;

            try {
                tryConnectToServer = new Socket(serverIP, serverPort);

            } catch (IOException e) {
                backup = false;
            }

            if (backup) {
                tryConnectToServer.close();
                new BackupServer(serverIP, UDPPort);
            } else {

                ServerSocket conectionToClient = new ServerSocket(serverPort);
                Socket cliente;
                System.out.println(flag);
                /**
                 * Cria uma intância da classe UDPServer para receber ping's do servidor backup e responder para mostrar ao backup que ainda está vivo.
                 */
                UDPServer conectServer = new UDPServer(UDPPort);

                /**
                 * Cria ligação com o servidor RMI.
                 */
                RMIServerInterface remoteConection = (RMIServerInterface) Naming.lookup(rmiLocation);

                /*if (flag.equals("backup_to_primary")) {
                 System.out.println("[Server]Vou avisar o rmi para desligar o outro servidor!");

                 }*/
                new cronoThread(remoteConection, rmiLocation);
                System.out.println("[Server] Servidor à escuta no porto " + serverPort);

                while (true) {

                    /**
                     * Espera que um cliente se ligue.
                     */
                    cliente = conectionToClient.accept();

                    /**
                     * Por cada cliente que se liga, vai criar uma thread que fica encarregue de lidar com ele.
                     */
                    new NewClient(cliente, remoteConection, rmiLocation);

                }
            }

        } catch (IOException e) {
            /**
             * Esta excepção é apanhada quando já existe um servidor activo. Neste caso o servidor vai agir como servidor backup.
             *
             * if (e.getMessage().equals("Address already in use")) { new BackupServer(serverIP, UDPPort);
             *
             * } else { e.printStackTrace(); }
             */
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        new Server("start_server");

    }
}

class cronoThread extends Thread {

    RMIServerInterface remoteConection;
    String rmiLocation;

    public cronoThread(RMIServerInterface rmiConection, String rmiPlace) {

        remoteConection = rmiConection;
        rmiLocation = rmiPlace;
        this.start();

    }

    public void run() {

        Date time = new Date();

        while (true) {
            if (time.getHours() == 16) {
                try {
                    System.out.println("Estou a correr");
                    remoteConection.terminaProjecto();
                } catch (RemoteException ex) {
                    System.out.println("Houve um erro com o RMI.");
                    while (true) {
                        try {
                            System.out.println("[CronoThread]Vou tentar ligar ao RMI!");
                            remoteConection = (RMIServerInterface) Naming.lookup(rmiLocation);
                            System.out.println("[CronoThread]Voltei a ligar ao RMI!");
                            break;
                        } catch (NotBoundException e) {
                            System.out.println("[CronoThread]Não me consigo ligar ao RMI!");
                        } catch (MalformedURLException e) {
                            System.out.println("[CronoThread]MalformedRLException");
                        } catch (RemoteException e) {
                            System.out.println("[CronoThread]RemoteException");
                        }
                    }
                }
            }
            try {
                Thread.sleep(3600000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
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
    String alterRequest;
    String rmiLocation;

    NewClient(Socket cliente, RMIServerInterface rmiConection, String ipRMI) {

        try {
            /**
             * Guarda o cliente que se ligou anteriormente ao servidor e também a ligação com o servidor RMI.
             */
            myClient = cliente;
            remoteConection = rmiConection;
            rmiLocation = ipRMI;

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

        } catch (EOFException ex) {
            System.out.println("Servidor Backup tentou ligar-se");
            return;
        } catch (IOException e) {
            System.out.print("[NewClient]");
            e.printStackTrace();
        }
    }

    public void run() {

        while (true) {
            try {

                postCard = (ClientRequest) reciver.readUnshared();

                if (postCard.getRequest()[0].equals("log")) {

                    postCard.setStage(1);
                    myMail = remoteConection.verificaLogIn(postCard);

                    if (myMail.getResponse()[0].equals("log_in_correcto")) {
                        System.out.println("Log in Correcto");
                        myUserID = (int) myMail.getResponse()[1];
                    } else if (myMail.getResponse()[0].equals("log_in_error")) {
                        System.out.println("Log in errado (Server error)");
                        myUserID = (int) myMail.getResponse()[1];
                    }

                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("new")) {

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
                    postCard.getRequest()[0] = myUserID;

                    myMail = remoteConection.novoProjecto(postCard);



                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("seesal")) {

                    postCard.getRequest()[1] = myUserID;
                    postCard.setStage(1);
                    myMail = remoteConection.getUserSaldo(postCard);
                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("mailbox")) {

                    System.out.println("Caixa de Correio\n");

                    postCard.getRequest()[0] = myUserID;
                    postCard.setStage(1);
                    myMail = remoteConection.caixaCorreio(postCard);

                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("answer")) {
                    postCard.getRequest()[1] = myUserID;
                    postCard.setStage(1);
                    myMail = remoteConection.veResposta(postCard);
                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("resp_mess")) {
                    System.out.print("Responder mensagem\n");

                    postCard.getRequest()[0] = 0;
                    postCard.setStage(1);
                    myMail = remoteConection.respMensagem(postCard);
                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("send_mess")) {
                    System.out.print("Enviar mensagem\n");

                    postCard.getRequest()[0] = myUserID;
                    postCard.setStage(1);
                    myMail = remoteConection.enviaMensagem(postCard);
                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("delete_project")) {
                    System.out.println("Apagar um projecto\n");

                    postCard.getRequest()[0] = myUserID;
                    postCard.setStage(1);
                    myMail = remoteConection.apagaProjecto(postCard);
                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("list_my_projects")) {
                    System.out.println("Vim consultar os meus projectos!\n");

                    postCard.getRequest()[1] = myUserID;
                    postCard.setStage(1);

                    myMail = remoteConection.getUserProjects(postCard);
                    myMail.setStage(4);

                } else if ((postCard.getRequest()[0].equals("list_actual_projects")) || (postCard.getRequest()[0].equals("list_old_projects"))) {
                    System.out.print("Vim consultar os projectos!\n");

                    postCard.setStage(1);
                    myMail = remoteConection.getActualProjects(postCard);
                    myMail.setStage(4);

                } else if ((postCard.getRequest()[0].equals("list_project_details"))) {
                    postCard.setStage(1);
                    myMail = remoteConection.getProjectDetails(postCard);
                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("pledge")) {
                    postCard.getRequest()[0] = myUserID;
                    myMail = remoteConection.pledgeToProject(postCard);
                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("add_Admin")) {
                    postCard.setStage(1);
                    myMail = remoteConection.addAdminToProject(postCard);
                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("vote_for_product")) {
                    System.out.println("[Server] Vote For Product");
                    postCard.setStage(1);
                    myMail = remoteConection.voteForProduct(postCard);
                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("donate_reward_take_mine_away")) {
                    System.out.println("[Server] Donating Reward");
                    postCard.setStage(1);
                    myMail = remoteConection.donateReward(postCard);
                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("listar_recompensas")) {
                    System.out.println("[Server] Listar Rewards");
                    postCard.getRequest()[1] = myUserID;
                    postCard.setStage(1);
                    myMail = remoteConection.listarRecompensas(postCard);
                    myMail.setStage(4);


                } else if (postCard.getRequest()[0].equals("new_reward")){
                    System.out.println("[Server] Adicionar Recompensa");
                    postCard.getRequest()[0] = myUserID;
                    postCard.setStage(1);
                    myMail = remoteConection.addReward(postCard);
                    myMail.setStage(4);

                } else if (postCard.getRequest()[0].equals("delete_reward")){
                    System.out.println("[Server] Apagar Recompensa");
                    postCard.getRequest()[3] = myUserID;
                    postCard.setStage(1);
                    myMail = remoteConection.deleteReward(postCard);
                    myMail.setStage(4);
                }

                System.out.println("Passou");
                sender.reset();
                sender.writeUnshared(myMail);
                postCard = null;

            } catch (RemoteException ex) {

                while (true) {
                    try {
                        System.out.println("[Server]RMI está down.");
                        postCard = null;
                        remoteConection = (RMIServerInterface) Naming.lookup(rmiLocation);

                       while(!remoteConection.checkDataBaseConection().equals("done"));
                        Object[] rmiDown = {"rmidown"};
                        ClientRequest temp = new ClientRequest("", rmiDown, "");
                        sender.reset();
                        sender.writeUnshared(temp);
                        break;
                    } catch (NotBoundException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        System.out.println("[Server]RemoteException");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (EOFException ex) {
                System.out.println("Cliente desligou-se!");
                return;

            } catch (IOException ex) {
                System.out.println("Cliente desligou-se!");
                return;
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();

            }
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

                        System.out.println("[Backup Server] O Servidor principal está em baixo, vou assumir o controlo.");
                        udpConection.close();

                        PropertiesReaderServer prop = new PropertiesReaderServer();
                        prop.switchIPS();
                        new Server("backup_to_primary");
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
