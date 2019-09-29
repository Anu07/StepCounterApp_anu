package com.sd.src.stepcounterapp.model.syncDevice;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Tokens{
  @SerializedName("date")
  @Expose
  private String date;
  @SerializedName("token")
  @Expose
  private Integer token;
  public void setDate(String date){
   this.date=date;
  }
  public String getDate(){
   return date;
  }
  public void setToken(Integer token){
   this.token=token;
  }
  public Integer getToken(){
   return token;
  }
}