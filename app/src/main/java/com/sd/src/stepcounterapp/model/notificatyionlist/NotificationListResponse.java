
package com.sd.src.stepcounterapp.model.notificatyionlist;

import java.util.List;

@SuppressWarnings("unused")
public class NotificationListResponse {

    private List<NotificationData> data;
    private String message;
    private int status;


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

    public List<NotificationData> getData() {
        return data;
    }

    public void setData(List<NotificationData> data) {
        this.data = data;
    }
}
