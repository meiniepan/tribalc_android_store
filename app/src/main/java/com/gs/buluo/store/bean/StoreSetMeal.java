package com.gs.buluo.store.bean;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import java.util.List;

/**
 * Created by hjn on 2017/1/19.
 */
public class StoreSetMeal implements IBaseResponse {
    public String name ;
    public List<String> pictures;
    public String topics;
    public String recommendedReason;
    public String personExpense;
    public boolean isReservable;
}
