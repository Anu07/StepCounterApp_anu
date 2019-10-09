package com.sd.src.stepcounterapp.model.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class BasicUserRequest {
	@SerializedName("userId")
	@Expose
	private String userId;

	public BasicUserRequest(String userId) {
		this.userId = userId;
	}



	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
}