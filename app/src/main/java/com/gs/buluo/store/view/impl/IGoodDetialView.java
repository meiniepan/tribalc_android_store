package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.bean.GoodsStandard;

/**
 * Created by hjn on 2016/11/22.
 */
public interface IGoodDetialView extends IBaseView{
    void getDetailSuccess(ListGoodsDetail entity);
    void getStandardSuccess(GoodsStandard standard);
    void addSuccess();
}
