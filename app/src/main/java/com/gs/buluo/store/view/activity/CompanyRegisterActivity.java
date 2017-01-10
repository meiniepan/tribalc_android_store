package com.gs.buluo.store.view.activity;

import android.os.Bundle;

import com.gs.buluo.store.R;
import com.gs.buluo.store.presenter.BasePresenter;

/**
 * Created by hjn on 2016/11/7.
 */
public class CompanyRegisterActivity extends BaseActivity{
    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_register_company;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}
