package com.gs.buluo.store.bean;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by hjn on 2016/11/22.
 */
public class GoodsStandard implements IBaseResponse {
    public String id;
    public Map<String, ListGoodsDetail> goodsIndexes;
    public GoodsStandardDescriptions descriptions;
}
