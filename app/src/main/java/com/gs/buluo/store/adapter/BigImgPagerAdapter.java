package com.gs.buluo.store.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.view.widget.DoubleScaleImageView;

import java.util.List;

/**
 * Created by hjn on 2016/11/1.
 */
public class BigImgPagerAdapter extends PagerAdapter {
    List<String> lists;
    Context mAct;

    public BigImgPagerAdapter(Context activity, List<String> mList) {
        lists = mList;
        this.mAct = activity;
    }

    public int getCount() {
        return lists.size();
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        DoubleScaleImageView imageView =new DoubleScaleImageView(mAct);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        GlideUtils.loadImage(mAct,lists.get(position),imageView);
        container.addView(imageView);
        return imageView;
    }

    public boolean isViewFromObject(View view, Object object) {
        if (view == object) {
            return true;
        } else {
            return false;
        }
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        object = null;
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }
}
