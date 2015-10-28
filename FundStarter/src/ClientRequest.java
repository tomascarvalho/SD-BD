
import java.io.Serializable;

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
public class ClientRequest implements Serializable{
    
    /**
     * STAGE 
     * 
     * * * * clrqst -> Cliente enviou pedido para o Servidor
     * * * * inprog -> Servidor mandou pedido para o RMI
     * * * * rmiin  -> RMI começou a tratar do pedido
     * * * * rmiout -> RMI terminou processo e devolveu ao servidor
     * * * * bckcl  -> Servidor mando resposta do RMI para o Cliente
     * * * * done   -> Cliente já leu o pedido
     *
     */
    
    private String requestID;
    private String stage;
    private Object[] request;
    private Object[] response; 
    private String[] stgType={"clrqst","inprog","rmiin","rmiout","bckcl","done"};
    private String timestamp;
    
    
    public ClientRequest(String id,Object[] pedido,String time){
        
        this.requestID=id;
        this.request=pedido;
        this.stage=stgType[0];
        this.response=null;
        this.timestamp=time;
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

    public String getTimestamp() {
        return timestamp;
    }
    
    
    
}
