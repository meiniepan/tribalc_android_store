package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.UserAddressEntity;

/**
 * Created by hjn on 2016/11/15.
 */
public interface IAddAddressView extends IBaseView{
    void addAddressSuccess(UserAddressEntity entity);
    void updateAddressSuccess(UserAddressEntity entity);
}
