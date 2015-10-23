import java.util.*;
import java.io.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabrieloliveira
 */
public class PropertiesReaderClient {
    
    String primaryIP,secundaryIP;
    int primaryPort,secundaryPort;

    public PropertiesReaderClient() {
        
        FileInputStream input=null;
        Properties prop=new java.util.Properties();
        
        try{
            input=new FileInputStream("src/configClient.properties");
            
            if(input==null){
                System.out.println("[Client]Não encontrei ficheiro de configurações!");
            }
            
            prop.load(input);
            
            this.primaryIP=prop.getProperty("PrimaryServerIp");
            this.primaryPort=Integer.parseInt(prop.getProperty("PrimaryServerPort"));
            this.secundaryIP=prop.getProperty("SecundaryServerIP");
            this.secundaryPort=Integer.parseInt(prop.getProperty("SecundaryServerPort"));
            
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException ex) {
                    System.out.println("[Client]Erro a fechar o ficheiro de configurações!");
                }
            }
        }
    }

    public String getPrimaryIP() {
        return primaryIP;
    }

    public String getSecundaryIP() {
        return secundaryIP;
    }

    public int getPrimaryPort() {
        return primaryPort;
    }

    public int getSecundaryPort() {
        return secundaryPort;
    }
    
    
    
}
