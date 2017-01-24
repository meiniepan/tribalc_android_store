package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by hjn on 2017/1/22.
 */

public class GoodsStandardMeta implements Parcelable {
    public String id;
    public String title;
    public GoodsStandardDescriptions descriptions;
    public HashMap<String,GoodsPriceAndRepertory> priceAndRepertoryMap ;

    public GoodsStandardMeta() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeParcelable(this.descriptions, flags);
        dest.writeSerializable(this.priceAndRepertoryMap);
    }

    protected GoodsStandardMeta(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.descriptions = in.readParcelable(GoodsStandardDescriptions.class.getClassLoader());
        this.priceAndRepertoryMap = (HashMap<String, GoodsPriceAndRepertory>) in.readSerializable();
    }

    public static final Creator<GoodsStandardMeta> CREATOR = new Creator<GoodsStandardMeta>() {
        @Override
        public GoodsStandardMeta createFromParcel(Parcel source) {
            return new GoodsStandardMeta(source);
        }

        @Override
        public GoodsStandardMeta[] newArray(int size) {
            return new GoodsStandardMeta[size];
        }
    };
}
