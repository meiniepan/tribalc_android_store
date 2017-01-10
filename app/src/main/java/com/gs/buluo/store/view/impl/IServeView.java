package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.ResponseBody.ServeResponse;

/**
 * Created by hjn on 2016/11/29.
 */
public interface IServeView extends IBaseView {
    void getServerSuccess(ServeResponse.ServeResponseBody body);
}
