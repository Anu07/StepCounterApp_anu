
package com.sd.src.stepcounterapp.model.vendor;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class Vendor implements Parcelable {

    private String _id;
    private String address;
    private String contactPerson;
    private String description;
    private String email;
    private String image;
    private long mobile;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.address);
        dest.writeString(this.contactPerson);
        dest.writeString(this.description);
        dest.writeString(this.email);
        dest.writeString(this.image);
        dest.writeLong(this.mobile);
    }

    public Vendor() {
    }

    protected Vendor(Parcel in) {
        this._id = in.readString();
        this.address = in.readString();
        this.contactPerson = in.readString();
        this.description = in.readString();
        this.email = in.readString();
        this.image = in.readString();
        this.mobile = in.readLong();
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
