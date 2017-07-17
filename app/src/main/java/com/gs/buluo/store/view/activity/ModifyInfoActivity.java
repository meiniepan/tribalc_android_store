package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.SelfEvent;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.SelfPresenter;
import com.gs.buluo.store.view.impl.ISelfView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;

/**
 * Created by hjn on 2016/12/15.
 */
public class ModifyInfoActivity extends BaseActivity implements View.OnClickListener, ISelfView {
    @Bind(R.id.modify_save)
    TextView save;
    @Bind(R.id.modify_title)
    TextView title;

    private String info;
    private StoreInfo userInfo;

    private Intent intent;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        userInfo = TribeApplication.getInstance().getUserInfo();
        info = getIntent().getStringExtra(Constant.ForIntent.MODIFY);
        intent = new Intent();
        findViewById(R.id.modify_back).setOnClickListener(this);
        initView(info);
    }

    private void initView(final String info) {
        switch (info) {
            case Constant.LINKMAN:
                View nameView = ((ViewStub) findViewById(R.id.modify_nickname)).inflate();
                final EditText name = (EditText) nameView.findViewById(R.id.modify_nickname_edit);
                title.setText("联系人姓名");
                save.setVisibility(View.VISIBLE);
                showKeyBoard(name);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (name.length() == 0) return;
                        StoreMeta bean = new StoreMeta();
                        String value = name.getText().toString().trim();
                        showLoadingDialog();
                        ((SelfPresenter) mPresenter).updateUser(Constant.LINKMAN, value, bean);
                    }
                });
                break;

            case Constant.ForIntent.STORE_NAME:
                View storeNameView = ((ViewStub) findViewById(R.id.modify_nickname)).inflate();
                final EditText storeName = (EditText) storeNameView.findViewById(R.id.modify_nickname_edit);
                title.setText("商店名称");
                save.setVisibility(View.VISIBLE);
                showKeyBoard(storeName);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (storeName.length() == 0) return;
                        StoreMeta bean = new StoreMeta();
                        String value = storeName.getText().toString().trim();
                        bean.setName(value);
                        showLoadingDialog();
                        ((SelfPresenter) mPresenter).updateUser(Constant.NAME, value, bean);
                    }
                });
                break;
            case Constant.ADDRESS:
                title.setText(R.string.address);
                final View addressView = ((ViewStub) findViewById(R.id.modify_address)).inflate();
                final TextView address = (TextView) addressView.findViewById(R.id.modify_address_layout);
                save.setVisibility(View.VISIBLE);
                address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (address.length() == 0) return;
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                break;
        }

    }

    @Override
    protected int getContentLayout() {
        return R.layout.self_modification;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_single:
                break;
            case R.id.modify_loving:
                break;
            case R.id.modify_married:
                break;
            case R.id.modify_back:
                finish();
                break;
        }
    }

    @Override
    public void updateSuccess(String key, String value) {
        dismissDialog();
        switch (key) {
            case Constant.NAME:
                intent.putExtra(Constant.NAME, value);
                setResult(RESULT_OK, intent);
                userInfo.setName(value);
                EventBus.getDefault().post(new SelfEvent());
                finish();
                break;
            case Constant.LINKMAN:
                intent.putExtra(Constant.LINKMAN, value);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case Constant.AREA:
                break;
        }
        new StoreInfoDao().update(userInfo);
    }

    public void showKeyBoard(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
            }
        }, 200);
    }

    @Override
    public void showError(int res) {
        dismissDialog();
        ToastUtils.ToastMessage(this, getString(res));
    }

    @Override
    protected BasePresenter getPresenter() {
        return new SelfPresenter();
    }
}
