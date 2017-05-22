package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.BankCard;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.network.MoneyApis;
import com.gs.buluo.store.network.TribeRetrofit;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/23.
 */
public class AddBankCardActivity extends BaseActivity {
    @Bind(R.id.card_add_bank_name)
    TextView etBankName;
    @Bind(R.id.card_add_bank_num)
    EditText etNum;
    @Bind(R.id.card_add_name)
    EditText etName;
    @Bind(R.id.card_add_phone)
    EditText etPhone;
    @Bind(R.id.card_add_verify)
    EditText etVerify;

    @Bind(R.id.card_send_verify)
    TextView sendVerify;
    private String cardId;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.card_bind_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.card_add_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddCard();
            }
        });
        findViewById(R.id.card_add_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getCtx(), BankPickActivity.class), Constant.ForIntent.REQUEST_CODE);
            }
        });
        sendVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerifyCode(etPhone.getText().toString().trim());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String name = data.getStringExtra(Constant.ForIntent.FLAG);
            etBankName.setText(name);
        }
    }

    private void sendVerifyCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.ToastMessage(this, R.string.phone_not_empty);
            return;
        }
        final BankCard card = new BankCard();
        card.bankCardNum = etNum.getText().toString().trim();
        card.bankName = etBankName.getText().toString().trim();
        card.userName = etName.getText().toString().trim();
        card.phone = etPhone.getText().toString().trim();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                prepareAddBankCard(TribeApplication.getInstance().getUserInfo().getId(), card).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BankCard>>() {
                    @Override
                    public void onNext(BaseResponse<BankCard> bankCardBaseResponse) {
                        dealWithIdentify(bankCardBaseResponse.code);
                        BankCard data = bankCardBaseResponse.data;
                        cardId = data.id;
                    }

                    @Override
                    public void onFail(ApiException e) {
                        dealWithIdentify(e.getCode());
                    }
                });
    }


    private void dealWithIdentify(int code) {
        switch (code) {
            case 201:
                sendVerify.setText("60s");
                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        sendVerify.setClickable(false);
                        sendVerify.setText(millisUntilFinished / 1000 + "秒");
                    }

                    @Override
                    public void onFinish() {
                        sendVerify.setText("获取验证码");
                        sendVerify.setClickable(true);
                    }
                }.start();
                break;
            case 400:
                ToastUtils.ToastMessage(getCtx(),R.string.wrong_number);
                break;
            case 403:
                ToastUtils.ToastMessage(getCtx(),getString(R.string.bank_card_forbidden));
                break;
            case 409:
                ToastUtils.ToastMessage(getCtx(),getString(R.string.bank_card_binded));
                break;
            case 412:
                ToastUtils.ToastMessage(getCtx(),getString(R.string.not_auth));
                break;
            case 424:
                ToastUtils.ToastMessage(getCtx(),getString(R.string.not_support_card));
            default:
                ToastUtils.ToastMessage(getCtx(),getString(R.string.connect_error)+code);
                break;

        }
    }

    private void doAddCard() {
        showLoadingDialog();
        String vCode = etVerify.getText().toString().trim();
        ValueRequestBody verifyBody = new ValueRequestBody(vCode);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                uploadVerify(TribeApplication.getInstance().getUserInfo().getId(), cardId, verifyBody).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> codeResponseBaseResponse) {
                        dismissDialog();
                        startActivity(new Intent(getCtx(), BankCardActivity.class));
                    }

                    @Override
                    public void onFail(ApiException e) {
                        dismissDialog();
                        if (e.getCode()==401) ToastUtils.ToastMessage(getCtx(), R.string.wrong_verify);
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_bank_card;
    }
}
