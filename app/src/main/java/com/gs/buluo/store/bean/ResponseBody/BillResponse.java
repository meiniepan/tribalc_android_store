package com.gs.buluo.store.bean.ResponseBody;

import com.gs.buluo.store.bean.BillEntity;

import java.util.List;

import static com.gs.buluo.store.bean.BillEntity.TradingType;

/**
 * Created by hjn on 2016/11/18.
 */
public class BillResponse {
    public TradingType tradingType;
    public String preSkip;
    public String nextSkip;
    public boolean hasMoren;
    public List<BillEntity> content;
}
