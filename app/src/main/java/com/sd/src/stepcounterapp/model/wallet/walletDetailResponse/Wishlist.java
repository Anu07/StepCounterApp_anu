
package com.sd.src.stepcounterapp.model.wallet.walletDetailResponse;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class Wishlist implements Parcelable {

    private int _V;
    private String _id;
    private String adminId;
    private String createdAt;
    private String description;
    private String image;
    private Boolean isActive;
    private Boolean isDeleted;
    private String name;
    private int quantity;
    private String rewardId;
    private String shortDesc;
    private int token;
    private int totalSales;
    private String updatedAt;
    private VendorId vendorId;
    private int wishlistCount;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }



    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    public int get_V() {
        return _V;
    }

    public void set_V(int _V) {
        this._V = _V;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public int getWishlistCount() {
        return wishlistCount;
    }

    public void setWishlistCount(int wishlistCount) {
        this.wishlistCount = wishlistCount;
    }


    public VendorId getVendorId() {
        return vendorId;
    }

    public void setVendorId(VendorId vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._V);
        dest.writeString(this._id);
        dest.writeString(this.adminId);
        dest.writeString(this.createdAt);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeValue(this.isActive);
        dest.writeValue(this.isDeleted);
        dest.writeString(this.name);
        dest.writeInt(this.quantity);
        dest.writeString(this.rewardId);
        dest.writeString(this.shortDesc);
        dest.writeInt(this.token);
        dest.writeInt(this.totalSales);
        dest.writeString(this.updatedAt);
        dest.writeParcelable(this.vendorId, flags);
        dest.writeInt(this.wishlistCount);
    }

    public Wishlist() {
    }

    protected Wishlist(Parcel in) {
        this._V = in.readInt();
        this._id = in.readString();
        this.adminId = in.readString();
        this.createdAt = in.readString();
        this.description = in.readString();
        this.image = in.readString();
        this.isActive = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isDeleted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.name = in.readString();
        this.quantity = in.readInt();
        this.rewardId = in.readString();
        this.shortDesc = in.readString();
        this.token = in.readInt();
        this.totalSales = in.readInt();
        this.updatedAt = in.readString();
        this.vendorId = in.readParcelable(VendorId.class.getClassLoader());
        this.wishlistCount = in.readInt();
    }

    public static final Parcelable.Creator<Wishlist> CREATOR = new Parcelable.Creator<Wishlist>() {
        @Override
        public Wishlist createFromParcel(Parcel source) {
            return new Wishlist(source);
        }

        @Override
        public Wishlist[] newArray(int size) {
            return new Wishlist[size];
        }
    };
}
