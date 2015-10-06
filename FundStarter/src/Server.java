
import java.net.*;
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
public class Server {
    
    
    public static void main(String args[]){
        
        try{
            
            /*
                Cria um socket para ligação com clientes no porto indicado no
                serverPort
            */
            int serverPort=6000;
            ServerSocket conectionToClient=new ServerSocket(serverPort);
            Socket cliente;
            DataInputStream reciver;
            DataOutputStream sender;
            String message;
            
            System.out.println("Servidor à eescuta no porto 6000");
            
            /*
                Espera que um cliente se ligue
            */
            cliente=conectionToClient.accept();
            
            /*
                cria canais de comunicação com os clientes
            */
            reciver=new DataInputStream(cliente.getInputStream());
            sender=new DataOutputStream(cliente.getOutputStream());
            
             message=reciver.readUTF();
             
             if(message.equals("Teste")){
                 message="Mesagem de teste recebida";
             }
             else{
                 message="Error!!";
             }
             
             sender.writeUTF(message);            
            
        }catch(Exception e){
            /*falta aqui as excepções*/
        }
    }
    
}
