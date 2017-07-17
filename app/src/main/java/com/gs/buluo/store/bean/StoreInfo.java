package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2016/11/10.
 */
@Table(name = "store_info")
public class StoreInfo implements Parcelable {
    @Column(name = "id", isId = true)
    private int mid;
    @Column(name = "cid")
    private String id;

    @Column(name = "name")
    public String name;

    @Column(name = "type")
    private String accountType;

    private List<Privilege> discount;

    public List<Privilege> getDiscount() {
        return discount;
    }

    public void setDiscount(List<Privilege> discount) {
        this.discount = discount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    private String balance;

    public enum StoreType {
        CARD, PROTOCOL
    }

    public StoreType getType() {
        return accountType == null ? StoreType.CARD : StoreType.valueOf(accountType);
    }

    public void setType(StoreType type) {
        this.accountType = type.name();
    }

    @Column(name = "token")
    public String token;

    @Column(name = "store_type")
    public String storeType;

    @Column(name = "logo")
    public String logo;

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
        dest.writeString(this.accountType);
        dest.writeList(this.discount);
        dest.writeString(this.balance);
        dest.writeString(this.token);
        dest.writeString(this.storeType);
        dest.writeString(this.logo);
    }

    protected StoreInfo(Parcel in) {
        this.mid = in.readInt();
        this.id = in.readString();
        this.name = in.readString();
        this.accountType = in.readString();
        this.discount = new ArrayList<Privilege>();
        in.readList(this.discount, Privilege.class.getClassLoader());
        this.balance = in.readString();
        this.token = in.readString();
        this.storeType = in.readString();
        this.logo = in.readString();
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
