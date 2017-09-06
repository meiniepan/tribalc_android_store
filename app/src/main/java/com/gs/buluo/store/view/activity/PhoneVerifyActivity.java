package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gs.buluo.store.R;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.LoginPresenter;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.view.impl.ILoginView;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/9.
 */
public class PhoneVerifyActivity extends BaseActivity implements ILoginView {
    @Bind(R.id.bind_edit_phone)
    EditText mPhone;
    private String phone;

    @Override
    protected void bindView(Bundle savedInstanceState) {
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_update_phone1;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void showError(int res,String message) {
        ToastUtils.ToastMessage(this, getString(R.string.connect_fail));
    }

    @Override
    public void loginSuccess() {
    }

    @Override
    public void dealWithIdentify(int res) {
        switch (res) {
            case 202:
                Intent intent = new Intent(PhoneVerifyActivity.this, PhoneVerifyActivity2.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
                break;
            case 400:
                ToastUtils.ToastMessage(this, getString(R.string.wrong_number));
                break;
        }
    }
}
