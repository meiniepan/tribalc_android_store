package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.WalletAccount;

/**
 * Created by hjn on 2016/11/18.
 */
public interface IWalletView extends IBaseView {
    void getWalletInfoFinished(WalletAccount account);
}
