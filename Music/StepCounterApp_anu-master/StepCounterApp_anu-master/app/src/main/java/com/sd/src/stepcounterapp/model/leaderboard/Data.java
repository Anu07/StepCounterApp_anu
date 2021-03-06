package com.sd.src.stepcounterapp.model.leaderboard;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Data{
  @SerializedName("date")
  @Expose
  private String date;
  @SerializedName("image")
  @Expose
  private String image;
  @SerializedName("distance")
  @Expose
  private Float distance;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("userId")
  @Expose
  private String userId;
  @SerializedName("steps")
  @Expose
  private Integer steps;
  public void setDate(String date){
   this.date=date;
  }
  public String getDate(){
   return date;
  }
  public void setImage(String image){
   this.image=image;
  }
  public String getImage(){
   return image;
  }
  public void setDistance(Float distance){
   this.distance=distance;
  }
  public Float getDistance(){
   return distance;
  }
  public void setName(String name){
   this.name=name;
  }
  public String getName(){
   return name;
  }
  public void setUserId(String userId){
   this.userId=userId;
  }
  public String getUserId(){
   return userId;
  }
  public void setSteps(Integer steps){
   this.steps=steps;
  }
  public Integer getSteps(){
   return steps;
  }
}