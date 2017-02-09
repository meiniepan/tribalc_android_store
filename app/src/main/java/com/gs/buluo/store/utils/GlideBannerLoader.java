package com.gs.buluo.store.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.Constant;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by hjn on 2016/11/2.
 */
public class GlideBannerLoader extends ImageLoader {
    private boolean isLocal = false;

    public GlideBannerLoader(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public GlideBannerLoader() {
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (isLocal) {
            imageView.setBackgroundResource((int) path);
            return;
        }
        if (path == null) return;
        String url = path.toString();
        if (!url.contains("://")) {
            url = Constant.Base.BASE_IMG_URL + url;
        } else {
            url = transformUrl(url);
        }
        Glide.with(context).load(url).into(imageView);
    }

    public String transformUrl(String url) {
        String[] arrs = url.split("\\://");
        String head = arrs[0];
        String body = arrs[1];
        switch (head) {
            case "oss":
                return Constant.Base.BASE_ONLINE_URL + body;
            default:
                return Constant.Base.BASE_IMG_URL + body;
        }
    }
}
