
package com.sd.src.stepcounterapp.model.changepwd;

@SuppressWarnings("unused")
public class ChangePasswordRequest {

    private String oldPassword;
    private String password;
    private String userId;

    public ChangePasswordRequest( String userId,String oldPassword, String password) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
