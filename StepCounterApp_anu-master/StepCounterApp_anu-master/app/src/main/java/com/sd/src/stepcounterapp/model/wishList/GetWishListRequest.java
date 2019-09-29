package com.sd.src.stepcounterapp.model.wishList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class GetWishListRequest{
  @SerializedName("userId")
  @Expose
  private String userId;
    
    public GetWishListRequest(String userId) {
        this.userId = userId;
    }
    
    public void setUserId(String userId){
   this.userId=userId;
  }
  public String getUserId(){
   return userId;
  }
}