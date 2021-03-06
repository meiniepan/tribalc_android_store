package com.gs.buluo.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.WithdrawBill;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.TribeDateUtils;
import com.gs.buluo.store.view.activity.BillDetailActivity;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hjn on 2016/11/17.
 */
public class WithdrawListAdapter extends RecyclerAdapter<WithdrawBill> {
    private long today;
    private int lastMonth = -1;  //上一个有变化的 月份
    Context context;

    public WithdrawListAdapter(Context context, List<WithdrawBill> list) {
        super(context, list);
        this.context = context;
        today = System.currentTimeMillis();
    }

    @Override
    public BaseViewHolder<WithdrawBill> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new BillHolder(parent, R.layout.bill_list_item);
    }

    class BillHolder extends BaseViewHolder<WithdrawBill> {
        TextView week;
        TextView time;
        TextView money;
        TextView detail;
        TextView month;
        ImageView icon;

        public BillHolder(ViewGroup itemView, int res) {
            super(itemView, res);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            week = findViewById(R.id.bill_week);
            time = findViewById(R.id.bill_time);
            money = findViewById(R.id.bill_money);
            detail = findViewById(R.id.bill_detail);
            month = findViewById(R.id.bill_item_month);
            icon = findViewById(R.id.bill_icon);
        }

        @Override
        public void setData(WithdrawBill entity) {
            super.setData(entity);
            Date date = new Date(entity.time);
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            int w = instance.get(Calendar.DAY_OF_WEEK);
            String s = TribeDateUtils.dateFormat5(date);
            String we = CommonUtils.getWeekFromCalendar(w);
            week.setText(we);

            if (TribeDateUtils.getTimeIntervalByDay(entity.time, today) < 1) {
                week.setText(R.string.today);
                time.setText(TribeDateUtils.dateFormat6(date));
            } else {
                time.setText(TribeDateUtils.dateFormat8(date));
            }

            money.setText(entity.amount);
            detail.setText(entity.status == null ? "" : entity.status.status);
            String newMonth = s.split("-")[1];
            month.setText(newMonth + "月");
            if (!(Integer.parseInt(newMonth) == lastMonth)) {
                month.setVisibility(View.VISIBLE);
                month.setText(newMonth + "月");
                lastMonth = Integer.parseInt(newMonth);
            } else {
                month.setVisibility(View.GONE);
            }
            GlideUtils.loadImage(getContext(), TribeApplication.getInstance().getUserInfo().getLogo(), icon, true);
        }

        @Override
        public void onItemViewClick(WithdrawBill entity) {
            super.onItemViewClick(entity);
            Intent intent = new Intent(context, BillDetailActivity.class);
            intent.putExtra(Constant.WITHDRAW_BILL, entity);
            context.startActivity(intent);
        }
    }
}
