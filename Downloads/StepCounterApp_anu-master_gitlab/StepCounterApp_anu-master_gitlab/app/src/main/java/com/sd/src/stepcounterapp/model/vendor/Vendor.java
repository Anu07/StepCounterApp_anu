
package com.sd.src.stepcounterapp.model.vendor;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Vendor implements Parcelable {

    @SerializedName("address")
    private String mAddress;
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
    @SerializedName("_id")
    private String m_id;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
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

    public String get_id() {
        return m_id;
    }

    public void set_id(String _id) {
        m_id = _id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAddress);
        dest.writeString(this.mContactPerson);
        dest.writeString(this.mDescription);
        dest.writeString(this.mEmail);
        dest.writeString(this.mImage);
        dest.writeValue(this.mMobile);
        dest.writeString(this.m_id);
    }

    public Vendor() {
    }

    protected Vendor(Parcel in) {
        this.mAddress = in.readString();
        this.mContactPerson = in.readString();
        this.mDescription = in.readString();
        this.mEmail = in.readString();
        this.mImage = in.readString();
        this.mMobile = (Long) in.readValue(Long.class.getClassLoader());
        this.m_id = in.readString();
    }

    public static final Parcelable.Creator<Vendor> CREATOR = new Parcelable.Creator<Vendor>() {
        @Override
        public Vendor createFromParcel(Parcel source) {
            return new Vendor(source);
        }

        @Override
        public Vendor[] newArray(int size) {
            return new Vendor[size];
        }
    };
}
