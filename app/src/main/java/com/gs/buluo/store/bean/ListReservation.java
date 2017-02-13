package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import java.util.List;

/**
 * Created by hjn on 2016/11/29.
 */
public class ListReservation implements Parcelable, IBaseResponse {
    public String id;
    public String storeId;
    public String storeSetMealId;
    public String storeName;
    public String mainPicture;
    public String markPlace;
    public long appointTime;
    public String personNum;
    public List<String> tags;
    public ReserveStatus status;
    public String linkman;
    public String phone;

    public enum ReserveStatus {
        PROCESSING("PROCESSING"), FAILURE("FAILURE"), SUCCEED("SUCCEED"), CANCEL("CANCEL");
        public String status;

        ReserveStatus(String processing) {
            status = processing;
        }
    }

    public ListReservation() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.storeId);
        dest.writeString(this.storeSetMealId);
        dest.writeString(this.storeName);
        dest.writeString(this.mainPicture);
        dest.writeString(this.markPlace);
        dest.writeLong(this.appointTime);
        dest.writeString(this.personNum);
        dest.writeStringList(this.tags);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeString(this.linkman);
        dest.writeString(this.phone);
    }

    protected ListReservation(Parcel in) {
        this.id = in.readString();
        this.storeId = in.readString();
        this.storeSetMealId = in.readString();
        this.storeName = in.readString();
        this.mainPicture = in.readString();
        this.markPlace = in.readString();
        this.appointTime = in.readLong();
        this.personNum = in.readString();
        this.tags = in.createStringArrayList();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : ReserveStatus.values()[tmpStatus];
        this.linkman = in.readString();
        this.phone = in.readString();
    }

    public static final Creator<ListReservation> CREATOR = new Creator<ListReservation>() {
        @Override
        public ListReservation createFromParcel(Parcel source) {
            return new ListReservation(source);
        }

        @Override
        public ListReservation[] newArray(int size) {
            return new ListReservation[size];
        }
    };
}
