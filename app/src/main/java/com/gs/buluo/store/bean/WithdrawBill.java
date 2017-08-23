package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2017/7/14.
 */

public class WithdrawBill implements Parcelable {
    public String id;
    public String ownerId;
    public long time;
    public String accountNumber;
    public String amount;
    public WithdrawStatus status;

    public enum WithdrawStatus {
        CREATED("提现中"), COMMITTED("已提交"), PAYED("已支付"), FINISHED("提现已完成"), FAILURE("提现已失败"), REJECTED("驳回");
        public String status;

        WithdrawStatus(String status) {
            this.status = status;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.ownerId);
        dest.writeLong(this.time);
        dest.writeString(this.accountNumber);
        dest.writeString(this.amount);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
    }

    public WithdrawBill() {
    }

    protected WithdrawBill(Parcel in) {
        this.id = in.readString();
        this.ownerId = in.readString();
        this.time = in.readLong();
        this.accountNumber = in.readString();
        this.amount = in.readString();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : WithdrawStatus.values()[tmpStatus];
    }

    public static final Parcelable.Creator<WithdrawBill> CREATOR = new Parcelable.Creator<WithdrawBill>() {
        @Override
        public WithdrawBill createFromParcel(Parcel source) {
            return new WithdrawBill(source);
        }

        @Override
        public WithdrawBill[] newArray(int size) {
            return new WithdrawBill[size];
        }
    };
}
