package com.gs.buluo.store.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.BigImgPagerAdapter;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by hjn on 2017/1/5.
 */

public class BigImagePanel extends Dialog {

    private ViewPager pager;

    public BigImagePanel(Context context, List<String> list) {
        super(context, R.style.big_img_dialog);
        initView(list);
    }

    private void initView(final List<String> list) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.big_img_board, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        pager = (ViewPager) rootView.findViewById(R.id.big_img_pager);
        pager.setAdapter(new BigImgPagerAdapter(getContext(),list));

        if (list.size()==1)return;
        initPagerPoint(list, rootView);
    }

    private void initPagerPoint(final List<String> list, View rootView) {
        final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.point_parent);
        for (int i = 0; i < list.size(); i++) {
            View imageView = new View(getContext());
            if (i==0){
                imageView.setBackgroundResource(R.drawable.pager_point_selected);
            }else {
                imageView.setBackgroundResource(R.drawable.pager_point);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtils.dip2px(getContext(),10),DensityUtils.dip2px(getContext(),10));
            layoutParams.setMargins(DensityUtils.dip2px(getContext(),16),0,DensityUtils.dip2px(getContext(),16),0);
            imageView.setLayoutParams(layoutParams);
            linearLayout.addView(imageView);
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int pos =0;pos<list.size();pos++){
                    linearLayout.getChildAt(pos).setBackgroundResource(R.drawable.pager_point);
                }
                linearLayout.getChildAt(position).setBackgroundResource(R.drawable.pager_point_selected);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setPos(int pos) {
        pager.setCurrentItem(pos);
    }
}
