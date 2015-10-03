package terminal;
import java.io.*;

public class ClientRead extends Thread {

	ObjectInputStream input;
	Data data;
	Object lock;
	TCPClient client;

	
	public ClientRead (TCPClient c, ObjectInputStream in, Object lock) {
		this.client = c;
		this.input = in;
		this.data = null;
		this.lock = lock;
		start();
	}

	public synchronized void run () {

		Data answer = null;
		Boolean running = true;

		//Utils.print("Thread de leitura inicializada");

		while (running) {
			try {

				//Utils.clearConsole();
				answer = (Data) input.readObject();

				if (answer.getOrder() == -3) {
					System.out.print(answer.pollData());
				}
				else if (answer.getOrder() == -2) {
					Utils.print(answer.pollData());
					synchronized (lock) {
						lock.notifyAll();
					}
					break;
				}
				else {
					this.data = answer;
				}
				answer = null;
				synchronized (lock) {
					lock.notifyAll();
				}
			} catch (IOException e) {
			//System.out.println("IO:" + e.getMessage());
				try {
					this.sleep(1000);
				} catch (InterruptedException ie) {
				}
				client.connect(true);
			} catch (ClassNotFoundException ce) {
			}
		}
	}

	public Data getData () {
		Data tmp = this.data;
		this.data = null;
		return tmp;
	}

	public void setInputStream (ObjectInputStream ois) {
		this.input = ois;
	}
}