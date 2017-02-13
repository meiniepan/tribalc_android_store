package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.bean.StoreSetMealCreation;

/**
 * Created by hjn on 2017/2/9.
 */
public interface IInfoView extends IBaseView{
    void updateSuccess();

    void setData(StoreMeta data);

    void setMealData(StoreSetMealCreation mealCreation);
}
