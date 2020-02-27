package com.sd.src.stepcounterapp.model.dashboard;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Activity{
  @SerializedName("date")
  @Expose
  private String date;
  @SerializedName("distance")
  @Expose
  private Integer distance;
  @SerializedName("steps")
  @Expose
  private Integer steps;
  public void setDate(String date){
   this.date=date;
  }
  public String getDate(){
   return date;
  }
  public void setDistance(Integer distance){
   this.distance=distance;
  }
  public Integer getDistance(){
   return distance;
  }
  public void setSteps(Integer steps){
   this.steps=steps;
  }
  public Integer getSteps(){
   return steps;
  }
}