package com.sd.src.stepcounterapp.model.challenge;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class ChallengeResponse{
  @SerializedName("trending")
  @Expose
  private List<Trending> trending;
  @SerializedName("featured")
  @Expose
  private List<Featured> featured;
  @SerializedName("ongoing")
  @Expose
  private List<Ongoing> ongoing;
  @SerializedName("data")
  @Expose
  private List<Data> data;
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setTrending(List<Trending> trending){
   this.trending=trending;
  }
  public List<Trending> getTrending(){
   return trending;
  }
  public void setFeatured(List<Featured> featured){
   this.featured=featured;
  }
  public List<Featured> getFeatured(){
   return featured;
  }
  public void setOngoing(List<Ongoing> ongoing){
   this.ongoing=ongoing;
  }
  public List<Ongoing> getOngoing(){
   return ongoing;
  }
  public void setData(List<Data> data){
   this.data=data;
  }
  public List<Data> getData(){
   return data;
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