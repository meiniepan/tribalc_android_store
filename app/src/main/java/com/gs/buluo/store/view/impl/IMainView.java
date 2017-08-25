package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.HomeMessage;
import com.gs.buluo.store.bean.HomeMessageResponse;
import com.gs.buluo.store.bean.UnReadMessageBean;
import com.gs.buluo.store.bean.WalletAccount;

/**
 * Created by hjn on 2016/11/3.
 */
public interface IMainView extends IBaseView {
    void getWalletSuccess(WalletAccount account);

    void getMessageSuccess(HomeMessageResponse data, boolean isNewer);

    void deleteSuccess(HomeMessage message);

    void ignoreSuccess(HomeMessage message);

    void getUnReadMessageSuccess(UnReadMessageBean response);
}
