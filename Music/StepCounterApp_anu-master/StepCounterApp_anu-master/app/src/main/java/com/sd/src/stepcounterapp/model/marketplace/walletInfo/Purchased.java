
package com.sd.src.stepcounterapp.model.marketplace.walletInfo;

import android.os.Parcel;
import android.os.Parcelable;
import com.sd.src.stepcounterapp.model.wallet.walletdetailresponse.VendorId;

@SuppressWarnings("unused")
public class Purchased implements Parcelable {

    private String _id;
    private String description;
    private String image;
    private String invoiceNo;
    private String name;
    private String purchasedAt;
    private long quantity;
    private String rewardId;
    private String shortDesc;
    private long token;


    private VendorId vendorId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(String purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
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

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeString(this.invoiceNo);
        dest.writeString(this.name);
        dest.writeString(this.purchasedAt);
        dest.writeLong(this.quantity);
        dest.writeString(this.rewardId);
        dest.writeString(this.shortDesc);
        dest.writeLong(this.token);
        dest.writeParcelable(this.vendorId, flags);
    }

    public Purchased() {
    }

    protected Purchased(Parcel in) {
        this._id = in.readString();
        this.description = in.readString();
        this.image = in.readString();
        this.invoiceNo = in.readString();
        this.name = in.readString();
        this.purchasedAt = in.readString();
        this.quantity = in.readLong();
        this.rewardId = in.readString();
        this.shortDesc = in.readString();
        this.token = in.readLong();
        this.vendorId = in.readParcelable(VendorId.class.getClassLoader());
    }

    public static final Parcelable.Creator<Purchased> CREATOR = new Parcelable.Creator<Purchased>() {
        @Override
        public Purchased createFromParcel(Parcel source) {
            return new Purchased(source);
        }

        @Override
        public Purchased[] newArray(int size) {
            return new Purchased[size];
        }
    };
}
