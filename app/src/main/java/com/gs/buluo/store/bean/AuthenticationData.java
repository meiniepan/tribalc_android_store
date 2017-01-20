package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hjn on 2017/1/20.
 */

public class AuthenticationData implements Parcelable {
    public List<String> idCardPicture;
    public String businessLicence;
    public String tradeLicence;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.idCardPicture);
        dest.writeString(this.businessLicence);
        dest.writeString(this.tradeLicence);
    }

    public AuthenticationData() {
    }

    protected AuthenticationData(Parcel in) {
        this.idCardPicture = in.createStringArrayList();
        this.businessLicence = in.readString();
        this.tradeLicence = in.readString();
    }

    public static final Parcelable.Creator<AuthenticationData> CREATOR = new Parcelable.Creator<AuthenticationData>() {
        @Override
        public AuthenticationData createFromParcel(Parcel source) {
            return new AuthenticationData(source);
        }

        @Override
        public AuthenticationData[] newArray(int size) {
            return new AuthenticationData[size];
        }
    };
}
