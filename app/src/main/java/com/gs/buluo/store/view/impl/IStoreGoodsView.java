package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.GoodsMeta;

import java.util.List;

/**
 * Created by hjn on 2017/1/24.
 */
public interface IStoreGoodsView extends IBaseView {
    void getGoodsSuccess(List<GoodsMeta> list,boolean published);
    void showNoMore(boolean published);
}
