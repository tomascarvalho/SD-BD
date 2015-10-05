
import java.lang.*;
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
public class Client extends Thread{
 
       public static void main(String args[]){
           
            String ipServer1,ipServer2;
            int port1,port2;
            Socket conectionToServer;
            String message="teste message";
            ObjectInputStream reciver;
            ObjectOutputStream sender;
           
            /*
            se o utilizador iniciar o programa e não meter ip's e portas dos
            servidores o programa entra neste if, imprime a mensagem de erro
            e termina
           
            PS: SE QUISEREM CORRER SÓ UM SERVIDOR PARA TERSTE METAM OS ULTIMOS
            CAMPOS COM NULL, ASSIM NÃO DÁ ERRO. :D
           */
           if(args.length!=4){
               System.out.println("java Client <IP1> <P1> <IP2> <P2>");
               System.exit(0);
           }
           
           else{
               /*
                Passa o argumentos para as variáveis para podermos fazer as
                ligações mais à frente
               */
               
               ipServer1=args[0];
               port1=Integer.parseInt(args[1]);
               ipServer2=args[2];
               port2=Integer.parseInt(args[3]);
               
               try{
                   
                   /*
                    cria ligação com o servidor que está com o IP->ipServer1 e
                    Porto->port1
                   */
                   conectionToServer=new Socket(ipServer1,port1);
                   
                   /*
                    vai iniciar os streams de input e output para partilhar
                    mensagens com o servidor
                   */
                   
                   reciver=new ObjectInputStream(conectionToServer.getInputStream());
                   sender=new ObjectOutputStream(conectionToServer.getOutputStream());
                   
                   
                   /*
                    vai mandar uma mensagem de teste ao servidor
                   */
                   
                   sender.writeUTF(message);
                   
                   Thread.sleep(5000);
                   
                   System.out.println(reciver.readUTF());
                   
               }catch(Exception e){
                   /*Ainda não sei qual pode ser a excepção que dá aqui*/
               }
               
               
           }
           
           
           
       }
}
