package com.gs.buluo.store.bean;

/**
 * Created by hjn on 2017/2/10.
 */
public class BannerPicture {
    public int posiotn;
    public String url;
    public boolean isChecked;

    public BannerPicture(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
