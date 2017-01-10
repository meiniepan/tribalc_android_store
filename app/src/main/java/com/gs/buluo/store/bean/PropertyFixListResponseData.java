package com.gs.buluo.store.bean;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import java.util.List;

/**
 * Created by fs on 2016/12/15.
 */

public class PropertyFixListResponseData implements IBaseResponse {
    public List<ListPropertyManagement> content;
    public boolean hasMore;
    public String prevSkip;
    public String nextSkip;
    public String sort;
    public String status;
}
