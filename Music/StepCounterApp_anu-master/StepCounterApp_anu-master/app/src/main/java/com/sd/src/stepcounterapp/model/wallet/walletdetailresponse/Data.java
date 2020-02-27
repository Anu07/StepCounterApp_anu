
package com.sd.src.stepcounterapp.model.wallet.walletdetailresponse;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Data implements Parcelable {

    private int days;
    private List<Purchased> purchased;
    private List<Object> redeemed;
    private int steps;
    private int totalEarnings;
    private int totalGenerated;
    private String updatedAt;
    private List<Wishlist> wishlist;


    public List<Purchased> getPurchased() {
        return purchased;
    }

    public void setPurchased(List<Purchased> purchased) {
        this.purchased = purchased;
    }

    public List<Object> getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(List<Object> redeemed) {
        this.redeemed = redeemed;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Wishlist> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<Wishlist> wishlist) {
        this.wishlist = wishlist;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.days);
        dest.writeTypedList(this.purchased);
        dest.writeList(this.redeemed);
        dest.writeInt(this.steps);
        dest.writeInt(this.totalEarnings);
        dest.writeInt(this.totalGenerated);
        dest.writeString(this.updatedAt);
        dest.writeTypedList(this.wishlist);
    }

    public Data() {
    }

    protected Data(Parcel in) {
        this.days = in.readInt();
        this.purchased = in.createTypedArrayList(Purchased.CREATOR);
        this.redeemed = new ArrayList<Object>();
        in.readList(this.redeemed, Object.class.getClassLoader());
        this.steps = in.readInt();
        this.totalEarnings = in.readInt();
        this.totalGenerated = in.readInt();
        this.updatedAt = in.readString();
        this.wishlist = in.createTypedArrayList(Wishlist.CREATOR);
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(int totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public int getTotalGenerated() {
        return totalGenerated;
    }

    public void setTotalGenerated(int totalGenerated) {
        this.totalGenerated = totalGenerated;
    }

    public static Creator<Data> getCREATOR() {
        return CREATOR;
    }
}
