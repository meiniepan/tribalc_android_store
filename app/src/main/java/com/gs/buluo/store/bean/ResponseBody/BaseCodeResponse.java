package com.gs.buluo.store.bean.ResponseBody;

/**
 * Created by hjn on 2016/11/23.
 */
public class BaseCodeResponse<T extends IBaseResponse> {
    public int code;
    public T data;
}
