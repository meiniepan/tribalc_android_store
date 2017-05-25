package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.PhotoAdapter;
import com.gs.buluo.store.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hjn on 2017/1/10.
 */
public class PhotoActivity extends BaseActivity implements ChoosePhotoPanel.OnSelectedFinished, PhotoAdapter.OnDeleteListener {
    private boolean isLogo;
    @Bind(R.id.holder_image)
    ImageView image;
    @Bind(R.id.image_group)
    RecyclerView llGroup;

    Context mCtx;
    private ArrayList<String> pictures = new ArrayList<>();
    private ArrayList<String> oldPictures;
    private PhotoAdapter adapter;
    private Intent intent;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        String s = getIntent().getStringExtra(Constant.ForIntent.PHOTO_TYPE);
        adapter = new PhotoAdapter(this,pictures,this);
        llGroup.setAdapter(adapter);
        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        llGroup.setLayoutManager(layout);

        if ("logo".equals(s)) {
            isLogo = true;
            String logo = TribeApplication.getInstance().getUserInfo().getLogo();
            if (logo != null)
                Glide.with(this).load(GlideUtils.formatImageUrl(logo)).centerCrop().into(image);
        } else {
            oldPictures = getIntent().getStringArrayListExtra(Constant.ENVIRONMENT);
            image.setImageResource(R.mipmap.default_env);
            if (oldPictures != null) {
                image.setVisibility(View.GONE);
                adapter.addAll(oldPictures);
            }
        }
        intent = new Intent();
        findViewById(R.id.add_photo_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pictures.size()==9){
                    ToastUtils.ToastMessage(getCtx(),"最多可选9张");
                    return;
                }
                choosePhoto();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void choosePhoto() {
        ChoosePhotoPanel panel = new ChoosePhotoPanel(this, false, this);
        panel.show();
    }

    private void updatePic(String file) {
        showLoadingDialog(R.string.uploading);
        TribeUploader.getInstance().uploadFile("photo", "", file, new TribeUploader.UploadCallback() {
            @Override
            public void uploadSuccess(UploadResponseBody data) {
                dismissDialog();
                if (isLogo) {
                    Glide.with(mCtx).load(GlideUtils.formatImageUrl(data.objectKey)).centerCrop().into(image);
                    Intent intent = new Intent();
                    intent.putExtra(Constant.LOGO, data.objectKey);
                    setResult(201, intent);
                } else {
                    image.setVisibility(View.GONE);
                    adapter.add(data.objectKey);
                    intent.putStringArrayListExtra(Constant.ENVIRONMENT, pictures);
                    setResult(202, intent);
                }
            }

            @Override
            public void uploadFail() {
                ToastUtils.ToastMessage(PhotoActivity.this, R.string.connect_fail);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_photo;
    }

    @Override
    public void onSelected(String string) {
        updatePic(string);
    }

    @Override
    public void onDelete() {
        intent.putStringArrayListExtra(Constant.ENVIRONMENT, pictures);
        setResult(202, intent);
    }
}
