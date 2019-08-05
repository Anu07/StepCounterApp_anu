package com.sd.src.stepcounterapp.model.syncDevice;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class SyncRequest {
  @SerializedName("dateTime")
  @Expose
  private String dateTime;
  @SerializedName("distance")
  @Expose
  private Integer distance;
  @SerializedName("step_count")
  @Expose
  private Integer step_count;
  @SerializedName("userId")
  @Expose
  private String userId;
  @SerializedName("deviceId")
  @Expose
  private String deviceId;
    
    public SyncRequest(String dateTime, Integer distance, Integer step_count, String userId, String deviceId) {
        this.dateTime = dateTime;
        this.distance = distance;
        this.step_count = step_count;
        this.userId = userId;
        this.deviceId = deviceId;
    }
    
    public void setDateTime(String dateTime){
   this.dateTime=dateTime;
  }
  public String getDateTime(){
   return dateTime;
  }
  public void setDistance(Integer distance){
   this.distance=distance;
  }
  public Integer getDistance(){
   return distance;
  }
  public void setStep_count(Integer step_count){
   this.step_count=step_count;
  }
  public Integer getStep_count(){
   return step_count;
  }
  public void setUserId(String userId){
   this.userId=userId;
  }
  public String getUserId(){
   return userId;
  }
  public void setDeviceId(String deviceId){
   this.deviceId=deviceId;
  }
  public String getDeviceId(){
   return deviceId;
  }
}