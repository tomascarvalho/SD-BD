
import java.io.*;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabrieloliveira
 */
public class User implements Serializable{
    
    private String name;
    private String pass;
    private int saldo;
    private ArrayList<String> recompensas=new ArrayList<String>();
    
    public User(String name, String pass){
        
        this.name=name;
        this.pass=pass;
        this.saldo=100;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getPass(){
        return this.pass;
    }
    
    public int getSaldo(){
        return this.saldo;
    }
}
