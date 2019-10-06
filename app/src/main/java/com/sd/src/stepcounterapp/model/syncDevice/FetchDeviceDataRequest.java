package com.sd.src.stepcounterapp.model.syncDevice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class FetchDeviceDataRequest {
	@SerializedName("recordType")
	@Expose
	private String recordType;
	@SerializedName("userId")
	@Expose
	private String userId;

	public FetchDeviceDataRequest(String recordType, String userId, int offset) {
		this.recordType = recordType;
		this.userId = userId;
		this.offset = offset;
	}


    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @SerializedName("offset")
	@Expose
	private int offset;
	

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	
	public String getRecordType() {
		return recordType;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
}