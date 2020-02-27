package com.sd.src.stepcounterapp.model.Deviceresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Awesome Pojo Generator
 */
public class Data {
	@SerializedName("lastUpdated")
	@Expose
	private String lastUpdated;

	public String getBadgeCount() {
		return badgeCount;
	}

	public void setBadgeCount(String badgeCount) {
		this.badgeCount = badgeCount;
	}

	@SerializedName("badgeCount")
	@Expose
	private String badgeCount;


	public int getAverageSteps() {
		return averageSteps;
	}

	public void setAverageSteps(int averageSteps) {
		this.averageSteps = averageSteps;
	}

	@SerializedName("averageSteps")
	@Expose
	private int averageSteps;


	@SerializedName("activity")
	@Expose
	private List<Activity> activity;
	@SerializedName("companyRank")
	@Expose
	private Integer companyRank;
	
	public Integer getClosestToken() {
		return closestToken;
	}
	
	public void setClosestToken(Integer closestToken) {
		this.closestToken = closestToken;
	}
	
	@SerializedName("closestToken")
	@Expose
	private Integer closestToken;
	@SerializedName("totalUserToken")
	@Expose
	private Integer totalUserToken;


	public String getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(String lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	@SerializedName("lastSyncTime")
	@Expose
	private String lastSyncTime;


	public String getWearableDevice() {
		return wearableDevice;
	}

	public void setWearableDevice(String wearableDevice) {
		this.wearableDevice = wearableDevice;
	}

	@SerializedName("wearableDevice")
	@Expose
	private String wearableDevice;


	public Integer getTotalUserSteps() {
		return totalUserSteps;
	}
	
	public void setTotalUserSteps(Integer totalUserSteps) {
		this.totalUserSteps = totalUserSteps;
	}
	
	@SerializedName("totalUserSteps")
	@Expose
	private Integer totalUserSteps;
	@SerializedName("todayToken")
	@Expose
	private Integer todayToken;
	@SerializedName("totalUserDistance")
	@Expose
	private Double totalUserDistance;
	
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public String getLastUpdated() {
		return lastUpdated;
	}
	
	public void setActivity(List<Activity> activity) {
		this.activity = activity;
	}
	
	public List<Activity> getActivity() {
		return activity;
	}
	
	public void setCompanyRank(Integer companyRank) {
		this.companyRank = companyRank;
	}
	
	public Integer getCompanyRank() {
		return companyRank;
	}
	
	public void setTotalUserToken(Integer totalUserToken) {
		this.totalUserToken = totalUserToken;
	}
	
	public Integer getTotalUserToken() {
		return totalUserToken;
	}
	
	public void setTodayToken(Integer todayToken) {
		this.todayToken = todayToken;
	}
	
	public Integer getTodayToken() {
		return todayToken;
	}
	
	public void setTotalUserDistance(Double totalUserDistance) {
		this.totalUserDistance = totalUserDistance;
	}
	
	public Double getTotalUserDistance() {
		return totalUserDistance;
	}
}