package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by hjn on 2016/11/10.
 */
@Table(name = "store_info")
public class StoreInfo implements IBaseResponse, Parcelable {
    @Column(name = "id", isId = true)
    private int mid;
    @Column(name = "cid")
    private String id;

    @Column(name = "name")
    public String name;

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getStoreAuthenticationStatus() {
        return storeAuthenticationStatus;
    }

    public void setStoreAuthenticationStatus(String storeAuthenticationStatus) {
        this.storeAuthenticationStatus = storeAuthenticationStatus;
    }

    @Column(name = "linkman")
    public String linkman;

    @Column(name = "token")
    public String token;

    @Column(name = "store_type")
    public String storeType;

    @Column(name = "phone")
    public String phone;

    @Column(name = "cover")
    public String cover;

    @Column(name = "logo")
    public String logo;

    @Column(name = "status")
    public String storeAuthenticationStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getId() {
//        return "584e4f8c1c3e73d1bc07e6ea";
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public StoreInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mid);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.linkman);
        dest.writeString(this.token);
        dest.writeString(this.storeType);
        dest.writeString(this.phone);
        dest.writeString(this.cover);
        dest.writeString(this.logo);
        dest.writeString(this.storeAuthenticationStatus);
    }

    protected StoreInfo(Parcel in) {
        this.mid = in.readInt();
        this.id = in.readString();
        this.name = in.readString();
        this.linkman = in.readString();
        this.token = in.readString();
        this.storeType = in.readString();
        this.phone = in.readString();
        this.cover = in.readString();
        this.logo = in.readString();
        this.storeAuthenticationStatus = in.readString();
    }

    public static final Creator<StoreInfo> CREATOR = new Creator<StoreInfo>() {
        @Override
        public StoreInfo createFromParcel(Parcel source) {
            return new StoreInfo(source);
        }

        @Override
        public StoreInfo[] newArray(int size) {
            return new StoreInfo[size];
        }
    };
}
