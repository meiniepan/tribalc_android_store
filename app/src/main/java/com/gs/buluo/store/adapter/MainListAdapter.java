package com.gs.buluo.store.adapter;

import android.app.Activity;
import android.content.Intent;
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
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.HomeMessage;
import com.gs.buluo.store.bean.HomeMessageEnum;
import com.gs.buluo.store.presenter.MainPresenter;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.view.activity.BillDetailActivity;
import com.gs.buluo.store.view.widget.recyclerHelper.BaseHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.gs.buluo.store.bean.HomeMessageEnum.TENANT_RECHARGE;
import static com.gs.buluo.store.bean.HomeMessageEnum.TENANT_WITHDRAW;

/**
 * Created by hjn on 2017/7/12.
 */

public class MainListAdapter extends RecyclerView.Adapter<BaseHolder> {
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
    public BaseHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        BaseHolder viewHolder;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_list_item, viewGroup, false);
                viewHolder = new MainViewHolder(view);
                return viewHolder;
            case 2:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_list_item, viewGroup, false);
                viewHolder = new RegisterMessageHolder(view);
                return viewHolder;
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_list_item, viewGroup, false);
                viewHolder = new MainViewHolder(view);
                return viewHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        HomeMessageEnum homeMessageTypeEnum = datas.get(position).messageBody.homeMessageType.homeMessageTypeEnum;
        if (homeMessageTypeEnum == null) return 2;
        switch (homeMessageTypeEnum) {
            case TENANT_RECHARGE:
            case TENANT_WITHDRAW:
                return 1;
            case ACCOUNT_REGISTER:
                return 2;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        final HomeMessage homeMessage = datas.get(position);
        HomeMessageEnum homeMessageTypeEnum = homeMessage.messageBody.homeMessageType.homeMessageTypeEnum;
        if (homeMessageTypeEnum == null) { //there is no supported model, pick default one
            setRegisterHolder(homeMessage, (RegisterMessageHolder) holder);
        } else if (holder instanceof MainViewHolder) {
            setMainMessageHolder(homeMessage, (MainViewHolder) holder);
        } else if (holder instanceof RegisterMessageHolder) {
            setRegisterHolder(homeMessage, (RegisterMessageHolder) holder);
        }
    }

    private void setRegisterHolder(final HomeMessage homeMessage, RegisterMessageHolder holder) {
        if (homeMessage.createTime != 0) {
            calendar.setTime(new Date(homeMessage.createTime));
            int day = calendar.get(Calendar.DAY_OF_YEAR);
            if (day == nowDayOfYear) {
                holder.setText(R.id.main_item_create_date, TribeDateUtils.dateFormat6(new Date(homeMessage.createTime)));
            } else {
                holder.setText(R.id.main_item_create_date, TribeDateUtils.dateFormat3(new Date(homeMessage.createTime)));
            }
        }
        if (homeMessage.messageBody.homeMessageType.homeMessageTypeEnum != null)
            holder.setText(R.id.main_item_description, homeMessage.messageBody.body);
        holder.setText(R.id.main_item_owner, homeMessage.messageBody.homeMessageType.homeMessageTypeCategory);
        holder.getView(R.id.main_item_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] arr = {0, 0};
                v.getLocationOnScreen(arr);
                showPoP(v, arr[1], homeMessage);
            }
        });
    }

    private void setMainMessageHolder(final HomeMessage homeMessage, MainViewHolder holder) {
        holder.tvMoney.setText(homeMessage.messageBody.body);
        holder.tvDesc.setText(homeMessage.messageBody.description);
        if (homeMessage.messageBody.avatar != null) {
            GlideUtils.loadImage(mCtx, "oss://" + homeMessage.messageBody.avatar + "/icon.jpg", holder.ivHead, true);
        }
        if (homeMessage.messageBody.homeMessageType.homeMessageTypeEnum == TENANT_RECHARGE) {
            holder.tvDate.setText("当日累计交易" + homeMessage.messageBody.dayTradingNumber + "笔，累计收入" + homeMessage.messageBody.dayTradingAmount + "元");
        } else if (homeMessage.messageBody.applicationTime != 0) {
            holder.tvDate.setText(TribeDateUtils.dateFormat5(new Date(homeMessage.messageBody.applicationTime)));
        }
        holder.tvOwner.setText(homeMessage.messageBody.homeMessageType.homeMessageTypeCategory);
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
        holder.linkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLink(homeMessage);
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

    private void setLink(HomeMessage message) {
        Intent intent = new Intent();
        if (message.messageBody.homeMessageType.homeMessageTypeEnum == TENANT_RECHARGE) {
            intent.setClass(mCtx, BillDetailActivity.class);
            intent.putExtra(Constant.BILL_ID, message.messageBody.referenceId);
            mCtx.startActivity(intent);
        } else if (message.messageBody.homeMessageType.homeMessageTypeEnum == TENANT_WITHDRAW) {
            intent.setClass(mCtx, BillDetailActivity.class);
            intent.putExtra(Constant.WITHDRAW_ID, message.messageBody.referenceId);
            mCtx.startActivity(intent);
        }
    }

    private class MainViewHolder extends BaseHolder {
        TextView tvMoney;
        TextView tvOwner;
        TextView tvDesc;
        TextView tvDate;
        TextView tvCreateDate;
        ImageView ivHead;
        View actionView;
        View linkView;

        MainViewHolder(View view) {
            super(view);
            tvMoney = (TextView) view.findViewById(R.id.main_item_money);
            tvCreateDate = (TextView) view.findViewById(R.id.main_item_create_date);
            tvOwner = (TextView) view.findViewById(R.id.main_item_owner);
            tvDesc = (TextView) view.findViewById(R.id.main_item_description);
            tvDate = (TextView) view.findViewById(R.id.main_item_date);
            ivHead = (ImageView) view.findViewById(R.id.main_item_head);
            actionView = view.findViewById(R.id.main_item_action);
            linkView = view.findViewById(R.id.main_item_link);
        }
    }

    private class RegisterMessageHolder extends BaseHolder {
        public RegisterMessageHolder(View view) {
            super(view);
        }
    }

}
