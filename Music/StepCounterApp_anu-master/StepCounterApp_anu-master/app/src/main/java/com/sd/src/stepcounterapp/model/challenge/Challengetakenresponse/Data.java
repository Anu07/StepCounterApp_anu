package com.sd.src.stepcounterapp.model.challenge.Challengetakenresponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Data{
  @SerializedName("stopAt")
  @Expose
  private Object stopAt;
  @SerializedName("challengeId")
  @Expose
  private String challengeId;
  @SerializedName("completedAt")
  @Expose
  private Object completedAt;
  @SerializedName("joinedAt")
  @Expose
  private String joinedAt;
  @SerializedName("__v")
  @Expose
  private Integer __v;
  @SerializedName("challenge")
  @Expose
  private Challenge challenge;
  @SerializedName("_id")
  @Expose
  private String _id;
  @SerializedName("is_stop")
  @Expose
  private Boolean is_stop;
  @SerializedName("invitationId")
  @Expose
  private Object invitationId;
  @SerializedName("userId")
  @Expose
  private String userId;
  @SerializedName("is_completed")
  @Expose
  private Boolean is_completed;
  public void setStopAt(Object stopAt){
   this.stopAt=stopAt;
  }
  public Object getStopAt(){
   return stopAt;
  }
  public void setChallengeId(String challengeId){
   this.challengeId=challengeId;
  }
  public String getChallengeId(){
   return challengeId;
  }
  public void setCompletedAt(Object completedAt){
   this.completedAt=completedAt;
  }
  public Object getCompletedAt(){
   return completedAt;
  }
  public void setJoinedAt(String joinedAt){
   this.joinedAt=joinedAt;
  }
  public String getJoinedAt(){
   return joinedAt;
  }
  public void set__v(Integer __v){
   this.__v=__v;
  }
  public Integer get__v(){
   return __v;
  }
  public void setChallenge(Challenge challenge){
   this.challenge=challenge;
  }
  public Challenge getChallenge(){
   return challenge;
  }
  public void set_id(String _id){
   this._id=_id;
  }
  public String get_id(){
   return _id;
  }
  public void setIs_stop(Boolean is_stop){
   this.is_stop=is_stop;
  }
  public Boolean getIs_stop(){
   return is_stop;
  }
  public void setInvitationId(Object invitationId){
   this.invitationId=invitationId;
  }
  public Object getInvitationId(){
   return invitationId;
  }
  public void setUserId(String userId){
   this.userId=userId;
  }
  public String getUserId(){
   return userId;
  }
  public void setIs_completed(Boolean is_completed){
   this.is_completed=is_completed;
  }
  public Boolean getIs_completed(){
   return is_completed;
  }
}