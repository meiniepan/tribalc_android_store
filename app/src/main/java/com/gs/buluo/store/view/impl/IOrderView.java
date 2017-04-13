package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.OrderBean;
import com.gs.buluo.store.bean.ResponseBody.OrderResponseBean;

/**
 * Created by hjn on 2016/11/24.
 */
public interface IOrderView extends IBaseView {
    void getOrderInfoSuccess(OrderResponseBean data);

    void updateSuccess(OrderBean data);
}
