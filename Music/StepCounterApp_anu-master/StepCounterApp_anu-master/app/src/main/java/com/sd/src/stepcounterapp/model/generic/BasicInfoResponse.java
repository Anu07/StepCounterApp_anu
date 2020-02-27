package com.sd.src.stepcounterapp.model.generic;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class BasicInfoResponse{
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("status")
  @Expose
  private int status;
  public void setMessage(String message){
   this.message=message;
  }
  public String getMessage(){
   return message;
  }
  public void setStatus(int status){
   this.status=status;
  }
  public int getStatus(){
   return status;
  }
}