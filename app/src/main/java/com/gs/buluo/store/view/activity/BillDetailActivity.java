package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.BillEntity;
import com.gs.buluo.store.bean.WithdrawBill;
import com.gs.buluo.store.utils.TribeDateUtils;

import java.util.Date;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/21.
 */
public class BillDetailActivity extends BaseActivity {
    @Bind(R.id.bill_detail_number)
    TextView mNumber;
    @Bind(R.id.bill_detail_money)
    TextView mMoney;
    @Bind(R.id.bill_detail_name)
    TextView mName;
    @Bind(R.id.bill_detail_time)
    TextView mTime;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        BillEntity entity = (BillEntity) getIntent().getSerializableExtra(Constant.BILL);
        WithdrawBill bill = getIntent().getParcelableExtra(Constant.WITHDRAW_BILL);
        if (entity!=null){          //账单
            mNumber.setText(entity.id);
            String amount = entity.amount;
            if (amount.contains("-")) {
                mMoney.setText("支出" + amount.substring(1, amount.length()));
            } else {
                mMoney.setText("收入" + amount);
            }

            mName.setText(entity.annotherAccountId);

            long createTime = Long.parseLong(entity.createTime);
            Date date = new Date(createTime);
            mTime.setText(TribeDateUtils.dateFormat9(date));
        }else {             //提现记录
            mNumber.setText(bill.id);
            String amount = bill.amount;
            if (amount.contains("-")) {
                mMoney.setText("支出" + amount.substring(1, amount.length()));
            } else {
                mMoney.setText("收入" + amount);
            }

            mName.setText(bill.status.toString());

            Date date = new Date(bill.createTime);
            mTime.setText(TribeDateUtils.dateFormat9(date));
        }

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bill_detail;
    }
}
