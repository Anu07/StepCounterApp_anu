
package com.sd.src.stepcounterapp.model.login;

public class Data {

    private String _id;
    private Boolean basicFlag;
    private String dob;
    private String elciesCallbackurl;
    private int elciesUserId;
    private String email;
    private String firstName;
    private String image;
    private Boolean isElciesConnected = true;
    private String lastName;
    private Boolean rewardFlag;
    private String username;
    private String wearableDevice;

    public Data(String _id, Boolean basicFlag, String dob, String elciesCallbackurl, int elciesUserId, String email, String firstName, String image, Boolean isElciesConnected, String lastName, Boolean rewardFlag, String username, String wearableDevice) {
        this._id = _id;
        this.basicFlag = basicFlag;
        this.dob = dob;
        this.elciesCallbackurl = elciesCallbackurl;
        this.elciesUserId = elciesUserId;
        this.email = email;
        this.firstName = firstName;
        this.image = image;
        this.isElciesConnected = isElciesConnected;
        this.lastName = lastName;
        this.rewardFlag = rewardFlag;
        this.username = username;
        this.wearableDevice = wearableDevice;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Boolean getBasicFlag() {
        return basicFlag;
    }

    public void setBasicFlag(Boolean basicFlag) {
        this.basicFlag = basicFlag;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getElciesCallbackurl() {
        return elciesCallbackurl;
    }

    public void setElciesCallbackurl(String elciesCallbackurl) {
        this.elciesCallbackurl = elciesCallbackurl;
    }


    public int getElciesUserId() {
        return elciesUserId;
    }

    public void setElciesUserId(int elciesUserId) {
        this.elciesUserId = elciesUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getElciesConnected() {
        return isElciesConnected;
    }

    public void setElciesConnected(Boolean elciesConnected) {
        isElciesConnected = elciesConnected;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getRewardFlag() {
        return rewardFlag;
    }

    public void setRewardFlag(Boolean rewardFlag) {
        this.rewardFlag = rewardFlag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWearableDevice() {
        return wearableDevice;
    }

    public void setWearableDevice(String wearableDevice) {
        this.wearableDevice = wearableDevice;
    }
}
