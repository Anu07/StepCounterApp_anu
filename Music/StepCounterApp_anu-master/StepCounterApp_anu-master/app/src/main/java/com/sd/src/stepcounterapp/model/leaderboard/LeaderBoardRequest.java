package com.sd.src.stepcounterapp.model.leaderboard;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class LeaderBoardRequest{
  @SerializedName("leaderboardType")
  @Expose
  private String leaderboardType;
  @SerializedName("userId")
  @Expose
  private String userId;
    
    public LeaderBoardRequest(String leaderboardType, String userId) {
        this.leaderboardType = leaderboardType;
        this.userId = userId;
    }
    
    public void setLeaderboardType(String leaderboardType){
   this.leaderboardType=leaderboardType;
  }
  public String getLeaderboardType(){
   return leaderboardType;
  }
  public void setUserId(String userId){
   this.userId=userId;
  }
  public String getUserId(){
   return userId;
  }
}