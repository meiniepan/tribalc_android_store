package com.gs.buluo.store.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.utils.SystemBarTintManager;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.view.impl.IBaseView;
import com.gs.buluo.common.widget.LoadingDialog;

import butterknife.ButterKnife;


/**
 * Created by admin on 2016/11/1.
 */
public abstract class BaseActivity<T extends BasePresenter<IBaseView>> extends AppCompatActivity {
    View mRoot;
    protected BasePresenter mPresenter;

    private int color = R.color.titlebar_background;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        AppManager.getAppManager().addActivity(this);
        setExplode();//new Slide()  new Fade()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            addFlag();
        }
        mPresenter = getPresenter();
        if (mPresenter != null && this instanceof IBaseView) {
            mPresenter.attach((IBaseView) this);
        }

        mRoot = createView();
        setContentView(mRoot);
        bindView(savedInstanceState);
        initSystemBar(this);

        View view = mRoot.findViewById(R.id.back);
        if (view!=null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addFlag() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setExplode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }
    }

    protected void init() {

    }

    private View createView() {
        View view = LayoutInflater.from(this).inflate(getContentLayout(), null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    public View getRootView() {
        return mRoot;
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorInt
     */
    public void setBarColor(int colorInt) {
        color = colorInt;
        initSystemBar(this);
    }

    private void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }

    private static void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void showLoadingDialog() {
        LoadingDialog.getInstance().show(mRoot.getContext(), getString(R.string.loading), true);
    }

    protected void showLoadingDialog(int res) {
        LoadingDialog.getInstance().show(mRoot.getContext(),res, true);
    }

    protected void dismissDialog() {
        LoadingDialog.getInstance().dismissDialog();
    }

    protected Context getCtx(){
        return this;
    }

    protected abstract void bindView(Bundle savedInstanceState);

    protected abstract int getContentLayout();

    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    protected boolean checkUser(Context context) {
        if (TribeApplication.getInstance().getUserInfo() == null) {
            ToastUtils.ToastMessage(context, getString(R.string.login_first));
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    protected  <T extends View> T findView(int resId) {
        return (T) findViewById(resId);
    }
}


