package com.gs.buluo.store.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.store.R;
import com.gs.buluo.store.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by hjn on 2017/7/12.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainViewHolder> {
    private ArrayList<String> datas = null;
    private Activity mCtx;


    public MainListAdapter(ArrayList<String> datas, Activity context) {
        this.datas = datas;
        mCtx = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_list_item, viewGroup, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.mTextView.setText(datas.get(position));
        holder.actionView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        holder.actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] arr = {0, 0};
                v.getLocationOnScreen(arr);
                showPoP(v, arr[1]);
            }
        });
    }

    private void showPoP(View v, int i) {
        View view = View.inflate(mCtx, R.layout.item_pop, null);
        PopupWindow popupWindow = new PopupWindow(CommonUtils.getScreenWidth(mCtx) - 40, DensityUtils.dip2px(mCtx, 60));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        if (i<CommonUtils.getScreenHeight(mCtx)/2){
            view.setBackgroundResource(R.mipmap.pop_bg_bottom);
            popupWindow.setContentView(view);
            popupWindow.showAtLocation(v, Gravity.TOP, 0, i + DensityUtils.dip2px(mCtx, 50));
        }else {
            view.setBackgroundResource(R.mipmap.pop_bg_top);
            popupWindow.setContentView(view);
            popupWindow.showAtLocation(v, Gravity.TOP, 0, i - DensityUtils.dip2px(mCtx, 50));
        }


        CommonUtils.backgroundAlpha(mCtx, 0.6f);
        popupWindow.setAnimationStyle(R.style.AroundDialogAnimation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.backgroundAlpha(mCtx, 1f);
            }
        });

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public View actionView;

        public MainViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.textView10);
            actionView = view.findViewById(R.id.item_action);
        }
    }

}
