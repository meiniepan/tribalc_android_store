package com.gs.buluo.store.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2017/1/23.
 */
public class GoodsStandardDescriptions implements Parcelable {
    public StandardLevel primary;
    public StandardLevel secondary;

    public GoodsStandardDescriptions() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.primary, flags);
        dest.writeParcelable(this.secondary, flags);
    }

    protected GoodsStandardDescriptions(Parcel in) {
        this.primary = in.readParcelable(StandardLevel.class.getClassLoader());
        this.secondary = in.readParcelable(StandardLevel.class.getClassLoader());
    }

    public static final Creator<GoodsStandardDescriptions> CREATOR = new Creator<GoodsStandardDescriptions>() {
        @Override
        public GoodsStandardDescriptions createFromParcel(Parcel source) {
            return new GoodsStandardDescriptions(source);
        }

        @Override
        public GoodsStandardDescriptions[] newArray(int size) {
            return new GoodsStandardDescriptions[size];
        }
    };
}
