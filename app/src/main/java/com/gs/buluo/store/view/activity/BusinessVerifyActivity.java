package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.AuthenticationData;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.utils.FresoUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;

import java.io.File;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/7.
 */
public class BusinessVerifyActivity extends BaseActivity implements View.OnClickListener {
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
                if (authenticationData.businessLicence==null)return;
                Intent intent=new Intent(BusinessVerifyActivity.this,IdentificationActivity.class);
                intent.putExtra(Constant.AUTH,authenticationData);
                startActivity(intent);
                break;
        }
    }

    private void choosePhoto() {
        ChoosePhotoPanel panel = new ChoosePhotoPanel(this, false, new ChoosePhotoPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String string) {
                uploadPhoto(string);
            }
        });
        panel.show();
    }

    private void uploadPhoto(String path) {
        TribeUploader.getInstance().uploadFile("business", "", new File(path), new TribeUploader.UploadCallback() {
            @Override
            public void uploadSuccess(UploadAccessResponse.UploadResponseBody data) {
                authenticationData .businessLicence = data.objectKey;
                businessImg.setVisibility(View.VISIBLE);
                Glide.with(BusinessVerifyActivity.this).load(FresoUtils.formatImageUrl(data.objectKey)).into(businessImg);
                findViewById(R.id.identify_business).setVisibility(View.GONE);
            }

            @Override
            public void uploadFail() {
                ToastUtils.ToastMessage(BusinessVerifyActivity.this,R.string.update_fail);
            }
        });

    }

}