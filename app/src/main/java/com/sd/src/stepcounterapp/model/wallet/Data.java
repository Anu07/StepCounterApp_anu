package com.sd.src.stepcounterapp.model.wallet;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class Data{
  @SerializedName("totalEarnings")
  @Expose
  private Integer totalEarnings;
  @SerializedName("totalGenerated")
  @Expose
  private Integer totalGenerated;
  @SerializedName("redeemed")
  @Expose
  private List<Redeemed> redeemed;
  @SerializedName("wishlist")
  @Expose
  private List<Wishlist> wishlist;
  @SerializedName("days")
  @Expose
  private Integer days;
  @SerializedName("steps")
  @Expose
  private Integer steps;
  @SerializedName("updatedAt")
  @Expose
  private String updatedAt;
  public void setTotalEarnings(Integer totalEarnings){
   this.totalEarnings=totalEarnings;
  }
  public Integer getTotalEarnings(){
   return totalEarnings;
  }
  public void setTotalGenerated(Integer totalGenerated){
   this.totalGenerated=totalGenerated;
  }
  public Integer getTotalGenerated(){
   return totalGenerated;
  }
  public void setRedeemed(List<Redeemed> redeemed){
   this.redeemed=redeemed;
  }
  public List<Redeemed> getRedeemed(){
   return redeemed;
  }
  public void setWishlist(List<Wishlist> wishlist){
   this.wishlist=wishlist;
  }
  public List<Wishlist> getWishlist(){
   return wishlist;
  }
  public void setDays(Integer days){
   this.days=days;
  }
  public Integer getDays(){
   return days;
  }
  public void setSteps(Integer steps){
   this.steps=steps;
  }
  public Integer getSteps(){
   return steps;
  }
  public void setUpdatedAt(String updatedAt){
   this.updatedAt=updatedAt;
  }
  public String getUpdatedAt(){
   return updatedAt;
  }
}