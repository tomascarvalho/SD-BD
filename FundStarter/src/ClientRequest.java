/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabrieloliveira
 */
public class ClientRequest {
    
    /**
     * STAGE 
     * 
     * * * * clrqst -> Cliente enviou pedido para o Servidor
     * * * * inprog -> Servidor mandou pedido para o RMI
     * * * * rmiin  -> RMI começou a tratar do pedido
     * * * * rmiout -> RMI terminou processo e devolveu ao servidor
     * * * * bckcl  -> Servidor mando resposta do RMI para o Cliente
     *
     */
    
    private String requestID;
    private String stage;
    private Object[] request;
    private Object[] response; 
    private String[] stgType={"clrqst","inprog","rmiin","rmiout","bckcl"};
    
    
    public ClientRequest(String id,Object[] pedido){
        
        this.requestID=id;
        this.request=pedido;
        this.stage=stgType[0];
        this.response=null;
    }
    
    
    public String getRequestID(){
        return this.requestID;
    }

    public String getStage() {
        return stage;
    }

    public Object[] getRequest() {
        return request;
    }

    public Object[] getResponse() {
        return response;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public void setStage(int pos) {
        this.stage = stgType[pos];
    }

    public void setRequest(Object[] request) {
        this.request = request;
    }

    public void setResponse(Object[] response) {
        this.response = response;
    }
    
    
}
