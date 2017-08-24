package com.gs.buluo.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.MessageToggleBean;
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.store.network.MessageApis;
import com.gs.buluo.store.network.TribeRetrofit;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/8/24.
 */

public class MessageManagerAdapter extends BaseAdapter {
    Context mCtx;
    List<MessageToggleBean> datas;

    public MessageManagerAdapter(Context context, List<MessageToggleBean> data) {
        mCtx = context;
        datas = data;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.item_msg_manager, parent, false);
        }
        MessageToggleBean messageToggleBean = datas.get(position);
        TextView textView = (TextView) convertView.findViewById(R.id.item_msg_manager_name);
        final Switch toggle = (Switch) convertView.findViewById(R.id.item_msg_manager_switch);
        toggle.setChecked(messageToggleBean.isOpen);
        textView.setText(messageToggleBean.messageTypeView.homeMessageTypeEnum == null ? "其他消息类型" : messageToggleBean.messageTypeView.homeMessageType);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMessageStatus(toggle,position,toggle.isChecked());
            }
        });
        return convertView;
    }

    private void updateMessageStatus(final Switch toggle, final int position, final boolean isChecked) {
        LoadingDialog.getInstance().show(mCtx, "", true);
        final MessageToggleBean bean = datas.get(position);
        TribeRetrofit.getInstance().createApi(MessageApis.class).
                toggleMessageStatus(TribeApplication.getInstance().getUserInfo().getId(), bean.messageTypeView.homeMessageTypeEnum, new ValueRequestBody(isChecked + ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse messageToggleBeanBaseResponse) {
                        bean.isOpen = isChecked;
                    }

                    @Override
                    public void onFail(ApiException e) {
                        toggle.setChecked(!isChecked);
                        ToastUtils.ToastMessage(mCtx, R.string.connect_fail);
                    }
                });
    }
}
