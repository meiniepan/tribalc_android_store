package com.gs.buluo.store.bean;

/**
 * Created by hjn on 2017/7/19.
 */

public enum HomeMessageEnum {
    ACCOUNT_WALLET_PAYMENT("钱包助手", "付款通知"),
    ACCOUNT_WALLET_RECHARGE("钱包助手", "充值到账"),
    CREDIT_ENABLE("授信助手", "开通授信"),
    CREDIT_DISABLE("授信助手", "授信额度冻结"),
    CREDIT_BILL_GENERATION("授信助手", "账单生成"),
    CREDIT_BILL_PAYMENT("授信助手", "账单支付"),
    RENT_CHECK_IN("公寓管家", "入住通知"),
    RENT_BILL_GENERATION("公寓管家", "缴租提醒"),
    RENT_BILL_PAYMENT("公寓管家", "租金缴纳"),
    TENANT_RECHARGE("商户助手", "收款到账"),
    TENANT_WITHDRAW("商户助手", "提现到账"),
    COMPANIES_ADMIN("企业办公", "管理变更"),
    COMPANIES_RENT_BILL_GENERATION("企业办公", "缴租提醒"),
    COMPANIES_RENT_BILL_PAYMENT("企业办公", "租金缴纳");

    public String owner;
    public String type;

    HomeMessageEnum(String owner, String type) {
        this.owner = owner;
        this.type = type;
    }
}
