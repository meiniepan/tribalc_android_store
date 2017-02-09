package com.gs.buluo.store.view.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.StoreMeta;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.dao.StoreInfoDao;
import com.gs.buluo.store.eventbus.SelfEvent;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.MinePresenter;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.activity.BusinessVerifyActivity;
import com.gs.buluo.store.view.activity.CaptureActivity;
import com.gs.buluo.store.view.activity.CreateStoreVarietyActivity;
import com.gs.buluo.store.view.activity.GoodsStoreInfoActivity;
import com.gs.buluo.store.view.activity.LoginActivity;
import com.gs.buluo.store.view.activity.MealStoreInfoActivity;
import com.gs.buluo.store.view.activity.SelfActivity;
import com.gs.buluo.store.view.activity.SettingActivity;
import com.gs.buluo.store.view.activity.VerifyProcessingActivity;
import com.gs.buluo.store.view.activity.WalletActivity;
import com.gs.buluo.store.view.widget.panel.ChoosePhotoPanel;
import com.gs.buluo.store.view.widget.pulltozoom.PullToZoomScrollViewEx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import retrofit2.Response;


/**
 * Created by admin on 2016/11/1.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    ImageView mHead;
    LinearLayout llLogin;
    LinearLayout llUnLogin;
    TextView mNick;

    ImageView mCover;
    PullToZoomScrollViewEx scrollView;
    private TextView tvSign;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        initZoomView();
        if (TribeApplication.getInstance().getUserInfo() != null) {
            setLoginState(true);
        }
        getActivity().findViewById(R.id.mine_wallet).setOnClickListener(this);

        EventBus.getDefault().register(this);
    }

    private void initZoomView() {
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.self_zoom_layout, null, false);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.self_content_layout, null, false);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.self_head_layout, null, false);
        scrollView = (PullToZoomScrollViewEx) getActivity().findViewById(R.id.self_scroll_view);
        mHead = (ImageView) headView.findViewById(R.id.mine_head);
        mHead.setOnClickListener(this);
        headView.findViewById(R.id.mine_login).setOnClickListener(this);
        headView.findViewById(R.id.mine_register).setOnClickListener(this);
        headView.findViewById(R.id.mine_update).setOnClickListener(this);
        headView.findViewById(R.id.mine_head).setOnClickListener(this);

        initContentView(contentView);
        zoomView.findViewById(R.id.mine_setting).setOnClickListener(this);
        zoomView.findViewById(R.id.mine_cover).setOnClickListener(this);
        zoomView.findViewById(R.id.self_scan).setOnClickListener(this);
        zoomView.findViewById(R.id.rl_head_bg);

        contentView.findViewById(R.id.mine_store).setOnClickListener(this);
        contentView.findViewById(R.id.mine_create).setOnClickListener(this);
        tvSign = (TextView) contentView.findViewById(R.id.mine_store_sign);

        llLogin = (LinearLayout) headView.findViewById(R.id.self_ll_login);
        llUnLogin = (LinearLayout) headView.findViewById(R.id.self_ll_un_login);
        mNick = (TextView) headView.findViewById(R.id.self_nickname);
        mCover = (ImageView) zoomView.findViewById(R.id.rl_head_bg);


        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (12.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }

    private void initContentView(View contentView) {
        contentView.findViewById(R.id.mine_verify).setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SelfEvent event) {
        setLoginState(true);
    }

    @Override
    protected BasePresenter getPresenter() {
        return new MinePresenter();
    }

    @Override
    public void onClick(View view) {
        if (!checkUser(getActivity())) return;
        final Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.mine_head:
                intent.setClass(getActivity(), SelfActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_login:
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_register:
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_verify:
                getStoreStatus();
                break;
            case R.id.mine_update:
                ToastUtils.ToastMessage(getActivity(), R.string.no_function);
                break;
            case R.id.mine_setting:
                intent.setClass(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_cover:
                chooseCover();
                break;
            case R.id.self_scan:
                intent.setClass(getActivity(), CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_wallet:
                intent.setClass(getActivity(), WalletActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_create:
                StoreInfo userInfo = TribeApplication.getInstance().getUserInfo();
                String storeType = userInfo.getStoreType();
                if (storeType != null) {
                    if (TextUtils.equals(storeType,"GOODS")) {
                        intent.setClass(mContext, GoodsStoreInfoActivity.class);
                    } else {
                        intent.setClass(mContext, MealStoreInfoActivity.class);
                    }
                } else {
                    intent.setClass(mContext, CreateStoreVarietyActivity.class);
                }
                startActivity(intent);
                break;
        }
    }

    public void chooseCover() {
        ChoosePhotoPanel window = new ChoosePhotoPanel(getActivity(), false, new ChoosePhotoPanel.OnSelectedFinished() {
            @Override
            public void onSelected(final String path) {
                TribeUploader.getInstance().uploadFile("cover.jpeg", "", new File(path), new TribeUploader.UploadCallback() {
                    @Override
                    public void uploadSuccess(UploadAccessResponse.UploadResponseBody body) {
                        ToastUtils.ToastMessage(mContext, mContext.getString(R.string.upload_success));
                        updateUserCover(body, path);
                    }

                    @Override
                    public void uploadFail() {
                        ToastUtils.ToastMessage(mContext, mContext.getString(R.string.upload_fail));
                    }
                });
            }
        });
        window.show();
    }


    private void updateUserCover(final UploadAccessResponse.UploadResponseBody body, final String path) {
        final String url = body.objectKey;
        StoreMeta bean = new StoreMeta();
        bean.setCover(url);
        new MainModel().updateStore(TribeApplication.getInstance().getUserInfo().getId(),
                "cover", url, bean, new TribeCallback<CodeResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                        StoreInfo userInfo = TribeApplication.getInstance().getUserInfo();
                        userInfo.setCover(url);
                        new StoreInfoDao().update(userInfo);
                        mCover.setImageURI(Uri.parse("file://" + path));
                    }

                    @Override
                    public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                        ToastUtils.ToastMessage(mContext, R.string.connect_fail);
                    }
                });
    }

    public void setLoginState(boolean loginState) {
        if (null == llUnLogin || null == llLogin) {
            SystemClock.sleep(500);
        }
        if (loginState) {
            llLogin.setVisibility(View.VISIBLE);
            llUnLogin.setVisibility(View.GONE);
            StoreInfo userInfo = TribeApplication.getInstance().getUserInfo();
            String nickname = userInfo.getName();
            if (!TextUtils.isEmpty(nickname)) {
                mNick.setText(nickname);
            } else {
                mNick.setText("");
            }
            GlideUtils.loadImage(getContext(),userInfo.getCover(), mCover);
            GlideUtils.loadImage(getContext(),userInfo.getLogo(), mHead,true);
            if (userInfo.getStoreType()!=null){
                tvSign.setText(R.string.store_setting);
                Drawable drawable= getResources().getDrawable(R.mipmap.store_setting);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvSign.setCompoundDrawables(drawable,null,null,null);
            }
        } else {
            llLogin.setVisibility(View.GONE);
            llUnLogin.setVisibility(View.VISIBLE);
            mHead.setImageURI(null);
            mCover.setImageURI(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void getStoreStatus() {
        new MainModel().getDetailStoreInfo(TribeApplication.getInstance().getUserInfo().getId(),new TribeCallback<StoreMeta>() {
            @Override
            public void onSuccess(Response<BaseResponse<StoreMeta>> response) {
                String storeAuthenticationStatus;
                Intent intent = new Intent();
                storeAuthenticationStatus = response.body().data.authenticationStatus;
                if (storeAuthenticationStatus == null || TextUtils.equals(storeAuthenticationStatus, "NOT_START")) {
                    intent.setClass(getActivity(), BusinessVerifyActivity.class);
                    startActivity(intent);
                } else if (TextUtils.equals(storeAuthenticationStatus, "PROCESSING")) {
                    intent.setClass(getActivity(), VerifyProcessingActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFail(int responseCode, BaseResponse<StoreMeta> body) {
                ToastUtils.ToastMessage(getActivity(), R.string.connect_fail);
            }
        });
    }
}
