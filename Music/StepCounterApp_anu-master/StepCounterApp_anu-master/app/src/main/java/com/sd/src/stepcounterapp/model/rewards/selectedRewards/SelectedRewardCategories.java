
package com.sd.src.stepcounterapp.model.rewards.selectedRewards;

import com.sd.src.stepcounterapp.model.rewards.Data;

import java.util.List;

@SuppressWarnings("unused")
public class SelectedRewardCategories {

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getSelected() {
        return selected;
    }

    public void setSelected(List<String> selected) {
        this.selected = selected;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    private List<Data> data;
    private String message;
    private List<String> selected;
    private long status;

}
