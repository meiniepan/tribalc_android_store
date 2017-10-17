package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gs.buluo.common.UpdateEvent;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.presenter.LoginPresenter;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.WXUtils;
import com.gs.buluo.store.view.impl.ILoginView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, ILoginView {
    @BindView(R.id.login_username)
    EditText et_phone;
    @BindView(R.id.login_verify)
    EditText et_verify;
    @BindView(R.id.login_send_verify)
    Button reg_send;
    private HashMap<String, String> params;
    private Subscriber<Long> subscriber;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        setBarColor(R.color.custom_tint2);
        findViewById(R.id.login_back).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.login_send_verify).setOnClickListener(this);
        findViewById(R.id.wx_login).setOnClickListener(this);
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
                showLoadingDialog();
                ((LoginPresenter) mPresenter).doLogin(params);
                break;
            case R.id.login_protocol:
                startActivity(new Intent(getCtx(), WebActivity.class));
                break;
            case R.id.wx_login:
                WXUtils.getInstance().doLogin();
                break;
        }
    }

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
            case 404:
                ToastUtils.ToastMessage(this, R.string.store_not_exist);
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
    public void actSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
