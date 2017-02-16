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
    public String subbranchName;
    public String desc;
    public StoreCategory category;                //Default FOOD From { FOOD, GIFT, OFFICE, LIVING, HOUSE, MAKEUP, PENETRATION, REPAST, HAIRDRESSING, FITNESS, ENTERTAINMENT, KEEPHEALTHY}
    public String otherPhone;        //其他电话
    public String province;
    public String city;
    public String district;
    public String address;
    public ArrayList<String> pictures;
    public List<String> facilities;              //辅助设施
    public String topics;
    public String personExpense;
    public List<String> cookingStyle;
    public String businessHours;
    public double[] coordinate;
    public String markPlace;

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
        dest.writeString(this.subbranchName);
        dest.writeString(this.desc);
        dest.writeInt(this.category == null ? -1 : this.category.ordinal());
        dest.writeString(this.otherPhone);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.address);
        dest.writeStringList(this.pictures);
        dest.writeStringList(this.facilities);
        dest.writeString(this.topics);
        dest.writeString(this.personExpense);
        dest.writeStringList(this.cookingStyle);
        dest.writeString(this.businessHours);
        dest.writeDoubleArray(this.coordinate);
    }

    protected StoreMeta(Parcel in) {
        super(in);
        this.subbranchName = in.readString();
        this.desc = in.readString();
        int tmpCategory = in.readInt();
        this.category = tmpCategory == -1 ? null : StoreCategory.values()[tmpCategory];
        this.otherPhone = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.address = in.readString();
        this.pictures = in.createStringArrayList();
        this.facilities = in.createStringArrayList();
        this.topics = in.readString();
        this.personExpense = in.readString();
        this.cookingStyle = in.createStringArrayList();
        this.businessHours = in.readString();
        this.coordinate = in.createDoubleArray();
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