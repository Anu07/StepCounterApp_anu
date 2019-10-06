package com.sd.src.stepcounterapp.model.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class BasicRequest {
	@SerializedName("userId")
	@Expose
	private String userId;

	public BasicRequest(String userId, int page) {
		this.userId = userId;
		this.page = page;
	}


	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@SerializedName("page")
	@Expose
	private int page;
	
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
}