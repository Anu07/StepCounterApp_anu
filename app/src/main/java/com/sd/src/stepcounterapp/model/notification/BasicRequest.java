package com.sd.src.stepcounterapp.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class BasicRequest {
	@SerializedName("userId")
	@Expose
	private String userId;

	public BasicRequest(String userId) {
		this.userId = userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
}