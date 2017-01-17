package com.gs.buluo.store.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.CreateStoreBean;
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
import com.gs.buluo.store.utils.FresoUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.activity.CaptureActivity;
import com.gs.buluo.store.view.activity.LoginActivity;
import com.gs.buluo.store.view.activity.SelfActivity;
import com.gs.buluo.store.view.activity.SettingActivity;
import com.gs.buluo.store.view.activity.CreateStoreVarietyActivity;
import com.gs.buluo.store.view.activity.VerifyActivity;
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
    SimpleDraweeView mHead;
    LinearLayout llLogin;
    LinearLayout llUnLogin;
    TextView mNick;

    SimpleDraweeView mCover;
    PullToZoomScrollViewEx scrollView;

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
        mHead = (SimpleDraweeView) headView.findViewById(R.id.mine_head);
        mHead.setOnClickListener(this);
        headView.findViewById(R.id.mine_login).setOnClickListener(this);
        headView.findViewById(R.id.mine_register).setOnClickListener(this);
        headView.findViewById(R.id.mine_update).setOnClickListener(this);

        initContentView(contentView);
        zoomView.findViewById(R.id.mine_setting).setOnClickListener(this);
        zoomView.findViewById(R.id.mine_cover).setOnClickListener(this);
        zoomView.findViewById(R.id.self_scan).setOnClickListener(this);
        zoomView.findViewById(R.id.rl_head_bg);

        contentView.findViewById(R.id.mine_store).setOnClickListener(this);
        contentView.findViewById(R.id.mine_create).setOnClickListener(this);

        llLogin = (LinearLayout) headView.findViewById(R.id.self_ll_login);
        llUnLogin = (LinearLayout) headView.findViewById(R.id.self_ll_un_login);
        mNick = (TextView) headView.findViewById(R.id.self_nickname);
        mCover = (SimpleDraweeView) zoomView.findViewById(R.id.rl_head_bg);


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
                intent.setClass(getActivity(), VerifyActivity.class);
                startActivity(intent);
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
                intent.setClass(getActivity(), CreateStoreVarietyActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_store:
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
        CreateStoreBean bean=new CreateStoreBean();
        bean.setCover(url);
        new MainModel().updateUser(TribeApplication.getInstance().getUserInfo().getId(),
                "cover" ,url,bean, new TribeCallback<CodeResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                        StoreInfo userInfo = TribeApplication.getInstance().getUserInfo();
                        userInfo.setCover(url);
                        new StoreInfoDao().update(userInfo);
                        mCover.setImageURI("file://" + path);
                    }

                    @Override
                    public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                        ToastUtils.ToastMessage(mContext,R.string.connect_fail);
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
            String nickname = TribeApplication.getInstance().getUserInfo().getName();
            if (!TextUtils.isEmpty(nickname)) {
                mNick.setText(nickname);
            } else {
                mNick.setText("");
            }
            FresoUtils.loadImage(TribeApplication.getInstance().getUserInfo().getCover(), mCover);
            FresoUtils.loadImage(TribeApplication.getInstance().getUserInfo().getLogo(), mHead);
        } else {
            llLogin.setVisibility(View.GONE);
            llUnLogin.setVisibility(View.VISIBLE);
            FresoUtils.loadImage("", mHead);
            FresoUtils.loadImage("", mCover);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
