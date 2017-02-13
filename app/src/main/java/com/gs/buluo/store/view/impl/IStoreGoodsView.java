package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.StoreGoodsList;

import java.util.List;

/**
 * Created by hjn on 2017/1/24.
 */
public interface IStoreGoodsView extends IBaseView {
    void getGoodsSuccess(StoreGoodsList data, boolean published);
    void showNoMore(boolean published);
}
