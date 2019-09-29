package com.sd.src.stepcounterapp.model.rewards;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class RewardsCategoriesResponse{
  @SerializedName("data")
  @Expose
  private List<Data> data;
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setData(List<Data> data){
   this.data=data;
  }
  public List<Data> getData(){
   return data;
  }
  public void setMessage(String message){
   this.message=message;
  }
  public String getMessage(){
   return message;
  }
  public void setStatus(Integer status){
   this.status=status;
  }
  public Integer getStatus(){
   return status;
  }
}