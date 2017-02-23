package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import java.util.List;

/**
 * Created by hjn on 2017/1/20.
 */

public class AuthenticationData implements Parcelable, IBaseResponse {
    public List<String> idCardPicture;
    public String businessLicense;
    public String tradeLicense;
    public String authenticationStatus;

    public AuthenticationData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.idCardPicture);
        dest.writeString(this.businessLicense);
        dest.writeString(this.tradeLicense);
        dest.writeString(this.authenticationStatus);
    }

    protected AuthenticationData(Parcel in) {
        this.idCardPicture = in.createStringArrayList();
        this.businessLicense = in.readString();
        this.tradeLicense = in.readString();
        this.authenticationStatus = in.readString();
    }

    public static final Creator<AuthenticationData> CREATOR = new Creator<AuthenticationData>() {
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
