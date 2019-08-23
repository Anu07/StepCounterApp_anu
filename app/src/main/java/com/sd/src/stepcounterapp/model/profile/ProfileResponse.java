package com.sd.src.stepcounterapp.model.profile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * {"status":200,"message":"Success","data":{"_id":"5d39452f2c35df2cbe611a68","email":"anub@yopmail.com","username":"anub","firstName":"anu","lastName":"bhalla","image":"/public/uploads/profile_image/dummy.png","dob":"25/07/2001","bmi":24.629324,"gender":"female","height":68,"heightType":"Cms","weight":74,"weightType":"Kgs","mobile":9898989898,"company":"Dhiman Traders","department":"Marketing"}}
 * */
public class ProfileResponse{
  @SerializedName("data")
  @Expose
  private Data data;
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setData(Data data){
   this.data=data;
  }
  public Data getData(){
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