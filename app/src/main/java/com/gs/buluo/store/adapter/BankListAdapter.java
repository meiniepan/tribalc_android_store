package com.gs.buluo.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.BankCard;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.type;

/**
 * Created by fs on 2016/12/22.
 */

public class BankListAdapter extends BaseAdapter {

    private final Context mContext;
    private LayoutInflater mInflater;
    private List<BankCard> mList = new ArrayList<>();

    public BankListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<BankCard> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pick_bank, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        BankCard bankCard = mList.get(position);
        holder.tvBank.setText(bankCard.bankName);
        if (bankCard.maxPaymentAmount == 0) {
            holder.tvSign.setVisibility(View.VISIBLE);
            bankCard.bindType = BankCard.BankCardBindTypeEnum.WITHDRAW;
        } else {
            holder.tvSign.setVisibility(View.GONE);
            bankCard.bindType = BankCard.BankCardBindTypeEnum.NORMAL;
        }
        return convertView;
    }

    public static class ViewHolder {

        public TextView tvBank;
        public TextView tvSign;

        public ViewHolder(View view) {
            tvBank = (TextView) view.findViewById(R.id.bank_name);
            tvSign = (TextView) view.findViewById(R.id.bank_name_sign);
        }

    }

}
