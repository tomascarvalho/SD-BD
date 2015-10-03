import java.net.*;
import java.io.*;

public class UDPThread extends Thread {
	
	private DatagramSocket socket;
	private DatagramPacket request;
	private DatagramPacket reply;
	private byte[] buffer;
	private byte [] m;
	private String replyMsg;
	private InetAddress host;
    private int serverPort;

	public UDPThread (InetAddress host, int sPort) {
		try {
			this.socket = new DatagramSocket(sPort);
		} catch (SocketException e) {
		}

		this.buffer = new byte[1000];
		this.request = new DatagramPacket(buffer, buffer.length);
		this.replyMsg = "Sim, estou ligado! :)";
		this.host = host;
		this.serverPort = 5678;
		this.m = replyMsg.getBytes();
		this.reply = new DatagramPacket(m, m.length, host, serverPort);

		this.start();
	}

	public void run () {
		Utils.print("A responder a UDP");
		while (true) {
			try {
				socket.receive(request);
				socket.send(reply);
			} catch (SocketTimeoutException e) {
			} catch (IOException e) {
			}
		}
	}
}