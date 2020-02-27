
package com.sd.src.stepcounterapp.model.vendor;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VendorId {

    @SerializedName("companyName")
    private String mCompanyName;
    @SerializedName("contactPerson")
    private String mContactPerson;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("image")
    private String mImage;
    @SerializedName("mobile")
    private Long mMobile;
    @SerializedName("websiteUrl")
    private String mWebsiteUrl;
    @SerializedName("_id")
    private String m_id;

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String companyName) {
        mCompanyName = companyName;
    }

    public String getContactPerson() {
        return mContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        mContactPerson = contactPerson;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public Long getMobile() {
        return mMobile;
    }

    public void setMobile(Long mobile) {
        mMobile = mobile;
    }

    public String getWebsiteUrl() {
        return mWebsiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        mWebsiteUrl = websiteUrl;
    }

    public String get_id() {
        return m_id;
    }

    public void set_id(String _id) {
        m_id = _id;
    }

}
