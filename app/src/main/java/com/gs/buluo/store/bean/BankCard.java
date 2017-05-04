package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2016/11/23.
 */
public class BankCard implements Parcelable {
    public String id;
    public String ownerId;
    public String userName;
    public String bankAddress;
    public String bankName;
    public String bankCardNum;
    public String phone;
    public String bankCode;

    public BankCard() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.ownerId);
        dest.writeString(this.userName);
        dest.writeString(this.bankAddress);
        dest.writeString(this.bankName);
        dest.writeString(this.bankCardNum);
        dest.writeString(this.phone);
        dest.writeString(this.bankCode);
    }

    protected BankCard(Parcel in) {
        this.id = in.readString();
        this.ownerId = in.readString();
        this.userName = in.readString();
        this.bankAddress = in.readString();
        this.bankName = in.readString();
        this.bankCardNum = in.readString();
        this.phone = in.readString();
        this.bankCode = in.readString();
    }

    public static final Creator<BankCard> CREATOR = new Creator<BankCard>() {
        @Override
        public BankCard createFromParcel(Parcel source) {
            return new BankCard(source);
        }

        @Override
        public BankCard[] newArray(int size) {
            return new BankCard[size];
        }
    };
}
