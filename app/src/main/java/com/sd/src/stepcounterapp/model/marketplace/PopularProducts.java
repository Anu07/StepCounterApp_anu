package com.sd.src.stepcounterapp.model.marketplace;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class PopularProducts{
  @SerializedName("data")
  @Expose
  private ArrayList<Data> data;
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setData(ArrayList<Data> data){
   this.data=data;
  }
  public ArrayList<Data> getData(){
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
    
    
    public class Data{
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("rewardId")
        @Expose
        private String rewardId;
        @SerializedName("wishlist")
        @Expose
        private Boolean wishlist;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("shortDesc")
        @Expose
        private String shortDesc;
        @SerializedName("_id")
        @Expose
        private String _id;
        @SerializedName("token")
        @Expose
        private Integer token;

        public Vendor getVendor() {
            return vendor;
        }

        public void setVendor(Vendor vendor) {
            this.vendor = vendor;
        }

        @SerializedName("vendor")
        @Expose
        private Vendor vendor;
        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        @SerializedName("quantity")
        @Expose
        private Integer quantity;

        public void setImage(String image){
            this.image=image;
        }
        public String getImage(){
            return image;
        }
        public void setRewardId(String rewardId){
            this.rewardId=rewardId;
        }
        public String getRewardId(){
            return rewardId;
        }
        public void setWishlist(Boolean wishlist){
            this.wishlist=wishlist;
        }
        public Boolean getWishlist(){
            return wishlist;
        }
        public void setName(String name){
            this.name=name;
        }
        public String getName(){
            return name;
        }
        public void setDescription(String description){
            this.description=description;
        }
        public String getDescription(){
            return description;
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
        public void setToken(Integer token){
            this.token=token;
        }
        public Integer getToken(){
            return token;
        }
    }
}