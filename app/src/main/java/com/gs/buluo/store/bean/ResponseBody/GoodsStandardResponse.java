package com.gs.buluo.store.bean.ResponseBody;

import com.gs.buluo.store.bean.GoodsStandardMeta;

import java.util.List;

/**
 * Created by hjn on 2017/1/22.
 */

public class GoodsStandardResponse implements IBaseResponse {
    public String category;
    public String sort;
    public String prevSkip;
    public String nextSkip;
    public boolean hasMore;
    public List<GoodsStandardMeta> content;
}
