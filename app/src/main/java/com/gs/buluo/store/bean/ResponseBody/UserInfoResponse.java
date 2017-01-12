package com.gs.buluo.store.bean.ResponseBody;

import com.gs.buluo.store.bean.StoreInfo;

/**
 * Created by hjn on 2016/11/10.
 */
public class UserInfoResponse {
    private int iid;
    private int code;
    private StoreInfo data;
    private String message;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(StoreInfo data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public StoreInfo getData() {
        if (data == null) {
            data = new StoreInfo();
        }
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }
}
