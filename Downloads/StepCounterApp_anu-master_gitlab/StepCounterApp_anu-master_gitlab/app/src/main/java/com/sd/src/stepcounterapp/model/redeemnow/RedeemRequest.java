package com.sd.src.stepcounterapp.model.redeemnow;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class RedeemRequest{
  @SerializedName("productId")
  @Expose
  private String productId;
  @SerializedName("userId")
  @Expose
  private String userId;
    
    public RedeemRequest(String productId, String userId) {
        this.productId = productId;
        this.userId = userId;
    }
    
    public void setProductId(String productId){
   this.productId=productId;
  }
  public String getProductId(){
   return productId;
  }
  public void setUserId(String userId){
   this.userId=userId;
  }
  public String getUserId(){
   return userId;
  }
}