package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.UserAddressEntity;

/**
 * Created by hjn on 2016/11/14.
 */
public interface IAddressView extends IBaseView{
    void deleteSuccessInfo(UserAddressEntity data);
    void updateDefaultAddressSuccess(UserAddressEntity data);
}
