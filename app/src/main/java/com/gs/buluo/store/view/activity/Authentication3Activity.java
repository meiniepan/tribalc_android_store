package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.AuthenticationData;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;

import java.io.File;

import butterknife.Bind;
import retrofit2.Response;

/**
 * Created by hjn on 2017/1/20.
 */
public class Authentication3Activity extends BaseActivity{
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
                if (path==null)return;
                data.tradeLicence =path;
                doAuth();
            }
        });
    }

    private void doAuth() {
        showLoadingDialog();
        new MainModel().doAuthentication(data, new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                dismissDialog();
                ToastUtils.ToastMessage(getCtx(),"认证提交成功");
                startActivity(new Intent(getCtx(),MainActivity.class));
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                dismissDialog();
                ToastUtils.ToastMessage(getCtx(),R.string.connect_fail);
            }
        });


    }

    private void showChoosePhoto() {
        ChoosePhotoPanel panel=new ChoosePhotoPanel(this, false, new ChoosePhotoPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String string) {
                uploadPic(string);
            }
        });
        panel.show();
    }

    private void uploadPic(String pic) {
        showLoadingDialog();
        TribeUploader.getInstance().uploadFile("trade", "", new File(pic), new TribeUploader.UploadCallback() {
            @Override
            public void uploadSuccess(UploadAccessResponse.UploadResponseBody data) {
                dismissDialog();
                tradeImage.setVisibility(View.VISIBLE);
                Glide.with(Authentication3Activity.this).load(GlideUtils.formatImageUrl(data.objectKey)).centerCrop().into(tradeImage);
                findViewById(R.id.identify_trade_sign).setVisibility(View.GONE);
                path =data.objectKey;
            }

            @Override
            public void uploadFail() {
                dismissDialog();
                ToastUtils.ToastMessage(getCtx(),R.string.upload_fail);
            }
        });

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_auth_trade;
    }
}
