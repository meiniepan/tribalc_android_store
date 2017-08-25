package com.gs.buluo.store.eventbus;

import com.gs.buluo.store.bean.HomeMessageEnum;

/**
 * Created by hjn on 2017/8/25.
 */

public class NewMessageEvent {
    private HomeMessageEnum type;

    public NewMessageEvent(HomeMessageEnum name) {
        type = name;
    }
}
