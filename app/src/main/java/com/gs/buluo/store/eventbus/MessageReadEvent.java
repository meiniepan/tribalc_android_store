package com.gs.buluo.store.eventbus;

import com.gs.buluo.store.bean.HomeMessageEnum;

/**
 * Created by hjn on 2017/8/25.
 */

public class MessageReadEvent {
    private HomeMessageEnum type;

    public MessageReadEvent(HomeMessageEnum type) {
        this.type = type;
    }

    public HomeMessageEnum getType() {
        return type;
    }
}
