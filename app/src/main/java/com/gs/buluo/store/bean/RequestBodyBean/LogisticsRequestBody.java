package com.gs.buluo.store.bean.RequestBodyBean;

/**
 * Created by hjn on 2017/2/4.
 */
public class LogisticsRequestBody {
    public String logisticsNum;
    public String logisticsCompany;
    public String status;

    public LogisticsRequestBody(String num, String way, String status) {
        logisticsNum = num;
        logisticsCompany  =way;
        this.status = status;
    }
}
