package com.sd.src.stepcounterapp.model.leaderboard;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class LeaderBoardResponse{
  @SerializedName("general")
  @Expose
  private List<General> general;
  @SerializedName("challenge")
  @Expose
  private List<Challenge> challenge;
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setGeneral(List<General> general){
   this.general=general;
  }
  public List<General> getGeneral(){
   return general;
  }
  public void setChallenge(List<Challenge> challenge){
   this.challenge=challenge;
  }
  public List<Challenge> getChallenge(){
   return challenge;
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