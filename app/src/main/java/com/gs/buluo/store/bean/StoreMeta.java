package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2017/1/12.
 */

public class StoreMeta extends StoreInfo implements Parcelable, IBaseResponse {
    public String desc;
    public String category;                //Default FOOD From { FOOD, GIFT, OFFICE, LIVING, HOUSE, MAKEUP, PENETRATION, REPAST, HAIRDRESSING, FITNESS, ENTERTAINMENT, KEEPHEALTHY}
    public String province;
    public String city;
    public String district;
    public String address;
    public ArrayList<String> pictures;
    public List<String> sellingPoint;
    public String topics;
    public String avgprice;
    public List<String> cookingStyle;
    public String businessHours;
    public double[] coordinate;
    public String markPlace;
    public String tags;
    public String email;
    public String serviceLine;

    public enum StoreCategory {
        FOOD("食品"), GIFT("礼品"), OFFICE("办公用品"), LIVING("生活用品"), HOUSE("家居用品"), MAKEUP("化妆品"), PENETRATION("妇婴用品"),
        REPAST("餐饮"), HAIRDRESSING("美容"), FITNESS("健身"), ENTERTAINMENT("休闲娱乐"), KEEPHEALTHY("养身");
        String type;

        StoreCategory(String s) {
            type=s;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public StoreMeta() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.desc);
        dest.writeString(this.category);
        dest.writeString(this.phone);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.address);
        dest.writeStringList(this.pictures);
        dest.writeStringList(this.sellingPoint);
        dest.writeString(this.topics);
        dest.writeString(this.avgprice);
        dest.writeStringList(this.cookingStyle);
        dest.writeString(this.businessHours);
        dest.writeDoubleArray(this.coordinate);
        dest.writeString(this.markPlace);
        dest.writeString(this.tags);
        dest.writeString(this.email);
    }

    protected StoreMeta(Parcel in) {
        super(in);
        this.desc = in.readString();
        this.category = in.readString();
        this.phone = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.address = in.readString();
        this.pictures = in.createStringArrayList();
        this.sellingPoint = in.createStringArrayList();
        this.topics = in.readString();
        this.avgprice = in.readString();
        this.cookingStyle = in.createStringArrayList();
        this.businessHours = in.readString();
        this.coordinate = in.createDoubleArray();
        this.markPlace = in.readString();
        this.tags = in.readString();
        this.email = in.readString();
    }

    public static final Creator<StoreMeta> CREATOR = new Creator<StoreMeta>() {
        @Override
        public StoreMeta createFromParcel(Parcel source) {
            return new StoreMeta(source);
        }

        @Override
        public StoreMeta[] newArray(int size) {
            return new StoreMeta[size];
        }
    };
}
