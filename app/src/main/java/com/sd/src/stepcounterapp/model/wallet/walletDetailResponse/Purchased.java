
package com.sd.src.stepcounterapp.model.wallet.walletDetailResponse;

@SuppressWarnings("unused")
public class Purchased {

    private String _id;
    private String description;
    private String image;
    private String name;
    private String rewardId;
    private String shortDesc;
    private long token;
    
    public String get_id() {
        return _id;
    }
    
    public void set_id(String _id) {
        this._id = _id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRewardId() {
        return rewardId;
    }
    
    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }
    
    public String getShortDesc() {
        return shortDesc;
    }
    
    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
    
    public long getToken() {
        return token;
    }
    
    public void setToken(long token) {
        this.token = token;
    }
}
