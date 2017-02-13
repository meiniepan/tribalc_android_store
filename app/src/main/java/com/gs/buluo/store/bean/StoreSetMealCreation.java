package com.gs.buluo.store.bean;

import java.util.List;

/**
 * Created by hjn on 2017/1/19.
 */

public class StoreSetMealCreation {
    public String id;
    public String name ;
    public List<String> pictures;
    public String topics;
    public String recommendedReason;
    public String personExpense;
    public boolean reservable;
    public StoreMeta.StoreCategory category;
}
