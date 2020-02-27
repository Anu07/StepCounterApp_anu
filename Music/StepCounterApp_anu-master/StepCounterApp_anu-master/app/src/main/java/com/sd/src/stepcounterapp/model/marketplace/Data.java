package com.sd.src.stepcounterapp.model.marketplace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data {
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("_id")
    @Expose
    public String _id;
    @SerializedName("products")
    @Expose
    public ArrayList<MarketResponse.Products> products;

    public Data(String image, String name, String _id, ArrayList<MarketResponse.Products> products) {
        this.image = image;
        this.name = name;
        this._id = _id;
        this.products = products;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
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

    public void setProducts(ArrayList<MarketResponse.Products> products) {
        this.products = products;
    }

    public ArrayList<MarketResponse.Products> getProducts() {
        return products;
    }
}

