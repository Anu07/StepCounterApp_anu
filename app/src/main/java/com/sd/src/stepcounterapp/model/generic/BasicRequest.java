package com.sd.src.stepcounterapp.model.generic;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class BasicRequest{
  @SerializedName("userId")
  @Expose
  private String userId;
    
    public BasicRequest(String userId, String page) {
        this.userId = userId;
        this.page = page;
    }
    public BasicRequest(String userId) {
        this.userId = userId;
    }
    public String getPage() {
        return page;
    }
    
    public void setPage(String page) {
        this.page = page;
    }
    
    @SerializedName("page")
    @Expose
    private String page;
  
   
    
    public void setUserId(String userId){
   this.userId=userId;
  }
  public String getUserId(){
   return userId;
  }
}