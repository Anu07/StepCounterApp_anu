
package com.sd.src.stepcounterapp.model.wallet;


@SuppressWarnings("unused")
public class TokenModel {

    private Data data;
    private String message;
    private long status;
    
    public Data getData() {
        return data;
    }
    
    public void setData(Data data) {
        this.data = data;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public long getStatus() {
        return status;
    }
    
    public void setStatus(long status) {
        this.status = status;
    }
}
