package com.gs.buluo.store.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by hjn on 2016/11/10.
 */
@Table(name = "store_info")
public class StoreAccount {
    @Column(name = "id", isId = true)
    private int mid;
    @Column(name = "cid")
    private String id;

    @Column(name = "name")
    public String name;

    @Column(name = "type")
    private String accountType;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "phone")
    public String phone;

    private List<Privilege> discount;

    public List<Privilege> getDiscount() {
        return discount;
    }

    public void setDiscount(List<Privilege> discount) {
        this.discount = discount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    private String balance;

    public enum StoreType {
        CARD, PROTOCOL
    }

    public StoreType getType() {
        return accountType == null ? StoreType.PROTOCOL : StoreType.valueOf(accountType);
    }

    public void setType(StoreType type) {
        this.accountType = type.name();
    }

    @Column(name = "token")
    public String token;

    @Column(name = "store_type")
    public String storeType;

    @Column(name = "logo")
    public String logo;

    @Column(name = "identity")
    public String legalPersonIdNo;

    @Column(name = "user_name")
    public String legalPersonName;

    @Column(name = "auth")
    public String authorizedStatus;

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getLegalPersonIdNo() {
        return legalPersonIdNo;
    }

    public void setLegalPersonIdNo(String legalPersonIdNo) {
        this.legalPersonIdNo = legalPersonIdNo;
    }


    public AuthStatus getAuthStatus() {
        switch (authorizedStatus) {
            case "NOT_START":
                return AuthStatus.NOT_START;
            case "PROCESSING":
                return AuthStatus.PROCESSING;
            case "SUCCESS":
                return AuthStatus.SUCCESS;
            case "FAILURE":
                return AuthStatus.FAILURE;
        }
        return AuthStatus.NOT_START;
    }

    public void setAuthStatus(AuthStatus authorizedStatus) {
        this.authorizedStatus = authorizedStatus.name();
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
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
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
