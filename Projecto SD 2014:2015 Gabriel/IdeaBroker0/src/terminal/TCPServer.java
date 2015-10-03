package terminal;
import java.net.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;

public class TCPServer extends UnicastRemoteObject {

	private static final long serialVersionUID = 1L;

	public TCPServer () throws RemoteException {

        super();
    }

	public static void main(String args[]) {

        if (args.length == 0) {
            System.out.println("java TCPServer [backupServerHostname] [RMIServerHostname]");
            System.exit(0);
        }

        try {
            DatagramSocket aSocket = new DatagramSocket(5678);
            InetAddress aHost = InetAddress.getByName(args[0]);
            int serverPort = 1234;  
            
            String texto = "Algum Servidor já ligado?";
            byte [] m = texto.getBytes();                                                    
            DatagramPacket request = new DatagramPacket(m,m.length, aHost, serverPort);

            aSocket.setSoTimeout(100);
            aSocket.send(request);

            byte[] buffer = new byte[1000];
            DatagramPacket ack = new DatagramPacket(buffer, buffer.length);
            int tries = 1;
            int count=0;

            while (tries <= 5) {
                try {
                    aSocket.receive(ack);
                } catch (SocketTimeoutException e) {
                    tries++;
                }
                aSocket.send(request);
                count++;
                if(count == 500){
                    Utils.print("Primary Server is Alive! :)");
                    count = 0;
                }
            }
            new UDPThread(aHost, serverPort);
        } catch (SocketException se) {
        } catch (IOException e) {
        }



        System.getProperties().put("java.security.policy", "policy.all");
        //System.setSecurityManager(new RMISecurityManager());

        RMIServerInterface rmi_server;
		
        int numero = 0;

        try {
            Notifier not = new Notifier();

            rmi_server = (RMIServerInterface) Naming.lookup("rmi://"+args[1]+":7000/RMIServer");
            int tcpServerPort = 6000;
            Utils.print("A Escuta no Porto " + tcpServerPort);
            ServerSocket listenSocket = new ServerSocket(tcpServerPort);
            Utils.print("LISTEN SOCKET=" + listenSocket);
            while(true) {   // começa a receber clientes
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                Utils.print("CLIENT_SOCKET (created at accept())="+clientSocket);
                numero ++;
                new TCPSCConnection(clientSocket, numero, rmi_server, not);   
            }
		} catch (IOException e) {
			System.out.println("[TCPServer - main] Listen:" + e.getMessage());
		} catch (NotBoundException e) {}
	}
}