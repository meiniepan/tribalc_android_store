package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.GoodList;

/**
 * Created by hjn on 2016/11/16.
 */
public interface IGoodsView extends IBaseView {
    void getGoodsInfo(GoodList responseList);
}
