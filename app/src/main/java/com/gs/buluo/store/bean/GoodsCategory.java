package com.gs.buluo.store.bean;

/**
 * Created by hjn on 2017/1/23.
 */

public enum  GoodsCategory {
    FOOD("食品"), GIFT("礼品"), OFFICE("办公用品"), LIVING("生活用品"), HOUSE("家居用品"), MAKEUP("化妆品"), PENETRATION("妇婴用品"),
    VIP("会员卡");
    String type;

    GoodsCategory(String s) {
        type=s;
    }

    @Override
    public String toString() {
        return type;
    }
}

