package com.sd.src.stepcounterapp.model.profile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

/**
 * Awesome Pojo Generator
 * */
public class UpdateProfileRequest{
  @SerializedName("firstName")
  @Expose
  private String firstName;
  @SerializedName("lastName")
  @Expose
  private String lastName;
  @SerializedName("weightType")
  @Expose
  private String weightType;
  @SerializedName("gender")
  @Expose
  private String gender;
  @SerializedName("heightType")
  @Expose
  private String heightType;
  @SerializedName("dob")
  @Expose
  private String dob;
  @SerializedName("mobile")
  @Expose
  private Integer mobile;
  @SerializedName("weight")
  @Expose
  private Float weight;
  @SerializedName("userId")
  @Expose
  private String userId;
  @SerializedName("height")
  @Expose
  private Float height;
  @SerializedName("bmi")
  @Expose
  private Double bmi;
    
    public UpdateProfileRequest(String firstName, String lastName,
                                String weightType, String gender, String heightType,
                                String dob, Integer mobile, Float weight, String userId,
                                Float height, Double bmi) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.weightType = weightType;
        this.gender = gender;
        this.heightType = heightType;
        this.dob = dob;
        this.mobile = mobile;
        this.weight = weight;
        this.userId = userId;
        this.height = height;
        this.bmi = bmi;
    }
    
    public void setFirstName(String firstName){
   this.firstName=firstName;
  }
  public String getFirstName(){
   return firstName;
  }
  public void setLastName(String lastName){
   this.lastName=lastName;
  }
  public String getLastName(){
   return lastName;
  }
  public void setWeightType(String weightType){
   this.weightType=weightType;
  }
  public String getWeightType(){
   return weightType;
  }
  public void setGender(String gender){
   this.gender=gender;
  }
  public String getGender(){
   return gender;
  }
  public void setHeightType(String heightType){
   this.heightType=heightType;
  }
  public String getHeightType(){
   return heightType;
  }
  public void setDob(String dob){
   this.dob=dob;
  }
  public String getDob(){
   return dob;
  }
  public void setMobile(Integer mobile){
   this.mobile=mobile;
  }
  public Integer getMobile(){
   return mobile;
  }
  public void setWeight(Float weight){
   this.weight=weight;
  }
  public Float getWeight(){
   return weight;
  }
  public void setUserId(String userId){
   this.userId=userId;
  }
  public String getUserId(){
   return userId;
  }
  public void setHeight(Float height){
   this.height=height;
  }
  public Float getHeight(){
   return height;
  }
  public void setBmi(Double bmi){
   this.bmi=bmi;
  }
  public Double getBmi(){
   return bmi;
  }
}