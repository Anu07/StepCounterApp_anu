package com.sd.src.stepcounterapp.model.syncDevice;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class Data{

//    {"status":200,"message":"Success","data":{"todayToken":0,"totalUserToken":1094,"totalUserDistance":0.4,"companyRank":6,"lastUpdated":"2019-08-07","activity":[{"date":"2019-08-08","steps":120,"distance":0,"token":0}]}}
  
  
  
  @SerializedName("lastUpdated")
  @Expose
  private String lastUpdated;
  @SerializedName("activity")
  @Expose
  private List<Activity> activity;
  @SerializedName("companyRank")
  @Expose
  private Integer companyRank;
  @SerializedName("totalUserToken")
  @Expose
  private Integer totalUserToken;
  @SerializedName("todayToken")
  @Expose
  private Integer todayToken;
  @SerializedName("totalUserDistance")
  @Expose
  private Integer totalUserDistance;
  public void setLastUpdated(String lastUpdated){
   this.lastUpdated=lastUpdated;
  }
  public String getLastUpdated(){
   return lastUpdated;
  }
  public void setActivity(List<Activity> activity){
   this.activity=activity;
  }
  public List<Activity> getActivity(){
   return activity;
  }
  public void setCompanyRank(Integer companyRank){
   this.companyRank=companyRank;
  }
  public Integer getCompanyRank(){
   return companyRank;
  }
  public void setTotalUserToken(Integer totalUserToken){
   this.totalUserToken=totalUserToken;
  }
  public Integer getTotalUserToken(){
   return totalUserToken;
  }
  public void setTodayToken(Integer todayToken){
   this.todayToken=todayToken;
  }
  public Integer getTodayToken(){
   return todayToken;
  }
  public void setTotalUserDistance(Integer totalUserDistance){
   this.totalUserDistance=totalUserDistance;
  }
  public Integer getTotalUserDistance(){
   return totalUserDistance;
  }
}