package com.sd.src.stepcounterapp.model.bmi;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 * */
public class Data{
  @SerializedName("lastName")
  @Expose
  private String lastName;
  @SerializedName("image")
  @Expose
  private String image;
  @SerializedName("gender")
  @Expose
  private String gender;
  @SerializedName("basicFlag")
  @Expose
  private Boolean basicFlag;
  @SerializedName("weight")
  @Expose
  private Double weight;
  @SerializedName("firstName")
  @Expose
  private String firstName;
  @SerializedName("weightType")
  @Expose
  private String weightType;
  @SerializedName("heightType")
  @Expose
  private String heightType;
  @SerializedName("dob")
  @Expose
  private String dob;
  @SerializedName("_id")
  @Expose
  private String _id;
  @SerializedName("rewardFlag")
  @Expose
  private Boolean rewardFlag;
  @SerializedName("email")
  @Expose
  private String email;
  @SerializedName("username")
  @Expose
  private String username;
  @SerializedName("bmi")
  @Expose
  private Double bmi;
  @SerializedName("height")
  @Expose
  private Integer height;
  public void setLastName(String lastName){
   this.lastName=lastName;
  }
  public String getLastName(){
   return lastName;
  }
  public void setImage(String image){
   this.image=image;
  }
  public String getImage(){
   return image;
  }
  public void setGender(String gender){
   this.gender=gender;
  }
  public String getGender(){
   return gender;
  }
  public void setBasicFlag(Boolean basicFlag){
   this.basicFlag=basicFlag;
  }
  public Boolean getBasicFlag(){
   return basicFlag;
  }
  public void setFirstName(String firstName){
   this.firstName=firstName;
  }
  public String getFirstName(){
   return firstName;
  }
  public void setWeightType(String weightType){
   this.weightType=weightType;
  }
  public String getWeightType(){
   return weightType;
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
  public void set_id(String _id){
   this._id=_id;
  }
  public String get_id(){
   return _id;
  }
  public void setRewardFlag(Boolean rewardFlag){
   this.rewardFlag=rewardFlag;
  }
  public Boolean getRewardFlag(){
   return rewardFlag;
  }
  public void setEmail(String email){
   this.email=email;
  }
  public String getEmail(){
   return email;
  }
  public void setUsername(String username){
   this.username=username;
  }
  public String getUsername(){
   return username;
  }
  public void setHeight(Integer height){
   this.height=height;
  }
  public Integer getHeight(){
   return height;
  }
    
    public Double getWeight() {
        return weight;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public Double getBmi() {
        return bmi;
    }
    
    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }
}