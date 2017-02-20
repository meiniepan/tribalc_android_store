package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.AuthenticationData;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.network.MainService;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.ToastUtils;

import butterknife.Bind;
import retrofit2.Response;

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
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(data.authenticationStatus,"FAILURE")){
                    startActivity(new Intent(getCtx(),Authentication1Activity.class));
                }else {
                    startActivity(new Intent(getCtx(),MainActivity.class));
                }
            }
        });
    }

    private void initView(AuthenticationData data) {
        switch (data.authenticationStatus){
            case "FAILURE":
                authPic.setImageResource(R.mipmap.auth_fail);
                tvSign.setText(R.string.auth_fail);
                tvButton.setText(R.string.auth_again);
                tvTitle.setText("审核失败");
                break;
            case "SUCCEED":
                tvTitle.setText("审核成功");
                inflateView();
                setData(data);
                break;
        }

    }

    private void inflateView() {
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
        GlideUtils.loadImage(this,data.businessLicence,licencePic);
        GlideUtils.loadImage(this,data.idCardPicture.get(0),frontPic);
        GlideUtils.loadImage(this,data.idCardPicture.get(1),backPic);
        GlideUtils.loadImage(this,data.tradeLicence,permitPic);
    }
}
