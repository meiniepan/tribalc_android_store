package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.panel.SimpleChoosePanel;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.OrderDetailGoodsAdapter;
import com.gs.buluo.store.bean.HomeMessageEnum;
import com.gs.buluo.store.bean.OrderBean;
import com.gs.buluo.store.bean.RequestBodyBean.ReadMsgRequest;
import com.gs.buluo.store.bean.RequestBodyBean.RefundRequest;
import com.gs.buluo.store.bean.ResponseBody.OrderResponseBean;
import com.gs.buluo.store.eventbus.NewMessageEvent;
import com.gs.buluo.store.eventbus.PaymentEvent;
import com.gs.buluo.store.network.MessageApis;
import com.gs.buluo.store.network.MoneyApis;
import com.gs.buluo.store.network.OrderApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.OrderPresenter;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.TribeDateUtils;
import com.gs.buluo.store.view.impl.IOrderView;
import com.gs.buluo.store.view.widget.panel.LogisticsPanel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/25.
 */
public class OrderDetailActivity extends BaseActivity implements IOrderView, SimpleChoosePanel.OnSelectedFinished {
    @Bind(R.id.order_detail_create_time)
    TextView tvCreateTime;
    @Bind(R.id.order_detail_address)
    TextView tvAddress;
    @Bind(R.id.order_detail_receiver)
    TextView tvReceiver;
    @Bind(R.id.order_detail_phone)
    TextView tvPhone;
    @Bind(R.id.order_detail_store_name)
    TextView tvStoreName;
    @Bind(R.id.order_detail_number)
    TextView tvOrderNum;
    @Bind(R.id.order_send_method)
    TextView tvMethod;
    @Bind(R.id.order_send_price)
    TextView tvSendPrice;

    @Bind(R.id.order_price_total)
    TextView tvTotal;
    @Bind(R.id.order_detail_goods_list)
    ListView lvGoods;
    @Bind(R.id.order_detail_pay_time)
    TextView tvPayTime;
    @Bind(R.id.order_detail_send_time)
    TextView tvSendTime;
    @Bind(R.id.order_detail_receive_time)
    TextView tvReceiveTime;
    @Bind(R.id.order_detail_positive)
    Button tvButton;
    @Bind(R.id.order_detail_negative)
    Button tvNeg;

    @Bind(R.id.order_detail_counter)
    TextView tvCounter;
    @Bind(R.id.order_detail_note)
    TextView tvNote;
    @Bind(R.id.order_detail_header)
    ImageView mHeader;
    @Bind(R.id.order_detail_logistic)
    TextView tvLogistic;
    @Bind(R.id.order_detail_refund_time)
    TextView tvRefundTime;
    @Bind(R.id.order_detail_refund_reason)
    TextView tvRefundReason;
    private Context mCtx;
    private OrderBean bean;
    private long time;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        EventBus.getDefault().register(this);
        bean = getIntent().getParcelableExtra(Constant.ORDER);
        String orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        if (bean != null) {
            initView();
            initData(bean);
        } else if (!TextUtils.isEmpty(orderId)) {
            getData(orderId);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String orderId = intent.getStringExtra(Constant.ORDER_ID);
        if (bean != null) {
            initView();
            initData(bean);
        } else if (!TextUtils.isEmpty(orderId)) {
            getData(orderId);
        }
    }

