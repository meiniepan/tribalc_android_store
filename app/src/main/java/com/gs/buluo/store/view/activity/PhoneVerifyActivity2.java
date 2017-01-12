package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.utils.AppManager;
import com.gs.buluo.store.utils.ToastUtils;

import butterknife.Bind;
import retrofit2.Response;


/**
 * Created by hjn on 2016/11/9.
 */
public class PhoneVerifyActivity2 extends BaseActivity{
    @Bind(R.id.verify_phone2)
    TextView mPhone;
    @Bind(R.id.bind_verify)
    EditText mVerify;
    @Bind(R.id.second_counts)
    TextView reg_send;

    @Bind(R.id.phone_code_title)
    TextView title;
    private String phone;
    private boolean fromPwd;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        fromPwd = getIntent().getBooleanExtra("for_security", false);
        if (fromPwd){
            phone=new StoreInfoDao().findFirst().getPhone();
            title.setText("安全校验");
            mPhone.setText(phone);
        }else {
            phone = getIntent().getStringExtra("phone");
            mPhone.setText(phone);
        }

        findViewById(R.id.phone_bind_next_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVerify();
            }
        });

        findViewById(R.id.phone2_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        doVerify();
    }

    private void doVerify() {
        new MainModel().doVerify(phone, new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                dealWithIdentify();
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                reg_send.setText("获取验证码");
                findViewById(R.id.text_behind).setVisibility(View.GONE);
                ToastUtils.ToastMessage(PhoneVerifyActivity2.this, R.string.connect_fail);
            }
        });
    }

    private void dealWithIdentify() {
        new CountDownTimer(60000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                reg_send.setClickable(false);
                reg_send.setText(millisUntilFinished/1000+"s");
            }
            @Override
            public void onFinish() {
                reg_send.setText("获取验证码");
                findViewById(R.id.text_behind).setVisibility(View.GONE);
                reg_send.setClickable(true);
                reg_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doVerify();
                    }
                });
            }
        }.start();
    }

    private void checkVerify() {
        String verify=mVerify.getText().toString().trim();
        if (TextUtils.isEmpty(verify)){
            ToastUtils.ToastMessage(PhoneVerifyActivity2.this,R.string.verify_not_empty);
            return;
        }
        if (fromPwd){ //忘记密码
            Intent intent = new Intent(this, UpdateWalletPwdActivity.class);
            intent.putExtra(Constant.VCODE,verify);
            startActivity(intent);
            AppManager.getAppManager().finishActivity(ConfirmActivity.class);
            finish();
        }else {
            new MainModel().updatePhone(phone, verify, new TribeCallback<CodeResponse>() {
                @Override
                public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                    StoreInfoDao dao = new StoreInfoDao();
                    StoreInfo entity = dao.findFirst();
                    entity.setPhone(phone);
                    dao.update(entity);
                    finish();
                    AppManager.getAppManager().finishActivity(PhoneVerifyActivity.class);
                }

                @Override
                public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                    if (responseCode==401){
                        ToastUtils.ToastMessage(PhoneVerifyActivity2.this,R.string.wrong_verify);
                    }else {
                        ToastUtils.ToastMessage(PhoneVerifyActivity2.this,R.string.connect_fail);
                    }
                }
            });

        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_phone2;
    }

}
