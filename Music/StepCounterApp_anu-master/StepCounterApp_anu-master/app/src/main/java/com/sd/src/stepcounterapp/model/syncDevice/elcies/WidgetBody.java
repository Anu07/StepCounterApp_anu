
package com.sd.src.stepcounterapp.model.syncDevice.elcies;

public class WidgetBody {

    private String name;
    private Boolean status;
    private String userId;

    public WidgetBody(String name, Boolean status, String userId) {
        this.name = name;
        this.status = status;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
