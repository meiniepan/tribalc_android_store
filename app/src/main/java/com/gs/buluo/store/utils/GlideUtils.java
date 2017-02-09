package com.gs.buluo.store.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;

import java.io.File;

/**
 * Created by hjn on 2016/11/1.
 */
public class GlideUtils {
    public static String formatImageUrl(String url) {
        if (url==null)return "";
        String ret = url;
        if (url.contains("://")) {
            String[] arrs = url.split("\\://");
            String head = arrs[0];
            String body = arrs[1];
            switch (head) {
                case "oss":
                    ret = Constant.Base.BASE_ONLINE_URL + body;
                    break;
                default:
                    ret = Constant.Base.BASE_IMG_URL + body;
            }
        } else {
            ret = Constant.Base.BASE_IMG_URL + url;
        }

        return ret;
    }

    public static void loadImage(Context context,String url, ImageView imageView) {
        if (url == null) return;
        if (!url.contains("://")) {
            url = Constant.Base.BASE_IMG_URL + url;
        } else {
            url = transformUrl(url);
        }
        Glide.with(context).load(url).placeholder(R.mipmap.default_pic).into(imageView);
    }

    public static void loadImage(Context context,String url, ImageView imageView,boolean isCircle) {
        if (url == null) return;
        if (!url.contains("://")) {
            url = Constant.Base.BASE_IMG_URL + url;
        } else {
            url = transformUrl(url);
        }
        Glide.with(context).load(url).placeholder(R.mipmap.default_pic).bitmapTransform(new CropCircleTransformation(context)).into(imageView);
    }

    private static String transformUrl(String url) {
        String[] arrs = url.split("://");
        String head = arrs[0];
        String body = arrs[1];
        switch (head) {
            case "oss":
                return Constant.Base.BASE_ONLINE_URL + body;
            default:
                return Constant.Base.BASE_IMG_URL + body;
        }
    }

    /**
     * 获取适用于Fresco本地图片资源Url
     */
    public static String getFrescoLocalResUrl(Context mContext, int resId) {
        return "res://" + mContext.getPackageName() + "/" + resId;
    }

    /**
     * 获取适用于Fresco本地文件图片资源
     */
    public static String getFrescoLocalFile(File file) {
        return "file://" + file.getPath();
    }

}
