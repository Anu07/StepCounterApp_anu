
package com.sd.src.stepcounterapp.model.vendor;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Product {

    @SerializedName("description")
    private String mDescription;
    @SerializedName("image")
    private String mImage;
    @SerializedName("invoiceNo")
    private Object mInvoiceNo;
    @SerializedName("name")
    private String mName;
    @SerializedName("quantity")
    private Long mQuantity;
    @SerializedName("rewardId")
    private String mRewardId;
    @SerializedName("shortDesc")
    private String mShortDesc;
    @SerializedName("token")
    private Long mToken;
    @SerializedName("vendorId")
    private VendorId mVendorId;
    @SerializedName("_id")
    private String m_id;

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public Object getInvoiceNo() {
        return mInvoiceNo;
    }

    public void setInvoiceNo(Object invoiceNo) {
        mInvoiceNo = invoiceNo;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Long getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Long quantity) {
        mQuantity = quantity;
    }

    public String getRewardId() {
        return mRewardId;
    }

    public void setRewardId(String rewardId) {
        mRewardId = rewardId;
    }

    public String getShortDesc() {
        return mShortDesc;
    }

    public void setShortDesc(String shortDesc) {
        mShortDesc = shortDesc;
    }

    public Long getToken() {
        return mToken;
    }

    public void setToken(Long token) {
        mToken = token;
    }

    public VendorId getVendorId() {
        return mVendorId;
    }

    public void setVendorId(VendorId vendorId) {
        mVendorId = vendorId;
    }

    public String get_id() {
        return m_id;
    }

    public void set_id(String _id) {
        m_id = _id;
    }

}
