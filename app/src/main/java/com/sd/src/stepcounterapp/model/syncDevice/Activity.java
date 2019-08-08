package com.sd.src.stepcounterapp.model.syncDevice;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Activity{
  @SerializedName("sportDate")
  @Expose
  private String sportDate;
  @SerializedName("distance")
  @Expose
  private Double distance;
  @SerializedName("sportTime")
  @Expose
  private Integer sportTime;
  @SerializedName("stepCount")
  @Expose
  private Integer stepCount;
  @SerializedName("burnCalories")
  @Expose
  private Integer burnCalories;
    
    public Activity(String sportDate, Double distance, Integer sportTime, Integer stepCount, Integer burnCalories) {
        this.sportDate = sportDate;
        this.distance = distance;
        this.sportTime = sportTime;
        this.stepCount = stepCount;
        this.burnCalories = burnCalories;
    }
    
    public void setSportDate(String sportDate){
   this.sportDate=sportDate;
  }
  public String getSportDate(){
   return sportDate;
  }
  public void setDistance(Double distance){
   this.distance=distance;
  }
  public Double getDistance(){
   return distance;
  }
  public void setSportTime(Integer sportTime){
   this.sportTime=sportTime;
  }
  public Integer getSportTime(){
   return sportTime;
  }
  public void setStepCount(Integer stepCount){
   this.stepCount=stepCount;
  }
  public Integer getStepCount(){
   return stepCount;
  }
  public void setBurnCalories(Integer burnCalories){
   this.burnCalories=burnCalories;
  }
  public Integer getBurnCalories(){
   return burnCalories;
  }
}