package com.gs.buluo.store.bean.RequestBodyBean;

import com.gs.buluo.store.bean.HomeMessageEnum;

/**
 * Created by hjn on 2017/8/28.
 */

public class ReadMsgRequest {
    public HomeMessageEnum messageBodyType;
    public String referenceId;

    public ReadMsgRequest(HomeMessageEnum type) {
        messageBodyType = type;
    }

    public ReadMsgRequest() {

    }
}
