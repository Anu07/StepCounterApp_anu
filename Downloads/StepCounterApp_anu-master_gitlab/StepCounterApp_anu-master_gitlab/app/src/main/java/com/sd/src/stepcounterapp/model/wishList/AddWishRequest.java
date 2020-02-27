package com.sd.src.stepcounterapp.model.wishList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class AddWishRequest {
	@SerializedName("userId")
	@Expose
	private String userId;
	@SerializedName("productId")
	@Expose
	private String productId;
	
	
	public AddWishRequest(String userId,String productId) {
		this.userId = userId;
		this.productId = productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getProductId() {
		return productId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
}