package com.gs.buluo.store.bean;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import java.util.List;

/**
 * Created by hjn on 2017/1/24.
 */
public class StoreGoodsList implements IBaseResponse {
    public boolean published;
    public String sort;
    public String publishedAmount;
    public String unpublishedAmount;
    public String prevSkip;
    public String nextSkip;
    public boolean hasMore;
    public List<GoodsMeta> content;
}
