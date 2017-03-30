package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.AuthenticationData;
import com.gs.buluo.store.utils.GlideUtils;

import butterknife.Bind;

/**
 * Created by hjn on 2017/1/20.
 */
public class AuthProcessingActivity extends BaseActivity{
    @Bind(R.id.auth_pic)
    ImageView authPic;
    @Bind(R.id.auth_sign)
    TextView tvSign;
    @Bind(R.id.verify_button)
    Button tvButton;
    @Bind(R.id.verify_status_title)
    TextView tvTitle;
    @Bind(R.id.auth_success_view)
    ViewStub mStub;
    private ImageView licencePic;
    private ImageView frontPic;
    private ImageView backPic;
    private ImageView permitPic;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        final AuthenticationData data = getIntent().getParcelableExtra(Constant.ForIntent.STATUS);
        initView(data);
        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.equals(data.authenticationStatus,Constant.SUCCEED)){
                    startActivity(new Intent(getCtx(),Authentication1Activity.class));
                }else {
                    startActivity(new Intent(getCtx(),MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void initView(AuthenticationData data) {
        if (TextUtils.equals(Constant.SUCCEED,data.authenticationStatus)){
            tvTitle.setText("审核成功");
            inflateView();
            setData(data);
        }else {
            authPic.setImageResource(R.mipmap.auth_fail);
            tvSign.setText(R.string.auth_fail);
            tvButton.setText(R.string.auth_again);
            tvTitle.setText("审核失败");
        }
    }

    private void inflateView() {
        findView(R.id.processing_view).setVisibility(View.GONE);
        View view = mStub.inflate();
        licencePic = (ImageView) view.findViewById(R.id.auth_licence);
        frontPic = (ImageView) view.findViewById(R.id.auth_id_front);
        backPic = (ImageView) view.findViewById(R.id.auth_id_front_back);
        permitPic = (ImageView) view.findViewById(R.id.auth_permit);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_verify_processing;
    }


    public void setData(AuthenticationData data) {
        GlideUtils.loadImage(this,data.businessLicense,licencePic);
        if (data.idCardPicture!=null){
            GlideUtils.loadImage(this,data.idCardPicture.get(0),frontPic);
            GlideUtils.loadImage(this,data.idCardPicture.get(1),backPic);
        }
        GlideUtils.loadImage(this,data.tradeLicense,permitPic);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back(){
        startActivity(new Intent(this,MainActivity.class));
    }
}
