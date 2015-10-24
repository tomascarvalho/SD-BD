
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabrieloliveira
 */
public class PropertiesReaderRMI {
    
    int port;
    String name;
    String BDIP;
    int BDPort;
    
    /**
     * Vai ler os dados do ficheiro configRMI.properties para fazer as ligaões do RMI.
     */
    public PropertiesReaderRMI() {
        
        FileInputStream input = null;
        Properties prop = new java.util.Properties();

        try {
            input = new FileInputStream("src/configRMI.properties");

            if (input == null) {
                System.out.println("[RMIServer]Não encontrei ficheiro de configurações!");
                return;
            }

            prop.load(input);
            
            this.port=Integer.parseInt(prop.getProperty("port"));
            this.name=prop.getProperty("name");
            this.BDIP=prop.getProperty("DBIP");
            this.BDPort=Integer.parseInt("DBPort");

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

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public String getBDIP() {
        return BDIP;
    }

    public int getBDPort() {
        return BDPort;
    }
    
    
}
