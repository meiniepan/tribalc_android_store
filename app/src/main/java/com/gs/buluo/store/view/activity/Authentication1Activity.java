package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.AuthenticationData;
import com.gs.buluo.store.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;

import butterknife.Bind;
import cn.finalteam.galleryfinal.FunctionConfig;

/**
 * Created by hjn on 2016/11/7.
 */
public class Authentication1Activity extends BaseActivity implements View.OnClickListener {
    private AuthenticationData authenticationData;
    @Bind(R.id.identify_business_image)
    ImageView businessImg;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        authenticationData=new AuthenticationData();
        findViewById(R.id.identify_back).setOnClickListener(this);
        findViewById(R.id.ll_identify_business).setOnClickListener(this);
        findViewById(R.id.identify_business_next).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_verify;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_identify_business:
                choosePhoto();
                break;
            case R.id.identify_back:
                finish();
                break;
            case R.id.identify_business_next:
                if (authenticationData.businessLicense ==null)return;
                Intent intent=new Intent(getCtx(),Authentication2Activity.class);
                intent.putExtra(Constant.AUTH,authenticationData);
                startActivity(intent);
                break;
        }
    }

    private void choosePhoto() {
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableEdit(true)
                .setEnableCamera(true)
                .setEnableRotate(true)
                .setEnablePreview(true)
                .build();
        ChoosePhotoPanel panel = new ChoosePhotoPanel(this, functionConfig, new ChoosePhotoPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String string) {
                uploadPhoto(string);
            }
        });
        panel.show();
    }

    private void uploadPhoto(String path) {
        showLoadingDialog();
        TribeUploader.getInstance().uploadFile("business", "", path, new TribeUploader.UploadCallback() {
            @Override
            public void uploadSuccess(UploadResponseBody data) {
                dismissDialog();
                authenticationData .businessLicense = data.objectKey;
                businessImg.setVisibility(View.VISIBLE);
                Glide.with(Authentication1Activity.this).load(GlideUtils.formatImageUrl(data.objectKey)).override(600,300).into(businessImg);
                findViewById(R.id.identify_business).setVisibility(View.GONE);
            }

            @Override
            public void uploadFail() {
                dismissDialog();
                ToastUtils.ToastMessage(Authentication1Activity.this,R.string.update_fail);
            }
        });

    }

}