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
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2017/1/12.
 */
public class Authentication2Activity extends BaseActivity {
    private String front;
    private String back;
    @Bind(R.id.identify_back_image)
    ImageView backImg;
    @Bind(R.id.identify_front_image)
    ImageView frontImg;
    private AuthenticationData data;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        data = getIntent().getParcelableExtra(Constant.AUTH);

        findViewById(R.id.ll_identify_front).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePhoto(true);
            }
        });
        findViewById(R.id.ll_identify_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePhoto(false);
            }
        });
        findViewById(R.id.qualification_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (front==null||back==null)return;
                List<String> list=new ArrayList<>();
                list.add(front);
                list.add(back);
                data.idCardPicture = list;
                Intent intent = new Intent(getCtx(),Authentication3Activity.class);
                intent.putExtra(Constant.AUTH,data);
                startActivity(intent);
            }
        });
        findView(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showChoosePhoto(final boolean isFront) {
        ChoosePhotoPanel panel=new ChoosePhotoPanel(this, false, new ChoosePhotoPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String string) {
                    uploadPic(string,isFront);
            }
        });
        panel.show();
    }

    private void uploadPic(String pic, final boolean isFront) {
        showLoadingDialog();
        TribeUploader.getInstance().uploadFile("id", "", new File(pic), new TribeUploader.UploadCallback() {
            @Override
            public void uploadSuccess(UploadAccessResponse.UploadResponseBody data) {
                dismissDialog();
                if (isFront){
                    frontImg.setVisibility(View.VISIBLE);
                    front = data.objectKey;
                    Glide.with(getCtx()).load(GlideUtils.formatImageUrl(data.objectKey)).centerCrop().into(frontImg);
                    findViewById(R.id.identify_front).setVisibility(View.GONE);
                }else {
                    backImg.setVisibility(View.VISIBLE);
                    back=data.objectKey;
                    findViewById(R.id.identify_back).setVisibility(View.GONE);
                    Glide.with(getCtx()).load(GlideUtils.formatImageUrl(data.objectKey)).centerCrop().into(backImg);
                }
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
        return R.layout.activity_qualification;
    }
}
