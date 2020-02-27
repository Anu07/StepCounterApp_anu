package com.sd.src.stepcounterapp.model.challenge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class WeekendChallengeStartRequestModel {
    @SerializedName("challengeId")
    @Expose
    private String challengeId;
    @SerializedName("challenge")
    @Expose
    private Data challenge;
    @SerializedName("userId")
    @Expose
    private String userId;

    public int getTargetSteps() {
        return targetSteps;
    }

    public void setTargetSteps(int targetSteps) {
        this.targetSteps = targetSteps;
    }

    public String getAverageSteps() {
        return averageSteps;
    }

    public void setAverageSteps(String averageSteps) {
        this.averageSteps = averageSteps;
    }

    @SerializedName("targetSteps")
    @Expose
    private int targetSteps;
    @SerializedName("averageSteps")
    @Expose
    private String averageSteps;

    public WeekendChallengeStartRequestModel(String challengeId, Data challenge, String userId, int targetSteps, String averageSteps) {
        this.challengeId = challengeId;
        this.challenge = challenge;
        this.userId = userId;
        this.targetSteps = targetSteps;
        this.averageSteps = averageSteps;
    }

    /*  public ChallengeStartRequestModel(String challengeId, Data challenge, String userId) {
          this.challengeId = challengeId;
          this.challenge = challenge;
          this.userId = userId;
      }
  */
    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallenge(Data challenge) {
        this.challenge = challenge;
    }

    public Data getChallenge() {
        return challenge;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}