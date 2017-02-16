package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2017/1/23.
 */
public class GoodsMeta implements Parcelable {
    public String id;
    public String standardId; // //创建新商品时，如果存在值，则表示加入指定规格组，否则为不加入任何规格组或者新创建的规格组并加入；修改商品时，被忽略
    public GoodsCategory category;
    public boolean primary;
    public boolean published;
    public String name;
    public String number;
    public String title;
    public String brand;
    public String mainPicture;
    public List<String> pictures;
    public String thumbnail;
    public String detail;
    public String note;
    public ArrayList<String> tags ;
    public String originCountry;
    public String dispatchPlace;
    public String expressType;
    public float expressFee;
    public String saleQuantity;
    public ArrayList<CategoryBean> cookingStyle;
    public List<String> standardKeys;           ////如果standardId指向谋个规格），这里描述了各级（依次）规格的Key信息 "红色", "S码"
    public GoodsPriceAndRepertory priceAndRepertory;  //创建新商品时，如果新建规格组则被忽略，而采用规格组信息中的数据填充商品价格和库存
    public long createTime;

    public boolean isEdit;      //修改商品，不是创建 标志位

    public GoodsMeta() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.standardId);
        dest.writeInt(this.category == null ? -1 : this.category.ordinal());
        dest.writeByte(this.primary ? (byte) 1 : (byte) 0);
        dest.writeByte(this.published ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
        dest.writeString(this.number);
        dest.writeString(this.title);
        dest.writeString(this.brand);
        dest.writeString(this.mainPicture);
        dest.writeStringList(this.pictures);
        dest.writeString(this.thumbnail);
        dest.writeString(this.detail);
        dest.writeString(this.note);
        dest.writeStringList(this.tags);
        dest.writeString(this.originCountry);
        dest.writeString(this.dispatchPlace);
        dest.writeString(this.expressType);
        dest.writeFloat(this.expressFee);
        dest.writeString(this.saleQuantity);
        dest.writeList(this.cookingStyle);
        dest.writeStringList(this.standardKeys);
        dest.writeSerializable(this.priceAndRepertory);
        dest.writeLong(this.createTime);
        dest.writeByte(this.isEdit ? (byte) 1 : (byte) 0);
    }

    protected GoodsMeta(Parcel in) {
        this.id = in.readString();
        this.standardId = in.readString();
        int tmpCategory = in.readInt();
        this.category = tmpCategory == -1 ? null : GoodsCategory.values()[tmpCategory];
        this.primary = in.readByte() != 0;
        this.published = in.readByte() != 0;
        this.name = in.readString();
        this.number = in.readString();
        this.title = in.readString();
        this.brand = in.readString();
        this.mainPicture = in.readString();
        this.pictures = in.createStringArrayList();
        this.thumbnail = in.readString();
        this.detail = in.readString();
        this.note = in.readString();
        this.tags = in.createStringArrayList();
        this.originCountry = in.readString();
        this.dispatchPlace = in.readString();
        this.expressType = in.readString();
        this.expressFee = in.readFloat();
        this.saleQuantity = in.readString();
        this.cookingStyle = new ArrayList<CategoryBean>();
        in.readList(this.cookingStyle, CategoryBean.class.getClassLoader());
        this.standardKeys = in.createStringArrayList();
        this.priceAndRepertory = (GoodsPriceAndRepertory) in.readSerializable();
        this.createTime = in.readLong();
        this.isEdit = in.readByte() != 0;
    }

    public static final Creator<GoodsMeta> CREATOR = new Creator<GoodsMeta>() {
        @Override
        public GoodsMeta createFromParcel(Parcel source) {
            return new GoodsMeta(source);
        }

        @Override
        public GoodsMeta[] newArray(int size) {
            return new GoodsMeta[size];
        }
    };
}
