package com.sd.src.stepcounterapp.model.transactionhistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// Created by Pardeep on 22/8/19.
// Copyright (c) 2019 (c) For Long Life Corp - All Rights Reserved.
public class TransactionHistoryModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<TransactionData> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TransactionData> getData() {
        return data;
    }

    public void setData(List<TransactionData> data) {
        this.data = data;
    }
}
