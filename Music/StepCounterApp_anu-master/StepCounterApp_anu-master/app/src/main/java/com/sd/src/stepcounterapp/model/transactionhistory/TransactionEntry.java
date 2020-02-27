package com.sd.src.stepcounterapp.model.transactionhistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Created by Pardeep on 22/8/19.
// Copyright (c) 2019 (c) For Long Life Corp - All Rights Reserved.
public class TransactionEntry {


    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("transactionName")
    @Expose
    private String transactionName;
    @SerializedName("totalSteps")
    @Expose
    private Integer totalSteps;
    @SerializedName("currentToken")
    @Expose
    private Integer currentToken;
    @SerializedName("openingTokens")
    @Expose
    private Integer openingTokens;
    @SerializedName("currentTransaction")
    @Expose
    private String currentTransaction;
    @SerializedName("closingTokens")
    @Expose
    private String closingTokens;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Integer getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(Integer totalSteps) {
        this.totalSteps = totalSteps;
    }

    public Integer getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(Integer currentToken) {
        this.currentToken = currentToken;
    }

    public Integer getOpeningTokens() {
        return openingTokens;
    }

    public void setOpeningTokens(Integer openingTokens) {
        this.openingTokens = openingTokens;
    }

    public String getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(String currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public String getClosingTokens() {
        return closingTokens;
    }

    public void setClosingTokens(String closingTokens) {
        this.closingTokens = closingTokens;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