    private void initView() {
        if (bean.status == OrderBean.OrderStatus.NO_SETTLE) { //待付款
            findView(R.id.ll_order_detail_counter).setVisibility(View.VISIBLE);
        } else if (bean.status == OrderBean.OrderStatus.SETTLE) {    //付款,发货,退货
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            findViewById(R.id.order_bottom).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_send_time).setVisibility(View.GONE);
            findView(R.id.ll_order_detail_counter).setVisibility(View.GONE);
        } else if (bean.status == OrderBean.OrderStatus.DELIVERY) { //待收货
            findViewById(R.id.ll_send_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_logistic).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            findView(R.id.ll_order_detail_counter).setVisibility(View.GONE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            findViewById(R.id.order_bottom).setVisibility(View.GONE);
        } else if (bean.status == OrderBean.OrderStatus.RECEIVED) {  //完成
            findView(R.id.ll_order_detail_counter).setVisibility(View.GONE);
            findViewById(R.id.ll_send_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_logistic).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_receive_time).setVisibility(View.VISIBLE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            tvReceiveTime.setText(TribeDateUtils.dateFormat7(new Date(bean.receivedTime)));
            findViewById(R.id.order_bottom).setVisibility(View.GONE);
        } else if (bean.status == OrderBean.OrderStatus.REFUNDED) {
            findView(R.id.ll_order_detail_counter).setVisibility(View.GONE);
            findViewById(R.id.ll_send_time).setVisibility(View.GONE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_logistic).setVisibility(View.GONE);
            findViewById(R.id.ll_receive_time).setVisibility(View.GONE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            tvReceiveTime.setText(TribeDateUtils.dateFormat7(new Date(bean.receivedTime)));
            findViewById(R.id.order_bottom).setVisibility(View.VISIBLE);
            findView(R.id.ll_refund_time).setVisibility(View.VISIBLE);
            findViewById(R.id.order_bottom).setVisibility(View.GONE);
        } else {
            findView(R.id.ll_order_detail_counter).setVisibility(View.GONE);
        }
    }

    private void initData(final OrderBean order) {
        tvStoreName.setText(order.nickName);
        String[] address = order.address.split("\\|");
        tvAddress.setText(address[2]);
        tvPhone.setText(address[1]);
        tvReceiver.setText(address[0]);
        tvOrderNum.setText(order.orderNum);
        tvCreateTime.setText(TribeDateUtils.dateFormat7(new Date(order.createTime)));
        tvRefundReason.setText(order.refundNote);
        tvRefundTime.setText(TribeDateUtils.dateFormat7(new Date(order.refundTime)));
        if (order.status == OrderBean.OrderStatus.NO_SETTLE) setCounter(order.createTime);
        tvMethod.setText(order.expressType);
        if (order.expressType == null) tvMethod.setText("包邮");
        else if (TextUtils.equals(order.expressType, "NOT_PAYPOSTAGE")) tvMethod.setText("");
        if (order.note != null) tvNote.setText(order.note);
        tvSendPrice.setText(order.expressFee + "");
        GlideUtils.loadImage(this, order.picture, mHeader, true);
        tvTotal.setText(order.totalFee + "");
        tvLogistic.setText(order.logisticsNum);
        OrderDetailGoodsAdapter adapter = new OrderDetailGoodsAdapter(order.itemList, this);
        lvGoods.setAdapter(adapter);
        CommonUtils.setListViewHeightBasedOnChildren(lvGoods);

        lvGoods.setOnItemClickListener
                (new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                         String itemId = order.itemList.get(position).goods.id;
                         Intent intent = new Intent(mCtx, GoodsDetailActivity.class);
                         intent.putExtra(Constant.GOODS_ID, itemId);
                         startActivity(intent);
                     }
                 }
                );
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order_detail;
    }

    private void showTransPanel() {
        LogisticsPanel panel = new LogisticsPanel(this);
        panel.setPresenter((OrderPresenter) mPresenter);
        panel.setData(bean.id);
        panel.show();
    }

    @Override
    protected BasePresenter getPresenter() {
        return new OrderPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constant.REQUEST_ADDRESS) {
            tvAddress.setText(data.getStringExtra(Constant.ADDRESS));
            tvReceiver.setText(data.getStringExtra(Constant.RECEIVER));
            tvPhone.setText(data.getStringExtra(Constant.PHONE));
        }
    }

    @Override
    public void updateSuccess(OrderBean data) {
        dismissDialog();
        bean = data;
        initView();
        initData(data);
    }

    @Override
    public void getOrderDetailSuccess(OrderBean data) {
        bean = data;
        initView();
        initData(data);
    }

    @Override
    public void getOrderInfoSuccess(OrderResponseBean data) {
    }

    //待付款订单页面 付款成功的通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void paySuccess(PaymentEvent event) {
        bean.status = OrderBean.OrderStatus.DELIVERY;
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        EventBus.getDefault().unregister(this);
    }

    public void setCounter(long createTime) {
        time = createTime + 24 * 3600 * 1000 - System.currentTimeMillis();
        if (time <= 0) {
            tvCounter.setText("已超时");
            return;
        }
        tvCounter.setText(TribeDateUtils.hourCounter(time));
        handler.postDelayed(runnable, 1000);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time <= 0) {
                tvCounter.setText("时间到");
                handler.removeCallbacks(runnable);
                return;
            }
            time -= 1000;
            tvCounter.setText(TribeDateUtils.hourCounter(time));
            handler.postDelayed(this, 1000);
        }
    };


    private void getData(String orderId) {
        showLoadingDialog();
        ((OrderPresenter) mPresenter).getOrderDetail(orderId);
        readMessage(orderId);
    }

    private void readMessage(String orderId) {      //通知栏点击进入详情，调用读一条消息接口
        ReadMsgRequest readMsgRequest = new ReadMsgRequest();
        readMsgRequest.messageBodyType = HomeMessageEnum.ORDER_SETTLE;
        readMsgRequest.referenceId = orderId;
        TribeRetrofit.getInstance().createApi(MessageApis.class).readMessage(TribeApplication.getInstance().getUserInfo().getId(), readMsgRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<BaseResponse>() {
                    @Override
                    public void call(BaseResponse baseResponse) {
                        Integer integer = TribeApplication.getInstance().getMessageMap().get(HomeMessageEnum.ORDER_SETTLE);
                        if (integer != null) integer = integer - 1;
                        TribeApplication.getInstance().getMessageMap().put(HomeMessageEnum.ORDER_SETTLE, integer);
                        EventBus.getDefault().post(new NewMessageEvent(HomeMessageEnum.ORDER_SETTLE));
                    }
                });
    }


    @Override
    public void showError(int res, String message) {
        ToastUtils.ToastMessage(getCtx(), message);
    }

    public void doDelivery(View view) {
        showTransPanel();
    }

    public void doRefund(View view) {
        ArrayList<String> messages = new ArrayList<>();
        messages.add("我不想卖了");
        messages.add("卖家缺货");
        messages.add("同城见面交易");
        messages.add("其他原因");
        new SimpleChoosePanel.Builder<String>(this, this)
                .setData(messages).setTitle("选择退款理由").build().show();
    }

    @Override
    public void onSelected(Object o) {
        showLoadingDialog();
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.reason = o.toString();
        refundRequest.orderId = bean.id;
        TribeRetrofit.getInstance().createApi(MoneyApis.class).refundOrder(TribeApplication.getInstance().getUserInfo().getId(), refundRequest)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseResponse, Observable<BaseResponse<OrderBean>>>() {
                    @Override
                    public Observable<BaseResponse<OrderBean>> call(BaseResponse baseResponse) {
                        return TribeRetrofit.getInstance().createApi(OrderApis.class).getOrderDetail(bean.id, TribeApplication.getInstance().getUserInfo().getId());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderBean>>() {
                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), e.getDisplayMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<OrderBean> baseResponse) {
                        ToastUtils.ToastMessage(getCtx(), "退货成功");
                        EventBus.getDefault().post(new PaymentEvent());
                        bean = baseResponse.data;
                        findView(R.id.ll_refund_time).setVisibility(View.VISIBLE);
                        findViewById(R.id.order_bottom).setVisibility(View.GONE);
                        tvRefundReason.setText(bean.refundNote);
                        tvRefundTime.setText(TribeDateUtils.dateFormat7(new Date(bean.refundTime)));
                    }
                });
    }
}
