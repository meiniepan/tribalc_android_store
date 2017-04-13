package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.ResponseBody.ReserveResponseBody;

/**
 * Created by hjn on 2016/11/29.
 */
public interface IReserveView extends IBaseView {

    void getReserveSuccess(ReserveResponseBody data);
}
