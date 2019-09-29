
package com.sd.src.stepcounterapp.model.vendor;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VendorDetailResponse {

    @SerializedName("data")
    private Data mData;
    @SerializedName("message")
    private String mMessage;

    public Data getmData() {
        return mData;
    }

    public void setmData(Data mData) {
        this.mData = mData;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    @SerializedName("status")
    private int mStatus;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }



}
