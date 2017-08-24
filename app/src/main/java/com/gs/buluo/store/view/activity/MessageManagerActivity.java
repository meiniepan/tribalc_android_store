package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.MessageManagerAdapter;
import com.gs.buluo.store.bean.MessageToggleBean;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.network.MessageApis;
import com.gs.buluo.store.network.TribeRetrofit;

import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/8/24.
 */

public class MessageManagerActivity extends BaseActivity {
    @Bind(R.id.msg_manager_list)
    ListView msgManagerList;
    @Bind(R.id.status_layout)
    StatusLayout statusLayout;
    private MessageManagerAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        statusLayout.showProgressView();
        StoreInfo userInfo = TribeApplication.getInstance().getUserInfo();
        TribeRetrofit.getInstance().createApi(MessageApis.class).getMessageToggleList(userInfo.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<MessageToggleBean>>>() {
                    @Override
                    public void onNext(BaseResponse<List<MessageToggleBean>> messageToggleBeen) {
                        setData(messageToggleBeen.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        statusLayout.showErrorView(getString(R.string.connect_fail));
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_message_manager;
    }

    public void setData(final List<MessageToggleBean> data) {
        if (data == null && data.size() == 0) {
            statusLayout.showEmptyView("当前无消息");
            return;
        }
        statusLayout.showContentView();
        adapter = new MessageManagerAdapter(this, data);
        msgManagerList.setAdapter(adapter);
    }
}
