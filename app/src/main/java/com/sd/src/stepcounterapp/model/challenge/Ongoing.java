package com.sd.src.stepcounterapp.model.challenge;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Ongoing{
  @SerializedName("image")
  @Expose
  private String image;
  @SerializedName("is_active")
  @Expose
  private Boolean is_active;
  @SerializedName("addedBy")
  @Expose
  private String addedBy;
  @SerializedName("description")
  @Expose
  private String description;
  @SerializedName("steps")
  @Expose
  private Integer steps;
  @SerializedName("points")
  @Expose
  private Integer points;
    
    public String getEndDateTime() {
        return endDateTime;
    }
    
    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }
    
    @SerializedName("endDateTime")
    @Expose
    private String endDateTime;
  @SerializedName("bonusPoint3")
  @Expose
  private Integer bonusPoint3;
  @SerializedName("bonusPoint2")
  @Expose
  private Integer bonusPoint2;
  @SerializedName("bonusPoint1")
  @Expose
  private Integer bonusPoint1;
  @SerializedName("is_deleted")
  @Expose
  private Boolean is_deleted;
  @SerializedName("startDateTime")
  @Expose
  private String startDateTime;
  @SerializedName("joinedIn")
  @Expose
  private Integer joinedIn;
  @SerializedName("__v")
  @Expose
  private Integer __v;
  @SerializedName("adminId")
  @Expose
  private String adminId;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("shortDesc")
  @Expose
  private String shortDesc;
  @SerializedName("_id")
  @Expose
  private String _id;
  @SerializedName("department")
  @Expose
  private String department;
  @SerializedName("is_featured")
  @Expose
  private Boolean is_featured;
  @SerializedName("updatedAt")
  @Expose
  private String updatedAt;
  public void setImage(String image){
   this.image=image;
  }
  public String getImage(){
   return image;
  }
  public void setIs_active(Boolean is_active){
   this.is_active=is_active;
  }
  public Boolean getIs_active(){
   return is_active;
  }
  public void setAddedBy(String addedBy){
   this.addedBy=addedBy;
  }
  public String getAddedBy(){
   return addedBy;
  }
  public void setDescription(String description){
   this.description=description;
  }
  public String getDescription(){
   return description;
  }
  public void setSteps(Integer steps){
   this.steps=steps;
  }
  public Integer getSteps(){
   return steps;
  }
  public void setPoints(Integer points){
   this.points=points;
  }
  public Integer getPoints(){
   return points;
  }
  public void setBonusPoint3(Integer bonusPoint3){
   this.bonusPoint3=bonusPoint3;
  }
  public Integer getBonusPoint3(){
   return bonusPoint3;
  }
  public void setBonusPoint2(Integer bonusPoint2){
   this.bonusPoint2=bonusPoint2;
  }
  public Integer getBonusPoint2(){
   return bonusPoint2;
  }
  public void setBonusPoint1(Integer bonusPoint1){
   this.bonusPoint1=bonusPoint1;
  }
  public Integer getBonusPoint1(){
   return bonusPoint1;
  }
  public void setIs_deleted(Boolean is_deleted){
   this.is_deleted=is_deleted;
  }
  public Boolean getIs_deleted(){
   return is_deleted;
  }
  public void setStartDateTime(String startDateTime){
   this.startDateTime=startDateTime;
  }
  public String getStartDateTime(){
   return startDateTime;
  }
  public void setJoinedIn(Integer joinedIn){
   this.joinedIn=joinedIn;
  }
  public Integer getJoinedIn(){
   return joinedIn;
  }
  public void set__v(Integer __v){
   this.__v=__v;
  }
  public Integer get__v(){
   return __v;
  }
  public void setAdminId(String adminId){
   this.adminId=adminId;
  }
  public String getAdminId(){
   return adminId;
  }
  public void setName(String name){
   this.name=name;
  }
  public String getName(){
   return name;
  }
  public void setShortDesc(String shortDesc){
   this.shortDesc=shortDesc;
  }
  public String getShortDesc(){
   return shortDesc;
  }
  public void set_id(String _id){
   this._id=_id;
  }
  public String get_id(){
   return _id;
  }
  public void setDepartment(String department){
   this.department=department;
  }
  public String getDepartment(){
   return department;
  }
  public void setIs_featured(Boolean is_featured){
   this.is_featured=is_featured;
  }
  public Boolean getIs_featured(){
   return is_featured;
  }
  public void setUpdatedAt(String updatedAt){
   this.updatedAt=updatedAt;
  }
  public String getUpdatedAt(){
   return updatedAt;
  }
}