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

import java.util.*;
import java.io.*;
import rmiServer.*;

public class PropertiesReaderClient {

    String primaryIP,secundaryIP;
    int primaryPort,secundaryPort;

    /**
     * Vai ler os dados do ficheiro de propriedes para que o cliente se possa ligar aos servidores.
     */
    public PropertiesReaderClient() {

        FileInputStream input=null;
        Properties prop=new java.util.Properties();

        try{
            input=new FileInputStream("src/code/configClient.properties");

            if(input==null){
                System.out.println("[Client]Não encontrei ficheiro de configurações!");
                return;
            }

            prop.load(input);

            this.primaryIP=prop.getProperty("PrimaryServerIP");
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

    /**
     * Sempre que existe uma mudança de servidores vai escrever no ficheiro de propriesdades
     * para o caso de um cliente novo se ligar este não ir ligar ao servidor anterior.
     */
    public void writeOnFile(){

        FileOutputStream output;
        Properties prop=new java.util.Properties();
        String temp;

        try {
            output=new FileOutputStream("src/configClient.properties");

            if(output==null){
                System.out.println("[Client] Não encontrei o ficheiro de configurações!");
                return;
            }
            System.out.println(primaryIP);
            prop.setProperty("PrimaryServerIP", secundaryIP);
            temp=""+secundaryPort;
            prop.setProperty("PrimaryServerPort", temp);
            prop.setProperty("SecundaryServerIP", primaryIP);
            temp=""+primaryPort;
            prop.setProperty("SecundaryServerPort", temp);

            prop.store(output,"");

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
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
