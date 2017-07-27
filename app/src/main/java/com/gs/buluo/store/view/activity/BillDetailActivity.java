package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.BillEntity;
import com.gs.buluo.store.bean.WithdrawBill;
import com.gs.buluo.store.utils.GlideUtils;
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
    TextView tvMoney;
    @Bind(R.id.bill_time)
    TextView tvTime;
    @Bind(R.id.bill_status)
    TextView tvStatus;
    @Bind(R.id.bill_logo)
    ImageView ivLogo;

    @Bind(R.id.bill_detail_money)
    MoneyTextView moneyTextView;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        BillEntity entity = (BillEntity) getIntent().getSerializableExtra(Constant.BILL);
        WithdrawBill bill = getIntent().getParcelableExtra(Constant.WITHDRAW_BILL);
        if (entity != null) {          //账单
            tvNumber.setText(entity.id);
            String amount = entity.amount;
            moneyTextView.setMoneyText(amount);
            if (amount.contains("-")) {
                tvMoney.setText("支出" + amount.substring(1, amount.length()));
            } else {
                tvMoney.setText("收入" + amount);
            }
            long createTime = Long.parseLong(entity.createTime);
            Date date = new Date(createTime);
            tvTime.setText(TribeDateUtils.dateFormat9(date));
            String url = Constant.Base.BASE_ONLINE_URL + entity.anotherId + "/icon.jpg";
            GlideUtils.loadImage(this, url, ivLogo, true);
            tvStatus.setText(entity.title);
        } else {             //提现记录
            tvNumber.setText(bill.id);
            String amount = bill.amount;
            moneyTextView.setMoneyText(amount);
            if (amount.contains("-")) {
                tvMoney.setText("支出" + amount.substring(1, amount.length()));
            } else {
                tvMoney.setText("收入" + amount);
            }

            tvStatus.setText(bill.status.status);
            GlideUtils.loadImage(this, TribeApplication.getInstance().getUserInfo().getLogo(), ivLogo, true);
            Date date = new Date(bill.createTime);
            tvTime.setText(TribeDateUtils.dateFormat9(date));
        }

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bill_detail;
    }
}
