package rmiServer;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReaderRMI {

    int port;
    String name;
    String BDIP;
    String BDPort;

    /**
     * Vai ler os dados do ficheiro configRMI.properties para fazer as ligaões do RMI.
     */
    public PropertiesReaderRMI() {

        FileInputStream input = null;
        Properties prop = new java.util.Properties();

        try {
            input = new FileInputStream("rmiServer/configRMI.properties");

            if (input == null) {
                System.out.println("[RMIServer]Não encontrei ficheiro de configurações!");
                return;
            }

            prop.load(input);

            this.port=Integer.parseInt(prop.getProperty("port"));
            this.name=prop.getProperty("name");
            this.BDIP=prop.getProperty("DBIP");
            this.BDPort=prop.getProperty("DBPort");

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

    public String getBDPort() {
        return BDPort;
    }


}
