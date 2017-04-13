package com.gs.buluo.store.bean.ResponseBody;

import com.gs.buluo.common.network.BaseCode;

/**
 * Created by hjn on 2016/11/23.
 */
public class BaseResponse<T> extends BaseCode{
    public String message;
    public T data;
}
