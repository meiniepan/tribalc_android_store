package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.SelfEvent;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.SelfPresenter;
import com.gs.buluo.store.utils.DensityUtils;
import com.gs.buluo.store.utils.FresoUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.impl.ISelfView;
import com.gs.buluo.store.view.widget.CustomAddLayout;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.Bind;

/**
 * Created by hjn on 2017/1/10.
 */
public class PhotoActivity extends BaseActivity implements ChoosePhotoPanel.OnSelectedFinished, ISelfView {
    private boolean isLogo;
    @Bind(R.id.holder_image)
    ImageView image;
    @Bind(R.id.image_group)
    CustomAddLayout llGroup;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String s = getIntent().getStringExtra(Constant.ForIntent.PHOTO_TYPE);
        if ("logo".equals(s)) {
            isLogo = true;
        }

        findViewById(R.id.add_photo_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        TribeUploader.getInstance().uploadFile("photo", "", new File(file), new TribeUploader.UploadCallback() {
            @Override
            public void uploadSuccess(UploadAccessResponse.UploadResponseBody data) {
                if (isLogo) {
                    updateStoreInfo(data);
                }
            }

            @Override
            public void uploadFail() {
                ToastUtils.ToastMessage(PhotoActivity.this, R.string.connect_fail);
            }
        });
    }

    private void updateStoreInfo(UploadAccessResponse.UploadResponseBody data) {
        ((SelfPresenter) mPresenter).updateUser(Constant.PICTURE, data.objectKey);
    }

    @Override
    protected BasePresenter getPresenter() {
        return new SelfPresenter();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_photo;
    }

    @Override
    public void onSelected(String string) {
        showLoadingDialog();
        if (isLogo){
            updatePic(string);
        }else {

        }
    }

    @Override
    public void updateSuccess(String key, String value) {
        dismissDialog();
        if (isLogo) {
            StoreInfoDao storeInfoDao = new StoreInfoDao();
            StoreInfo storeInfo = storeInfoDao.findFirst();
            storeInfo.setLogo(value);
            storeInfoDao.update(storeInfo);
            Glide.with(this).load(value).centerCrop().into(image);
            SelfEvent event = new SelfEvent();
            event.head = value;
            EventBus.getDefault().post(event);
        } else {
            image.setVisibility(View.GONE);
            ImageView imageView = new ImageView(this);
            Glide.with(this).load(FresoUtils.formatImageUrl(value)).centerCrop().into(imageView);
            llGroup.setmCellHeight(DensityUtils.dip2px(this, 100));
            llGroup.setmCellWidth(DensityUtils.dip2px(this, 100));
            llGroup.addView(imageView);
        }
    }

    @Override
    public void showError(int res) {
        dismissDialog();
        ToastUtils.ToastMessage(this, R.string.connect_fail);
    }
}
