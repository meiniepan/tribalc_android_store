package com.gs.buluo.store.bean.ResponseBody;

import com.gs.buluo.store.bean.WithdrawBill;

import java.util.List;

/**
 * Created by hjn on 2017/7/18.
 */

public class WithdrawBillResponse {
    public String prevSkip;
    public String nextSkip;
    public boolean hasMore;
    public List<WithdrawBill> content;
}
