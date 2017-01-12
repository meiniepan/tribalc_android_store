package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.ResponseBody.BillResponse;

/**
 * Created by hjn on 2016/11/18.
 */
public interface IBillView extends IBaseView {
    void getBillSuccess(BillResponse.BillResponseData billList);
}
