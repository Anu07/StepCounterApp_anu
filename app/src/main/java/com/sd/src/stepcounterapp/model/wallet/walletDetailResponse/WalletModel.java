
package com.sd.src.stepcounterapp.model.wallet.walletDetailResponse;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class WalletModel implements Parcelable {

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private Data data;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
        dest.writeString(this.message);
        dest.writeInt(this.status);
    }

    public WalletModel() {
    }

    protected WalletModel(Parcel in) {
        this.data = in.readParcelable(Data.class.getClassLoader());
        this.message = in.readString();
        this.status = in.readInt();
    }

    public static final Parcelable.Creator<WalletModel> CREATOR = new Parcelable.Creator<WalletModel>() {
        @Override
        public WalletModel createFromParcel(Parcel source) {
            return new WalletModel(source);
        }

        @Override
        public WalletModel[] newArray(int size) {
            return new WalletModel[size];
        }
    };
}
