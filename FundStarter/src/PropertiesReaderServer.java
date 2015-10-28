
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * FundStart
 *  Projecto para a cadeira de Sistemas Distribuidos
 *  Ano Lectivo 2015/1016
 *  Carlos Pinto 2011143469
 *  Diana Umbelino 2012******
 *  Tomás Carvalho 2012138578
 */
/**
 *
 * @author gabrieloliveira
 */
public class PropertiesReaderServer {

    String primaryIp;
    int primaryPort;
    String backupIP;
    int backupPort;
    int UDPPort;
    String rmiLocation;

    /**
     * Vai ler os dados do ficheiro configServer.properties para que o servidor
     * possa cria sockets com os ip's indicados e no ficheiro e para ligar ao RMI.
     */
    public PropertiesReaderServer() {
        FileInputStream input = null;
        Properties prop = new java.util.Properties();

        try {
            input = new FileInputStream("src/configServer.properties");

            if (input == null) {
                System.out.println("[Server]Não encontrei o ficheiro de configurações!");
                return;
            }

            prop.load(input);

            this.primaryIp = prop.getProperty("MyIP");
            this.primaryPort = Integer.parseInt(prop.getProperty("MyPort"));
            this.backupIP = prop.getProperty("BackupIP");
            this.backupPort = Integer.parseInt(prop.getProperty("BackupPort"));
            this.UDPPort = Integer.parseInt(prop.getProperty("UDPPort"));
            this.rmiLocation = prop.getProperty("RMILocation");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
   

    /**
     * Sempre que um servidor vai abaixo troca os ip's e portos do ficheiro de propriedades.
     */
    public void switchIPS() {
        
        Properties prop = new java.util.Properties();
        String temp;
        try {

            FileOutputStream output = new FileOutputStream("src/configServer.properties");

            if (output == null) {
                System.out.println("[Server]Não encontrei o ficheiro de configurações!");
            }

            prop.setProperty("MyIP", backupIP);
            temp = "" + backupPort;
            prop.setProperty("MyPort", temp);
            prop.setProperty("BackupIP", primaryIp);
            temp = "" + primaryPort;
            prop.setProperty("BackupPort", temp);
            prop.setProperty("RMILocation", rmiLocation);
            temp = "" + UDPPort;
            prop.setProperty("UDPPort", temp);

            prop.store(output, "");

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int getPrimaryPort() {
        return primaryPort;
    }

    public String getBackupIP() {
        return backupIP;
    }

    public String getPrimaryIp() {
        return primaryIp;
    }

    public int getUDPPort() {
        return UDPPort;
    }

    public int getBackupPort() {
        return backupPort;
    }

    public String getRmiLocation() {
        return rmiLocation;
    }

}
