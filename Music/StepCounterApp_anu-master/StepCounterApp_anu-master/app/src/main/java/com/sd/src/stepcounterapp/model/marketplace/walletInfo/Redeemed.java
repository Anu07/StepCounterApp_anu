package com.sd.src.stepcounterapp.model.marketplace.walletInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 * */
public class Redeemed {
  @SerializedName("image")
  @Expose
  private String image;
  @SerializedName("rewardId")
  @Expose
  private String rewardId;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("description")
  @Expose
  private String description;
  @SerializedName("redeemedAt")
  @Expose
  private String redeemedAt;
  @SerializedName("shortDesc")
  @Expose
  private String shortDesc;
  @SerializedName("_id")
  @Expose
  private String _id;
  @SerializedName("token")
  @Expose
  private Integer token;
  public void setImage(String image){
   this.image=image;
  }
  public String getImage(){
   return image;
  }
  public void setRewardId(String rewardId){
   this.rewardId=rewardId;
  }
  public String getRewardId(){
   return rewardId;
  }
  public void setName(String name){
   this.name=name;
  }
  public String getName(){
   return name;
  }
  public void setDescription(String description){
   this.description=description;
  }
  public String getDescription(){
   return description;
  }
  public void setRedeemedAt(String redeemedAt){
   this.redeemedAt=redeemedAt;
  }
  public String getRedeemedAt(){
   return redeemedAt;
  }
  public void setShortDesc(String shortDesc){
   this.shortDesc=shortDesc;
  }
  public String getShortDesc(){
   return shortDesc;
  }
  public void set_id(String _id){
   this._id=_id;
  }
  public String get_id(){
   return _id;
  }
  public void setToken(Integer token){
   this.token=token;
  }
  public Integer getToken(){
   return token;
  }
}