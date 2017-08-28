package com.gs.buluo.store.receiver;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.HomeMessageEnum;
import com.gs.buluo.store.bean.PushMessageBean;
import com.gs.buluo.store.eventbus.NewMessageEvent;
import com.gs.buluo.store.view.activity.BillDetailActivity;
import com.gs.buluo.store.view.activity.OrderDetailActivity;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.tencent.mm.opensdk.utils.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * Created by hjn on 2017/8/24.
 */

public class XGPushReceiver extends XGPushBaseReceiver {
    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        Log.e("XGPushTextMessage", xgPushTextMessage.toString());
        PushMessageBean messageBean = JSON.parseObject(xgPushTextMessage.getCustomContent(), PushMessageBean.class);
        HashMap<HomeMessageEnum, Integer> messageMap = TribeApplication.getInstance().getMessageMap();
        HomeMessageEnum name = messageBean.message.messageBodyType;
        Integer msgCount = messageMap.get(name);
        if (msgCount != null) {
            messageMap.put(name, msgCount += 1);
        } else {
            messageMap.put(name, 1);
        }
        if (name == HomeMessageEnum.ORDER_SETTLE)
            EventBus.getDefault().post(new NewMessageEvent(name));
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        Log.e("XGPushClickedResult", xgPushClickedResult.toString());
        PushMessageBean messageBean = JSON.parseObject(xgPushClickedResult.getCustomContent(), PushMessageBean.class);
        PushMessageBean.PushMessageBody messageBody = messageBean.message;
        if (messageBody.messageBodyType == HomeMessageEnum.ORDER_SETTLE) {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constant.ORDER_ID, messageBody.referenceId);
            context.startActivity(intent);
        } else if (messageBody.messageBodyType == HomeMessageEnum.TENANT_WITHDRAW) {
            Intent intent = new Intent(context, BillDetailActivity.class);
            intent.putExtra(Constant.BILL_ID, messageBody.referenceId);
            context.startActivity(intent);
        }
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        Log.e("XGPushShowedResult", xgPushShowedResult.toString());
    }
}
