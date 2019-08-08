package com.sd.src.stepcounterapp.model.challenge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChallengeResponse {
	@SerializedName("data")
	@Expose
	private List<Data> data;
	@SerializedName("trending")
	@Expose
	private List<Tranding> tranding;
	@SerializedName("message")
	@Expose
	private String message;
	@SerializedName("status")
	@Expose
	private Integer status;
	
	public void setData(List<Data> data) {
		this.data = data;
	}
	
	public List<Data> getData() {
		return data;
	}
	
	public void setTranding(List<Tranding> tranding) {
		this.tranding = tranding;
	}
	
	public List<Tranding> getTranding() {
		return tranding;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getStatus() {
		return status;
	}
}