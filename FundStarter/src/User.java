
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
public class User implements Serializable{
    
    private String name;
    private String pass;
    
    public User(String name, String pass){
        
        this.name=name;
        this.pass=pass;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getPass(){
        return this.pass;
    }
}
