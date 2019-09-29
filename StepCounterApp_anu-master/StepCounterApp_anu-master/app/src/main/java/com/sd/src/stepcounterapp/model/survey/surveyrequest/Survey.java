package com.sd.src.stepcounterapp.model.survey.surveyrequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class Survey{
  @SerializedName("expireOn")
  @Expose
  private String expireOn;
  @SerializedName("earningToken")
  @Expose
  private Integer earningToken;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("_id")
  @Expose
  private String _id;
  @SerializedName("products")
  @Expose
  private List<Products> products;
    public Boolean getAttempted() {
        return attempted;
    }
    
    public void setAttempted(Boolean attempted) {
        this.attempted = attempted;
    }
    
    @SerializedName("attempted")
    @Expose
    private Boolean attempted;
  public void setExpireOn(String expireOn){
   this.expireOn=expireOn;
  }
  public String getExpireOn(){
   return expireOn;
  }
  public void setEarningToken(Integer earningToken){
   this.earningToken=earningToken;
  }
  public Integer getEarningToken(){
   return earningToken;
  }
  public void setName(String name){
   this.name=name;
  }
  public String getName(){
   return name;
  }
  public void set_id(String _id){
   this._id=_id;
  }
  public String get_id(){
   return _id;
  }
  public void setProducts(List<Products> products){
   this.products=products;
  }
  public List<Products> getProducts(){
   return products;
  }
}