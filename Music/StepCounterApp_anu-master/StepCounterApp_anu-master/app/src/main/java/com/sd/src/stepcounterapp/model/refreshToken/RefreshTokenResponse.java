
package com.sd.src.stepcounterapp.model.refreshToken;

public class RefreshTokenResponse {

    private com.sd.src.stepcounterapp.model.refreshToken.Data data;
    private String message;
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
