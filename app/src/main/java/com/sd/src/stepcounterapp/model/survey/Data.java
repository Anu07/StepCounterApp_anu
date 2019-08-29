package com.sd.src.stepcounterapp.model.survey;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Awesome Pojo Generator
 */
public class Data implements Parcelable {
	@SerializedName("expireOn")
	@Expose
	private String expireOn;
	@SerializedName("earningToken")
	@Expose
	private Integer earningToken;
	
	public Boolean getAttempted() {
		return attempted;
	}
	
	public void setAttempted(Boolean attempted) {
		this.attempted = attempted;
	}
	
	@SerializedName("attempted")
	@Expose
	private Boolean attempted;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("_id")
	@Expose
	private String _id;
	@SerializedName("products")
	@Expose
	private List<Products> products;
	
	public void setExpireOn(String expireOn) {
		this.expireOn = expireOn;
	}
	
	public String getExpireOn() {
		return expireOn;
	}
	
	public void setEarningToken(Integer earningToken) {
		this.earningToken = earningToken;
	}
	
	public Integer getEarningToken() {
		return earningToken;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String get_id() {
		return _id;
	}
	
	public void setProducts(List<Products> products) {
		this.products = products;
	}
	
	public List<Products> getProducts() {
		return products;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.expireOn);
		dest.writeValue(this.earningToken);
		dest.writeString(this.name);
		dest.writeString(this._id);
		dest.writeList(this.products);
	}
	
	public Data() {
	}
	
	protected Data(Parcel in) {
		this.expireOn = in.readString();
		this.earningToken = (Integer) in.readValue(Integer.class.getClassLoader());
		this.name = in.readString();
		this._id = in.readString();
		this.products = new ArrayList<Products>();
		in.readList(this.products, Products.class.getClassLoader());
	}
	
	public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
		@Override
		public Data createFromParcel(Parcel source) {
			return new Data(source);
		}
		
		@Override
		public Data[] newArray(int size) {
			return new Data[size];
		}
	};
}