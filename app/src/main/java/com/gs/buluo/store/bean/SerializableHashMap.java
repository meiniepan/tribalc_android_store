package com.gs.buluo.store.bean;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by hjn on 2017/1/23.
 */

public class SerializableHashMap<S, G> implements Serializable {
    private HashMap<String,GoodsPriceAndRepertory> map;
    public HashMap<String, GoodsPriceAndRepertory> getMap() {
        return map;
    }

    public void setMap(HashMap<String, GoodsPriceAndRepertory> map) {
        this.map = map;
    }
}
