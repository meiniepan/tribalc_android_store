package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.presenter.LoginPresenter;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.view.impl.ILoginView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by hjn on 2016/11/3.
 */
public class ThirdLoginActivity extends BaseActivity implements View.OnClickListener, ILoginView {
    @Bind(R.id.login_username)
    EditText et_phone;
    @Bind(R.id.login_verify)
    EditText et_verify;
    @Bind(R.id.login_send_verify)
    Button reg_send;
    //wbn
    @Bind(R.id.login)
    Button bt_login;

    private HashMap<String, String> params;
    private String wxCode;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        wxCode = getIntent().getStringExtra(Constant.WX_CODE);
        findViewById(R.id.login_back).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.login_send_verify).setOnClickListener(this);
        findViewById(R.id.login_protocol).setOnClickListener(this);
        if (getIntent().getBooleanExtra(Constant.RE_LOGIN, false)) { //登录冲突
            ToastUtils.ToastMessage(getCtx(), getString(R.string.login_again));
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bind_login;
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
                if (!CommonUtils.checkPhone("86", phone, this)) return;
                startCounter();
                ((LoginPresenter) mPresenter).doVerify(phone);
                et_verify.requestFocus();
                break;
            case R.id.login:
                if (!CommonUtils.checkPhone("86", phone, this)) return;
                showLoadingDialog();
                params = new HashMap<>();
                params.put(Constant.PHONE, phone);
                params.put(Constant.VERIFICATION, et_verify.getText().toString().trim());
                ((LoginPresenter) mPresenter).doThirdLogin(params, wxCode);
                break;
            case R.id.login_protocol:
                startActivity(new Intent(getCtx(), WebActivity.class));
                break;
        }
    }

    private Subscriber<Long> subscriber;

    @Override
    public void dealWithIdentify(int res) {
        switch (res) {
            case 504:
                ToastUtils.ToastMessage(this, getString(R.string.frequency_code));
                break;
            case 400:
                ToastUtils.ToastMessage(this, getString(R.string.wrong_number));
                subscriber.unsubscribe();
                reg_send.setText("获取验证码");
                reg_send.setClickable(true);
                break;
            case 401:
                ToastUtils.ToastMessage(this, R.string.wrong_verify);
                break;
            default:
                ToastUtils.ToastMessage(this, "登录失败，错误码" + res);
                break;
        }
    }

    private void startCounter() {
        final int startTime = 60;
        reg_send.setClickable(false);
        subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                reg_send.setText("获取验证码");
                reg_send.setClickable(true);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Long aLong) {
                reg_send.setText(aLong + "秒");
            }
        };
        Observable.interval(0, 1, TimeUnit.SECONDS).take(startTime + 1)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long time) {
                        return startTime - time;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void showError(int res, String message) {
        ToastUtils.ToastMessage(this, res);
    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        AppManager.getAppManager().finishActivity(LoginActivity.class);
    }
}
