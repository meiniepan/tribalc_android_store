package com.gs.buluo.store.eventbus;

import com.gs.buluo.store.bean.OrderBean;

/**
 * Created by hjn on 2016/12/20.
 */
public class PaymentEvent {

    private  OrderBean data;
    private String orderId;

    public PaymentEvent() {
    }

    public PaymentEvent(String orderId) {
        this.orderId = orderId;
    }

    public PaymentEvent(OrderBean data) {
        this.data =data;
    }

    public OrderBean getData() {
        return data;
    }
}
