package com.sd.src.stepcounterapp.model.image;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Data{
  @SerializedName("fullPath")
  @Expose
  private String fullPath;
  @SerializedName("fileName")
  @Expose
  private String fileName;
  @SerializedName("fileType")
  @Expose
  private String fileType;
  public void setFullPath(String fullPath){
   this.fullPath=fullPath;
  }
  public String getFullPath(){
   return fullPath;
  }
  public void setFileName(String fileName){
   this.fileName=fileName;
  }
  public String getFileName(){
   return fileName;
  }
  public void setFileType(String fileType){
   this.fileType=fileType;
  }
  public String getFileType(){
   return fileType;
  }
}