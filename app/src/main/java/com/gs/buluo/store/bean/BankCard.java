package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2016/11/23.
 */
public class BankCard implements Parcelable {
    public String id;
    public String ownerId;
    public String department;
    public String userName;
    public String bankAddress;
    public String bankName;
    public String bankCardNum;
    public String phone;
    public String bankCode;
    public boolean personal = true;
    public BankCardBindTypeEnum bindType;
    public long maxWithdrawAmount;
    public long maxPaymentAmount;

    public enum BankCardBindTypeEnum {
        NORMAL("通用卡"),
        WITHDRAW("提现卡");

        BankCardBindTypeEnum(String type) {
        }
    }

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
        dest.writeString(this.department);
        dest.writeString(this.userName);
        dest.writeString(this.bankAddress);
        dest.writeString(this.bankName);
        dest.writeString(this.bankCardNum);
        dest.writeString(this.phone);
        dest.writeString(this.bankCode);
        dest.writeByte(this.personal ? (byte) 1 : (byte) 0);
        dest.writeInt(this.bindType == null ? -1 : this.bindType.ordinal());
        dest.writeLong(this.maxWithdrawAmount);
        dest.writeLong(this.maxPaymentAmount);
    }

    protected BankCard(Parcel in) {
        this.id = in.readString();
        this.ownerId = in.readString();
        this.department = in.readString();
        this.userName = in.readString();
        this.bankAddress = in.readString();
        this.bankName = in.readString();
        this.bankCardNum = in.readString();
        this.phone = in.readString();
        this.bankCode = in.readString();
        this.personal = in.readByte() != 0;
        int tmpBindType = in.readInt();
        this.bindType = tmpBindType == -1 ? null : BankCardBindTypeEnum.values()[tmpBindType];
        this.maxWithdrawAmount = in.readLong();
        this.maxPaymentAmount = in.readLong();
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
