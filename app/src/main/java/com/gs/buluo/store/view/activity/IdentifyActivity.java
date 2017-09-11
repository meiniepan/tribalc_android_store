package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.RequestBodyBean.AuthorityRequest;
import com.gs.buluo.store.bean.StoreAccount;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.network.MainApis;
import com.gs.buluo.store.network.TribeRetrofit;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/7.
 */
public class IdentifyActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.verify_IdCardNumber)
    EditText mIdCardNumber;
    @Bind(R.id.verify_name)
    EditText mName;
    @Bind(R.id.identify_sign)
    ImageView mSign;
    @Bind(R.id.identify_finish)
    TextView mFinish;
    private StoreAccount infoEntity;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra(Constant.UPDATE_PHONE_SIGN,false)){
            findView(R.id.tv_update_phone_sign).setVisibility(View.VISIBLE);
        }
        mFinish.setOnClickListener(this);
        findView(R.id.verify_jump).setOnClickListener(this);
        infoEntity = TribeApplication.getInstance().getUserInfo();
        if (infoEntity.getLegalPersonIdNo() != null) {
            switch (infoEntity.getAuthStatus()) {
                case NOT_START:
                    break;
                case PROCESSING:
                    break;
                case SUCCESS:
                    mSign.setVisibility(View.VISIBLE);
                    mSign.setImageResource(R.mipmap.identify_success);
                    mIdCardNumber.setFocusable(false);
                    mName.setFocusable(false);
                    mFinish.setVisibility(View.GONE);
                    break;
                case FAILURE:
                    mSign.setVisibility(View.VISIBLE);
                    mSign.setImageResource(R.mipmap.identify_fail);
                    mFinish.setText("重新提交认证");
                    mFinish.setVisibility(View.VISIBLE);
                    mFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doIdentity();
                        }
                    });
                    break;
            }
            mIdCardNumber.setInputType(InputType.TYPE_CLASS_TEXT);
            mIdCardNumber.setKeyListener(new NewNumberKeyListener() {
            });
            mIdCardNumber.setText(infoEntity.getLegalPersonIdNo());
            mName.setText(infoEntity.getLegalPersonName());
        }
    }

    private void doIdentity() {
        showLoadingDialog();
        AuthorityRequest request = new AuthorityRequest();
        request.idNo = mIdCardNumber.getText().toString().trim();
        request.name = mName.getText().toString().trim();

        TribeRetrofit.getInstance().createApi(MainApis.class).
                doAuthentication(TribeApplication.getInstance().getUserInfo().getId(), request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreAccount>>() {
                    @Override
                    public void onNext(BaseResponse<StoreAccount> response) {
                        StoreAccount data = response.data;
                        data.setToken(TribeApplication.getInstance().getUserInfo().getToken());
                        data.setMid(infoEntity.getMid());
                        new StoreInfoDao().update(data);
                        switch (data.getAuthStatus()) {
                            case SUCCESS:
                                ToastUtils.ToastMessage(IdentifyActivity.this, "身份认证成功");
                                startActivity(new Intent(getCtx(), IdentifyActivity.class));
                                break;
                            case PROCESSING:
                                ToastUtils.ToastMessage(IdentifyActivity.this, "身份认证处理中...");
                                break;
                            case FAILURE:
                                ToastUtils.ToastMessage(IdentifyActivity.this, "身份认证失败,请过段时间再试");
                                break;
                        }
                        finish();
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_verify;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.identify_finish:
                String id = mIdCardNumber.getText().toString().trim();
                if (mIdCardNumber.length() == 0 || mName.length() == 0) {
                    ToastUtils.ToastMessage(IdentifyActivity.this, R.string.not_empty);
                    return;
                }
                if (id.length() != 18 && id.length() != 16 && id.length() != 15) {
                    ToastUtils.ToastMessage(IdentifyActivity.this, getString(R.string.wrong_id_number));
                    return;
                }
                doIdentity();
                break;
            case R.id.verify_jump:
                finish();
                break;
        }
    }

    abstract static class NewNumberKeyListener extends NumberKeyListener {
        @Override
        protected char[] getAcceptedChars() {
            return new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'X', 'x'};
        }

        @Override
        public int getInputType() {
            return 3;
        }
    }
}
