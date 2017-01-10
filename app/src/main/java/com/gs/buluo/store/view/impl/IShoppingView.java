package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.ResponseBody.ShoppingCartResponse;

/**
 * Created by hjn on 2016/12/2.
 */
public interface IShoppingView extends IBaseView {
    void getShoppingCarInfoSuccess(ShoppingCartResponse.ShoppingCartResponseBody body);
}
