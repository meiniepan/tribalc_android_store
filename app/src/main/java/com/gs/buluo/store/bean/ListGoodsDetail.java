package com.gs.buluo.store.bean;

import android.os.Parcel;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import java.util.List;

/**
 * Created by hjn on 2016/11/22.
 */
public class ListGoodsDetail extends ListGoods implements IBaseResponse {
    public String title;
    public String standardId;
    //    public String standardSnapshot;
    public boolean snapshot;
    public boolean published;
    public GoodsCategory category;
    public List<String> pictures;
    public String thumbnail;
    public String detailURL;
    public int repertory;
    public String note;
    public List<String> tags;
    public String originCountry;
    public String dispatchPlace;
    public MarkStore tMarkStore;

    public ListGoodsDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.title);
        dest.writeString(this.standardId);
        dest.writeByte(this.snapshot ? (byte) 1 : (byte) 0);
        dest.writeByte(this.published ? (byte) 1 : (byte) 0);
        dest.writeInt(this.category == null ? -1 : this.category.ordinal());
        dest.writeStringList(this.pictures);
        dest.writeString(this.thumbnail);
        dest.writeString(this.detailURL);
        dest.writeInt(this.repertory);
        dest.writeString(this.note);
        dest.writeStringList(this.tags);
        dest.writeString(this.originCountry);
        dest.writeString(this.dispatchPlace);
        dest.writeParcelable(this.tMarkStore, flags);
    }

    protected ListGoodsDetail(Parcel in) {
        super(in);
        this.title = in.readString();
        this.standardId = in.readString();
        this.snapshot = in.readByte() != 0;
        this.published = in.readByte() != 0;
        int tmpCategory = in.readInt();
        this.category = tmpCategory == -1 ? null : GoodsCategory.values()[tmpCategory];
        this.pictures = in.createStringArrayList();
        this.thumbnail = in.readString();
        this.detailURL = in.readString();
        this.repertory = in.readInt();
        this.note = in.readString();
        this.tags = in.createStringArrayList();
        this.originCountry = in.readString();
        this.dispatchPlace = in.readString();
        this.tMarkStore = in.readParcelable(MarkStore.class.getClassLoader());
    }

    public static final Creator<ListGoodsDetail> CREATOR = new Creator<ListGoodsDetail>() {
        @Override
        public ListGoodsDetail createFromParcel(Parcel source) {
            return new ListGoodsDetail(source);
        }

        @Override
        public ListGoodsDetail[] newArray(int size) {
            return new ListGoodsDetail[size];
        }
    };
}
