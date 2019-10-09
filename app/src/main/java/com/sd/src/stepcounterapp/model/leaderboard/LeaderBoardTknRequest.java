package com.sd.src.stepcounterapp.model.leaderboard;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 * */
public class LeaderBoardTknRequest {

  @SerializedName("userId")
  @Expose
  private String userId;

    public LeaderBoardTknRequest( String userId, String challengeId) {
        this.userId = userId;
        this.challengeId = challengeId;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    @SerializedName("challengeId")
    @Expose
    private String challengeId;

    
  public void setUserId(String userId){
   this.userId=userId;
  }
  public String getUserId(){
   return userId;
  }
}