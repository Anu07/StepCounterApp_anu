
package com.sd.src.stepcounterapp.model.vendor;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("product")
    private Product mProduct;
    @SerializedName("purchasedAt")
    private String mPurchasedAt;
    @SerializedName("userId")
    private String mUserId;
    @SerializedName("vendor")
    private VendorId mVendor;

    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product product) {
        mProduct = product;
    }

    public String getPurchasedAt() {
        return mPurchasedAt;
    }

    public void setPurchasedAt(String purchasedAt) {
        mPurchasedAt = purchasedAt;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }


    public VendorId getmVendor() {
        return mVendor;
    }

    public void setmVendor(VendorId mVendor) {
        this.mVendor = mVendor;
    }
}
