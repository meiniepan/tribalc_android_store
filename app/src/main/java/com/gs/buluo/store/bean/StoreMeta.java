package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2017/1/12.
 */

public class StoreMeta extends StoreAccount{
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
}
