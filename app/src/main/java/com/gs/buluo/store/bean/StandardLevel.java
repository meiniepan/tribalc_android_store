package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hjn on 2017/1/23.
 */

public class StandardLevel implements Parcelable {
    public String label;
    public List<String> types;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeStringList(this.types);
    }

    public StandardLevel() {
    }

    protected StandardLevel(Parcel in) {
        this.label = in.readString();
        this.types = in.createStringArrayList();
    }

    public static final Parcelable.Creator<StandardLevel> CREATOR = new Parcelable.Creator<StandardLevel>() {
        @Override
        public StandardLevel createFromParcel(Parcel source) {
            return new StandardLevel(source);
        }

        @Override
        public StandardLevel[] newArray(int size) {
            return new StandardLevel[size];
        }
    };
}
