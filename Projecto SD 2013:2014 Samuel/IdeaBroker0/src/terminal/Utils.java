package terminal;
import java.sql.Timestamp;
import java.io.*;

public class Utils {
	
	public static void print(String s) {
		String time = new Timestamp(new java.util.Date().getTime()).toString();

		while (time.length() < 23) {
			time+="0";
		}

		System.out.println("["+time+"] "+s);
	}

	public static void clearConsole() {
		
		System.out.println("\033[2J\n");
	}

	public static Data readData (ObjectInputStream ois) {
		try {
			return (Data) ois.readObject();
		} catch (IOException e) {
		} catch (ClassNotFoundException ce) {
		}
		return null;
	}

	public static void writeData (Data data, ObjectOutputStream oos) {
		try {
			oos.reset();	
			oos.writeObject(data);
			oos.flush();
		} catch (IOException e) {
			//se o não conseguir escrever o pacote reenvia.
			writeData(data, oos);
		}
	}
}