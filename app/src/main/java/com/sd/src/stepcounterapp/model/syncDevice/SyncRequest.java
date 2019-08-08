package com.sd.src.stepcounterapp.model.syncDevice;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class SyncRequest{
  @SerializedName("activity")
  @Expose
  private ArrayList<Activity> activity;
  @SerializedName("userId")
  @Expose
  private String userId;
  @SerializedName("deviceId")
  @Expose
  private String deviceId;
    
    public SyncRequest(ArrayList<Activity> activity, String userId, String deviceId) {
        this.activity = activity;
        this.userId = userId;
        this.deviceId = deviceId;
    }
    
    public void setActivity(ArrayList<Activity> activity){
   this.activity=activity;
  }
  public List<Activity> getActivity(){
   return activity;
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