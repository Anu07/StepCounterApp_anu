package com.sd.src.stepcounterapp.model.syncDevice;
import android.graphics.drawable.Drawable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Awesome Pojo Generator
 * */
public class SyncDeviceSelectionArray{
  @SerializedName("image")
  @Expose
  private Drawable image;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("selected")
  @Expose
  private Boolean selected;
    
    
    public SyncDeviceSelectionArray(Drawable image, String name, Boolean selected) {
        this.image = image;
        this.name = name;
        this.selected = selected;
    }
    
    
    public void setImage(Drawable image){
   this.image=image;
  }
  public Drawable getImage(){
   return image;
  }
  public void setName(String name){
   this.name=name;
  }
  public String getName(){
   return name;
  }
  public void setSelected(Boolean selected){
   this.selected=selected;
  }
  public Boolean getSelected(){
   return selected;
  }
}