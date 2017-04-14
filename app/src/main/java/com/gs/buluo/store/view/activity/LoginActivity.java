package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.presenter.LoginPresenter;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.view.impl.ILoginView;
import com.gs.buluo.store.utils.ToastUtils;

import java.util.HashMap;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, ILoginView {
    @Bind(R.id.login_username)
    EditText et_phone;
    @Bind(R.id.login_verify)
    EditText et_verify;
    @Bind(R.id.login_send_verify)
    Button reg_send;
    private HashMap<String, String> params;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.login_back).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.login_send_verify).setOnClickListener(this);
        findView(R.id.login_protocol).setOnClickListener(this);
        if (getIntent().getBooleanExtra(Constant.RE_LOGIN, false)) { //登录冲突
            ToastUtils.ToastMessage(getCtx(), getString(R.string.login_again));
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onClick(View v) {
        String phone = et_phone.getText().toString().trim();
        switch (v.getId()) {
            case R.id.login_back:
                finish();
                break;
            case R.id.login_send_verify:
                et_verify.requestFocus();
                if (!CommonUtils.checkPhone("86", phone, this)) return;
                ((LoginPresenter) mPresenter).doVerify(phone);
                startCounter();
                break;
            case R.id.login:
                if (!CommonUtils.checkPhone("86", phone, this)) return;
                params = new HashMap<>();
                params.put(Constant.PHONE, phone);
                params.put(Constant.VERIFICATION, et_verify.getText().toString().trim());
                ((LoginPresenter) mPresenter).doLogin(params);
                break;
            case R.id.login_protocol:
                startActivity(new Intent(getCtx(),WebActivity.class));
                break;
        }
    }

    @Override
    public void dealWithIdentify(int res) {
        switch (res) {
            case 202:

                break;
            case 400:
                ToastUtils.ToastMessage(this, getString(R.string.wrong_number));
                reg_send.setText("获取验证码");
                reg_send.setClickable(true);
                break;
        }
    }

    private void startCounter() {
        reg_send.setText("60s");
        reg_send.setClickable(false);
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                reg_send.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                reg_send.setText("获取验证码");
                reg_send.setClickable(true);
            }
        }.start();
    }

    @Override
    public void showError(int res) {
        dismissDialog();
        ToastUtils.ToastMessage(this, res);
    }

    @Override
    public void loginSuccess() {
        dismissDialog();
        finish();
    }
}
