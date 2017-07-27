package com.gs.buluo.store.bean;

import java.io.Serializable;

/**
 * Created by hjn on 2016/11/17.
 */
public class BillEntity implements Serializable {
    public enum TradingType {
        PAY("PAY"), RECEIPT("RECEIPT"), RECHARGE("RECHARGE"), WITHDRAW("WITHDRAW");
        String status;

        TradingType(String status) {
            this.status = status;
        }
    }

    public enum PayChannel {
        BALANCE("BALANCE"), ALIPAY("ALIPAY"), WEICHAT("WEICHAT"), BANKCARD("BANKCARD");
        String status;

        PayChannel(String status) {
            this.status = status;
        }
    }

    public String id;
    public String createTime;
    public String title;
    public String paymentId;
    public String anotherId;
    public String amount;
    public String balance;
    public TradingType tradingType;
    public PayChannel payChannel;
    public String ownerId;
    public String note;
    public String associatedOrderId;
}
