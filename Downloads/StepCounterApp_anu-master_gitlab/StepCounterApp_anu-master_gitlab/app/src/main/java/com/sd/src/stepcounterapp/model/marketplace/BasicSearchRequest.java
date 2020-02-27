package com.sd.src.stepcounterapp.model.marketplace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class BasicSearchRequest {
	@SerializedName("userId")
	@Expose
	private String userId;
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	@SerializedName("keyword")
	@Expose
	private String keyword;
	
	public BasicSearchRequest(String userId, String keyword) {
		this.userId = userId;
		this.keyword = keyword;
	}
	
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
}