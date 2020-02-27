package com.sd.src.stepcounterapp.model.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class Data {
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("is_deleted")
    @Expose
    private Boolean is_deleted;
    @SerializedName("is_active")
    @Expose
    private Boolean is_active;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    private Boolean selectedItem= false;

    public Boolean getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Boolean selectedItem) {
        this.selectedItem = selectedItem;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setIs_deleted(Boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

}