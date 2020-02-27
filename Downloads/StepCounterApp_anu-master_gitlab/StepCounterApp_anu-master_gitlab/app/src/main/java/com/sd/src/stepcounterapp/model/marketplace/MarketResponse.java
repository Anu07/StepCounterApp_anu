package com.sd.src.stepcounterapp.model.marketplace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Awesome Pojo Generator
 */
public class MarketResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<Data> data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;


    private Integer pages;

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public ArrayList<Data> getData() {
        return data;
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
        public ArrayList<Products> products;

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

        public void setProducts(ArrayList<Products> products) {
            this.products = products;
        }

        public ArrayList<Products> getProducts() {
            return products;
        }
    }


    //Products class
    public class Products {
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("wishlist")
        @Expose
        private Boolean wishlist;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("shortDesc")
        @Expose
        private String shortDesc;
        @SerializedName("_id")
        @Expose
        private String _id;

        public Vendor getVendors() {
            return vendors;
        }

        public void setVendors(Vendor vendors) {
            this.vendors = vendors;
        }

        @SerializedName("vendors")
        @Expose
        private Vendor vendors;
        @SerializedName("token")
        @Expose
        private Integer token;

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        @SerializedName("quantity")
        @Expose
        private Integer quantity;

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setWishlist(Boolean wishlist) {
            this.wishlist = wishlist;
        }

        public Boolean getWishlist() {
            return wishlist;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setShortDesc(String shortDesc) {
            this.shortDesc = shortDesc;
        }

        public String getShortDesc() {
            return shortDesc;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String get_id() {
            return _id;
        }

        public void setToken(Integer token) {
            this.token = token;
        }

        public Integer getToken() {
            return token;
        }
    }

}


