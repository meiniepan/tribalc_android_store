package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.BillEntity;
import com.gs.buluo.store.bean.WithdrawBill;
import com.gs.buluo.store.utils.TribeDateUtils;
import com.gs.buluo.store.view.widget.MoneyTextView;

import java.util.Date;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/21.
 */
public class BillDetailActivity extends BaseActivity {
    @Bind(R.id.bill_number)
    TextView tvNumber;
    @Bind(R.id.bill_money)
    MoneyTextView tvMoney;
    @Bind(R.id.bill_time)
    TextView tvTime;
    @Bind(R.id.bill_status)
    TextView tvStatus;
    @Bind(R.id.bill_logo)
    ImageView ivLogo;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        BillEntity entity = (BillEntity) getIntent().getSerializableExtra(Constant.BILL);
        WithdrawBill bill = getIntent().getParcelableExtra(Constant.WITHDRAW_BILL);
        if (entity != null) {          //账单
            tvNumber.setText(entity.id);
            String amount = entity.amount;
            if (amount.contains("-")) {
                tvMoney.setMoneyText("支出" + amount.substring(1, amount.length()));
            } else {
                tvMoney.setMoneyText("收入" + amount);
            }

            tvStatus.setText(entity.annotherAccountId);

            long createTime = Long.parseLong(entity.createTime);
            Date date = new Date(createTime);
            tvTime.setText(TribeDateUtils.dateFormat9(date));
        } else {             //提现记录
            tvNumber.setText(bill.id);
            String amount = bill.amount;
            if (amount.contains("-")) {
                tvMoney.setMoneyText("支出" + amount.substring(1, amount.length()));
            } else {
                tvMoney.setMoneyText("收入" + amount);
            }

            tvStatus.setText(bill.status.toString());

            Date date = new Date(bill.createTime);
            tvTime.setText(TribeDateUtils.dateFormat9(date));
        }

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bill_detail;
    }
}
