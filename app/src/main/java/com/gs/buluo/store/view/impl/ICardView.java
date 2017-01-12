package com.gs.buluo.store.view.impl;

import com.gs.buluo.store.bean.BankCard;

import java.util.List;

/**
 * Created by hjn on 2016/11/23.
 */
public interface ICardView extends IBaseView {
    void getCardInfoSuccess(List<BankCard> data);
}
