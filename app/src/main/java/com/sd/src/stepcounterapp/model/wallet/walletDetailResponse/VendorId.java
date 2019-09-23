
package com.sd.src.stepcounterapp.model.wallet.walletDetailResponse;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class VendorId implements Parcelable {

    private String _id;
    private String companyName;
    private String contactPerson;
    private String description;
    private String email;
    private String image;
    private long mobile;
    private String websiteUrl;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.companyName);
        dest.writeString(this.contactPerson);
        dest.writeString(this.description);
        dest.writeString(this.email);
        dest.writeString(this.image);
        dest.writeLong(this.mobile);
        dest.writeString(this.websiteUrl);
    }

    public VendorId() {
    }

    protected VendorId(Parcel in) {
        this._id = in.readString();
        this.companyName = in.readString();
        this.contactPerson = in.readString();
        this.description = in.readString();
        this.email = in.readString();
        this.image = in.readString();
        this.mobile = in.readLong();
        this.websiteUrl = in.readString();
    }

    public static final Parcelable.Creator<VendorId> CREATOR = new Parcelable.Creator<VendorId>() {
        @Override
        public VendorId createFromParcel(Parcel source) {
            return new VendorId(source);
        }

        @Override
        public VendorId[] newArray(int size) {
            return new VendorId[size];
        }
    };
}
