package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.AuthenticationData;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.AuthSuccessEvent;
import com.gs.buluo.store.network.MainApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import cn.finalteam.galleryfinal.FunctionConfig;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/1/20.
 */
public class Authentication3Activity extends BaseActivity {
    @Bind(R.id.identify_trade_image)
    ImageView tradeImage;
    String path;

    private AuthenticationData data;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        data = getIntent().getParcelableExtra(Constant.AUTH);
        findViewById(R.id.ll_identify_trade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePhoto();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.identify_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (path == null) return;
                data.tradeLicense = path;
                doAuth();
            }
        });
    }

    private void doAuth() {
        TribeRetrofit.getInstance().createApi(MainApis.class).doAuthentication(TribeApplication.getInstance().getUserInfo().getId(), data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<AuthenticationData>>() {
                    @Override
                    public void onNext(BaseResponse<AuthenticationData> response) {
                        getAuth();
                    }
                });
    }

    private void showChoosePhoto() {
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableEdit(true)
                .setEnableCamera(true)
                .setEnableRotate(true)
                .setEnablePreview(true)
                .build();
        ChoosePhotoPanel panel = new ChoosePhotoPanel(this, functionConfig, new ChoosePhotoPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String string) {
                showLoadingDialog();
                uploadPic(string);
            }
        });
        panel.show();
    }

    private void uploadPic(String pic) {
        TribeUploader.getInstance().uploadFile("trade", "", pic, new TribeUploader.UploadCallback() {
            @Override
            public void uploadSuccess(UploadResponseBody data) {
                dismissDialog();
                tradeImage.setVisibility(View.VISIBLE);
                Glide.with(Authentication3Activity.this).load(GlideUtils.formatImageUrl(data.objectKey)).centerCrop().into(tradeImage);
                findViewById(R.id.identify_trade_sign).setVisibility(View.GONE);
                path = data.objectKey;
            }

            @Override
            public void uploadFail() {
                ToastUtils.ToastMessage(getCtx(), R.string.upload_fail);
                dismissDialog();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_auth_trade;
    }

    public void getAuth() {
        TribeRetrofit.getInstance().createApi(MainApis.class).getAuth(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<AuthenticationData>>() {
                    @Override
                    public void onNext(BaseResponse<AuthenticationData> response) {
                        AuthenticationData data = response.data;
                        StoreInfoDao dao = new StoreInfoDao();
                        StoreInfo storeInfo = dao.findFirst();

                        if (TextUtils.equals(data.authenticationStatus, Constant.SUCCEED)) {
                            EventBus.getDefault().post(new AuthSuccessEvent());
                            storeInfo.setAuthenticationStatus(data.authenticationStatus);
                            dao.update(storeInfo);
                        }
                        Intent intent = new Intent();
                        intent.setClass(getCtx(), AuthProcessingActivity.class);
                        intent.putExtra(Constant.ForIntent.STATUS, data);
                        startActivity(intent);
                    }
                });
    }
}
