package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.BillEntity;
import com.gs.buluo.store.bean.WithdrawBill;
import com.gs.buluo.store.network.MoneyApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.TribeDateUtils;
import com.gs.buluo.store.view.widget.MoneyTextView;

import java.util.Date;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/21.
 */
public class BillDetailActivity extends BaseActivity {
    @BindView(R.id.bill_number)
    TextView tvNumber;
    @BindView(R.id.bill_money)
    TextView tvMoney;
    @BindView(R.id.bill_time)
    TextView tvTime;
    @BindView(R.id.bill_status)
    TextView tvStatus;
    @BindView(R.id.bill_logo)
    ImageView ivLogo;

    @BindView(R.id.bill_detail_money)
    MoneyTextView moneyTextView;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        BillEntity entity = (BillEntity) getIntent().getSerializableExtra(Constant.BILL);
        WithdrawBill bill = getIntent().getParcelableExtra(Constant.WITHDRAW_BILL);
        String billId = getIntent().getStringExtra(Constant.BILL_ID);
        String withdrawId = getIntent().getStringExtra(Constant.WITHDRAW_ID);
        if (entity != null) {          //账单
            setBillData(entity);
        } else if (bill != null) {             //提现记录
            setWithdrawData(bill);
        } else if (billId != null) {
            getBillDetail(billId);
        } else if (withdrawId != null) {
            getWithdrawBillDetail(withdrawId);
        }
    }

    private void getBillDetail(String billId) {
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getBillDetail(TribeApplication.getInstance().getUserInfo().getId(), billId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BillEntity>>() {
                    @Override
                    public void onNext(BaseResponse<BillEntity> withdrawBillBaseResponse) {
                        setBillData(withdrawBillBaseResponse.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                    }
                });
    }

    private void setBillData(BillEntity entity) {
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
    }

    private void setWithdrawData(WithdrawBill bill) {
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
        Date date = new Date(bill.time);
        tvTime.setText(TribeDateUtils.dateFormat9(date));
    }

    private void getWithdrawBillDetail(String billId) {
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getWithdrawDetail(TribeApplication.getInstance().getUserInfo().getId(), billId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WithdrawBill>>() {
                    @Override
                    public void onNext(BaseResponse<WithdrawBill> withdrawBillBaseResponse) {
                        setWithdrawData(withdrawBillBaseResponse.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bill_detail;
    }
}
