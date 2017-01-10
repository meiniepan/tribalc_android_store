package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.DetailReservation;

/**
 * Created by hjn on 2016/11/30.
 */
public interface IDetailReserveView extends IBaseView{
    void getDetailSuccess(DetailReservation reservation);
    void cancelSuccess();
    void cancelFailure();
}
