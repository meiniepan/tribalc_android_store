package com.gs.buluo.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hjn on 2017/1/12.
 */

public class CreateStoreBean implements Parcelable {
    public String id;
    public String name;
    public String subbranchName;
    public StoreCategory category;                //Default FOOD From { FOOD, GIFT, OFFICE, LIVING, HOUSE, MAKEUP, PENETRATION, REPAST, HAIRDRESSING, FITNESS, ENTERTAINMENT, KEEPHEALTHY}
    public String linkman;                //店主姓名
    public String phone;              //电话
    public String otherPhone;        //其他电话
    public String coordinate;         //坐标
    public String businessHours;   //营业时间
    public String cookingStyle;           //菜系类型
    public String logo;     //logo
    public List<String> surroundingsPicture;     //环境图
    public String recommendedReason;         //推荐理由
    public String topics;                //服务话题
    public String personExpense;             //人均消费
    public String reservationNum;                   //预定人数
    public String reservable;                  //是否接受预定
    public List<String> faclities;              //辅助设施
    public String storeCreationStatus;    //店铺创建状态
    public String registeredNum;       //店铺注册号
    public String licenseName    ;     //营业执照名称
    public String licensePicture  ; //营业执照照片
    public String legalPersonName;
    public String legalPersonIdCardNo;
    public String leagalPersonPicture;
    public String threadLicensetype;
    public String threadLicenseName;
    public String  threadLicenseScope;
    public String  threadLicensePicture;

    public enum StoreCategory {
        FOOD("食品"), GIFT("礼品"), OFFICE("办公用品"), LIVING("生活用品"), HOUSE("家居用品"), MAKEUP("化妆品"), PENETRATION("妇婴用品"),
        REPAST("餐饮"), HAIRDRESSING("美容"), FITNESS("健身"), ENTERTAINMENT("休闲娱乐"), KEEPHEALTHY("养身");

        StoreCategory(String s) {
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.subbranchName);
        dest.writeInt(this.category == null ? -1 : this.category.ordinal());
        dest.writeString(this.linkman);
        dest.writeString(this.phone);
        dest.writeString(this.otherPhone);
        dest.writeString(this.coordinate);
        dest.writeString(this.businessHours);
        dest.writeString(this.cookingStyle);
        dest.writeString(this.logo);
        dest.writeStringList(this.surroundingsPicture);
        dest.writeString(this.recommendedReason);
        dest.writeString(this.topics);
        dest.writeString(this.personExpense);
        dest.writeString(this.reservationNum);
        dest.writeString(this.reservable);
        dest.writeStringList(this.faclities);
        dest.writeString(this.storeCreationStatus);
        dest.writeString(this.registeredNum);
        dest.writeString(this.licenseName);
        dest.writeString(this.licensePicture);
        dest.writeString(this.legalPersonName);
        dest.writeString(this.legalPersonIdCardNo);
        dest.writeString(this.leagalPersonPicture);
        dest.writeString(this.threadLicensetype);
        dest.writeString(this.threadLicenseName);
        dest.writeString(this.threadLicenseScope);
        dest.writeString(this.threadLicensePicture);
    }

    public CreateStoreBean() {
    }

    protected CreateStoreBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.subbranchName = in.readString();
        int tmpCategory = in.readInt();
        this.category = tmpCategory == -1 ? null : StoreCategory.values()[tmpCategory];
        this.linkman = in.readString();
        this.phone = in.readString();
        this.otherPhone = in.readString();
        this.coordinate = in.readString();
        this.businessHours = in.readString();
        this.cookingStyle = in.readString();
        this.logo = in.readString();
        this.surroundingsPicture = in.createStringArrayList();
        this.recommendedReason = in.readString();
        this.topics = in.readString();
        this.personExpense = in.readString();
        this.reservationNum = in.readString();
        this.reservable = in.readString();
        this.faclities = in.createStringArrayList();
        this.storeCreationStatus = in.readString();
        this.registeredNum = in.readString();
        this.licenseName = in.readString();
        this.licensePicture = in.readString();
        this.legalPersonName = in.readString();
        this.legalPersonIdCardNo = in.readString();
        this.leagalPersonPicture = in.readString();
        this.threadLicensetype = in.readString();
        this.threadLicenseName = in.readString();
        this.threadLicenseScope = in.readString();
        this.threadLicensePicture = in.readString();
    }

    public static final Parcelable.Creator<CreateStoreBean> CREATOR = new Parcelable.Creator<CreateStoreBean>() {
        @Override
        public CreateStoreBean createFromParcel(Parcel source) {
            return new CreateStoreBean(source);
        }

        @Override
        public CreateStoreBean[] newArray(int size) {
            return new CreateStoreBean[size];
        }
    };
}
