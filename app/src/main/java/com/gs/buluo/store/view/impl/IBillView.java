package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.ResponseBody.BillResponse;
import com.gs.buluo.store.bean.ResponseBody.WithdrawBillResponse;

/**
 * Created by hjn on 2016/11/18.
 */
public interface IBillView extends IBaseView {
    void getBillSuccess(BillResponse billList);

    void getWithdrawBillSuccess(WithdrawBillResponse billList);
}
