package com.sd.src.stepcounterapp.model.contactUs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactUsRequest {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("isLocked")
    @Expose
    private Boolean isLocked;

    public ContactUsRequest(String userId, Boolean isLocked, String message) {
        this.userId = userId;
        this.isLocked = isLocked;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    private String message;

}
