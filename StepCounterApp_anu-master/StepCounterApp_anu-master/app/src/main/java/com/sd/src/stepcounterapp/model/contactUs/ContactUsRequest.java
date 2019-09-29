package com.sd.src.stepcounterapp.model.contactUs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactUsRequest {

    @SerializedName("userId")
    @Expose
    private String userId;

    public ContactUsRequest(String userId, String message) {
        this.userId = userId;
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
