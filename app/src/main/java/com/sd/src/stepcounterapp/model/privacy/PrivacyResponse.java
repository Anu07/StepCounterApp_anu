
package com.sd.src.stepcounterapp.model.privacy;

import java.util.List;
@SuppressWarnings("unused")
public class PrivacyResponse {

    private List<Datum> data;
    private String message;
    private int status;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
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
