package com.gs.buluo.store.eventbus;

import com.gs.buluo.store.bean.GoodsMeta;

/**
 * Created by hjn on 2017/1/25.
 */
public class GoodsChangedEvent {
    private  GoodsMeta entity;

    public GoodsChangedEvent(GoodsMeta entity) {
        this.entity = entity;
    }

}
