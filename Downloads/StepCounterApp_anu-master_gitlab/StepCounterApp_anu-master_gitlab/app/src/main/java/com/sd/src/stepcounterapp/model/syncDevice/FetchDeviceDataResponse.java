package com.sd.src.stepcounterapp.model.syncDevice;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class FetchDeviceDataResponse{


//    {"status":200,"message":"Success","data":{"todayToken":0,"totalUserToken":1094,"totalUserDistance":0.4,"companyRank":6,"lastUpdated":"2019-08-07","activity":[{"date":"2019-08-08","steps":120,"distance":0,"token":0}]}}
  
  @SerializedName("data")
  @Expose
  private Data data;
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setData(Data data){
   this.data=data;
  }
  public Data getData(){
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