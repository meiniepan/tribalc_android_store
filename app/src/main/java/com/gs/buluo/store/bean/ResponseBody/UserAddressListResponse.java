package com.gs.buluo.store.bean.ResponseBody;

import com.gs.buluo.store.bean.UserAddressEntity;

import java.util.List;

/**
 * Created by hjn on 2016/11/11.
 */
public class UserAddressListResponse {
    public int code;
    public List<UserAddressEntity > data;
    public String message;
}
