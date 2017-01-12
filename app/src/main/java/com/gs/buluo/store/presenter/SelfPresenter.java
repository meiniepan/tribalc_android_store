package com.gs.buluo.store.presenter;

import android.util.Log;

import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.view.impl.ISelfView;

import org.xutils.common.Callback;

/**
 * Created by hjn on 2016/11/3.
 */
public class SelfPresenter extends BasePresenter<ISelfView> {
    private static final String TAG = "UpdateUser";
    MainModel mainModel;

    public SelfPresenter() {
        mainModel = new MainModel();
    }

    public void updateUser(final String key, final String value) {
        mainModel.updateUser(TribeApplication.getInstance().getUserInfo().getId(), key, value, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "update user success !!!!! key is " + key + "  value is" + value);
                if (result.contains("200")) {
                    if (isAttach()) mView.updateSuccess(key, value);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (isAttach()) mView.showError(R.string.connect_fail);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
}
