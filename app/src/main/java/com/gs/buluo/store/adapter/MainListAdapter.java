package com.gs.buluo.store.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.HomeMessage;
import com.gs.buluo.store.presenter.MainPresenter;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.GlideUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hjn on 2017/7/12.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainViewHolder> {
    private ArrayList<HomeMessage> datas = null;
    private Activity mCtx;
    private MainPresenter presenter;
    private final Calendar calendar;
    private final int nowDayOfYear;


    public MainListAdapter(ArrayList<HomeMessage> datas, Activity context) {
        this.datas = datas;
        mCtx = context;
        calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        nowDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_list_item, viewGroup, false);
        return new MainViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        final HomeMessage homeMessage = datas.get(position);
        holder.tvMoney.setText(homeMessage.messageBody.body);
        holder.tvDesc.setText(homeMessage.messageBody.description);
        if (homeMessage.messageBody.avatar != null)
            GlideUtils.loadImage(mCtx, homeMessage.messageBody.avatar, holder.ivHead);
        if (homeMessage.messageBody.applicationTime != 0)
            holder.tvDate.setText(TribeDateUtils.dateFormat5(new Date(homeMessage.messageBody.applicationTime)));
        holder.tvOwner.setText(homeMessage.messageBody.homeMessageType.homeMessageTypeEnum.owner);
        if (homeMessage.createTime != 0) {
            calendar.setTime(new Date(homeMessage.createTime));
            int day = calendar.get(Calendar.DAY_OF_YEAR);
            if (day == nowDayOfYear) {
                holder.tvCreateDate.setText(TribeDateUtils.dateFormat6(new Date(homeMessage.createTime)));
            } else {
                holder.tvCreateDate.setText(TribeDateUtils.dateFormat3(new Date(homeMessage.createTime)));
            }
        }

        holder.actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] arr = {0, 0};
                v.getLocationOnScreen(arr);
                showPoP(v, arr[1], homeMessage);
            }
        });
    }

    private void showPoP(View v, int i, final HomeMessage homeMessage) {
        View view = View.inflate(mCtx, R.layout.item_pop, null);
        final PopupWindow popupWindow = new PopupWindow(CommonUtils.getScreenWidth(mCtx) - 40, DensityUtils.dip2px(mCtx, 80));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.MainPopupWindowAnimationScale);
        if (i < CommonUtils.getScreenHeight(mCtx) / 2) {
            view.setBackgroundResource(R.mipmap.pop_bg_bottom);
            popupWindow.setContentView(view);
            popupWindow.showAtLocation(v, Gravity.TOP, 0, i + DensityUtils.dip2px(mCtx, 50));
        } else {
            view.setBackgroundResource(R.mipmap.pop_bg_top);
            popupWindow.setContentView(view);
            popupWindow.showAtLocation(v, Gravity.TOP, 0, i - DensityUtils.dip2px(mCtx, 50));
        }
        CommonUtils.backgroundAlpha(mCtx, 0.6f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.backgroundAlpha(mCtx, 1f);
            }
        });

        view.findViewById(R.id.main_item_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                presenter.deleteMessage(homeMessage);
            }
        });
        view.findViewById(R.id.main_item_ignore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                presenter.ignoreMessage(homeMessage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        TextView tvMoney;
        TextView tvOwner;
        TextView tvDesc;
        TextView tvDate;
        TextView tvCreateDate;
        ImageView ivHead;
        View actionView;

        MainViewHolder(View view) {
            super(view);
            tvMoney = (TextView) view.findViewById(R.id.main_item_money);
            tvCreateDate = (TextView) view.findViewById(R.id.main_item_create_date);
            tvOwner = (TextView) view.findViewById(R.id.main_item_owner);
            tvDesc = (TextView) view.findViewById(R.id.main_item_description);
            tvDate = (TextView) view.findViewById(R.id.main_item_date);
            ivHead = (ImageView) view.findViewById(R.id.main_item_logo);
            actionView = view.findViewById(R.id.main_item_action);
        }
    }

}
