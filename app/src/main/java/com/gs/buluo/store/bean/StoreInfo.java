package com.gs.buluo.store.bean;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by hjn on 2016/11/10.
 */
@Table(name = "store_info")
public class StoreInfo implements IBaseResponse {
    @Column(name = "id", isId = true)
    private int mid;
    @Column(name = "cid")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "token")
    private String token;

    @Column(name = "store_type")
    private String storeType;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "cover")
    private String cover;

    @Column(name = "district")
    private String district;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "logo")
    private String logo;

    @Column(name = "area")
    private String area;

    public String getArea() {
        if (area==null||area.contains("null"))
            return "";
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

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
//        return "584e4f8c1c3e73d1bc07e6ea";
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String coordinate) {
        this.district = coordinate;
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
}
