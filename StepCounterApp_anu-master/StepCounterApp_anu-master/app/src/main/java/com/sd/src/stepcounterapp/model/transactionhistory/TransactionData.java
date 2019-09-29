package com.sd.src.stepcounterapp.model.transactionhistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// Created by Pardeep on 22/8/19.
// Copyright (c) 2019 (c)  - All Rights Reserved.
public class TransactionData  {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("entries")
    @Expose
    private List<TransactionEntry> entries = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TransactionEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TransactionEntry> entries) {
        this.entries = entries;
    }

}
