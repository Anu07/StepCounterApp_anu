package com.sd.src.stepcounterapp.model.Deviceresponse;
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
  private Double distance;
  @SerializedName("steps")
  @Expose
  private Integer steps;
  @SerializedName("token")
  @Expose
  private Integer token;
  public void setDate(String date){
   this.date=date;
  }
  public String getDate(){
   return date;
  }
  public void setDistance(Double distance){
   this.distance=distance;
  }
  public Double getDistance(){
   return distance;
  }
  public void setSteps(Integer steps){
   this.steps=steps;
  }
  public Integer getSteps(){
   return steps;
  }
  public void setToken(Integer token){
   this.token=token;
  }
  public Integer getToken(){
   return token;
  }
}