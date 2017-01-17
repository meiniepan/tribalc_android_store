package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.adapter.OrderDetailGoodsAdapter;
import com.gs.buluo.store.bean.OrderBean;
import com.gs.buluo.store.bean.ResponseBody.OrderResponse;
import com.gs.buluo.store.eventbus.PaymentEvent;
import com.gs.buluo.store.presenter.BasePresenter;
import com.gs.buluo.store.presenter.OrderPresenter;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.utils.TribeDateUtils;
import com.gs.buluo.store.view.impl.IOrderView;
import com.gs.buluo.store.view.widget.CustomAlertDialog;
import com.gs.buluo.store.view.widget.panel.PayPanel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/25.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener, IOrderView {
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
    @Bind(R.id.order_detail_button)
    TextView tvButton;
    @Bind(R.id.ll_logistics_number)
    TextView tvLogNum;
    @Bind(R.id.ll_logistics_way)
    TextView tvLogWay;

    private Context mCtx;
    private OrderBean bean;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        EventBus.getDefault().register(this);
        findViewById(R.id.order_detail_back).setOnClickListener(this);
        findViewById(R.id.order_detail_button).setOnClickListener(this);
        bean = getIntent().getParcelableExtra(Constant.ORDER);

        if (bean != null) {
            initView();
            initData(bean);
        }
    }

    private void initView() {
        if (bean.status == OrderBean.OrderStatus.NO_SETTLE) { //待付款


        } else if (bean.status == OrderBean.OrderStatus.RECEIVED) {    //付款未发货,选物流
            findViewById(R.id.ll_send_time).setVisibility(View.GONE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            tvLogNum.setVisibility(View.VISIBLE);
            tvLogWay.setVisibility(View.VISIBLE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
        } else if (bean.status == OrderBean.OrderStatus.DELIVERY) { //待收货
            findViewById(R.id.ll_send_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            findViewById(R.id.order_bottom).setVisibility(View.GONE);
        } else if (bean.status == OrderBean.OrderStatus.SETTLE) {  //完成 取消
            findViewById(R.id.ll_send_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_receive_time).setVisibility(View.VISIBLE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            tvReceiveTime.setText(TribeDateUtils.dateFormat7(new Date(bean.receivedTime)));
            findViewById(R.id.order_bottom).setVisibility(View.GONE);
        } else {
            findViewById(R.id.order_bottom).setVisibility(View.GONE);
        }
    }

    private void initData(final OrderBean order) {
        if (order.store != null) {
            tvStoreName.setText(order.store.name);
        }
        String[] address = order.address.split("\\|");
        tvAddress.setText(address[2]);
        tvPhone.setText(address[1]);
        tvReceiver.setText(address[0]);
        tvOrderNum.setText(order.orderNum);
        tvCreateTime.setText(TribeDateUtils.dateFormat7(new Date(order.createTime)));
//        tvMethod.setText(order.expressType);
//        if (order.expressType==null)
        tvMethod.setText("包邮");
        tvSendPrice.setText(order.expressFee + "");

        tvTotal.setText(order.totalFee + "");
        if (order.store != null)
            tvStoreName.setText(order.store.name);

        OrderDetailGoodsAdapter adapter = new OrderDetailGoodsAdapter(order.itemList, this);
        lvGoods.setAdapter(adapter);
        CommonUtils.setListViewHeightBasedOnChildren(lvGoods);

        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemId = order.itemList.get(position).goods.id;
                Intent intent = new Intent(mCtx, GoodsDetailActivity.class);
                intent.putExtra(Constant.GOODS_ID, itemId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail_back:
                finish();
                break;
            case R.id.order_detail_button:
                String way= tvLogWay.getText().toString().trim();
                String num = tvLogNum.getText().toString().trim();
                if (TextUtils.isEmpty(way)||TextUtils.isEmpty(num)){
                    ToastUtils.ToastMessage(mCtx,"请填写物流信息");
                    return;
                }
                if (bean.status == OrderBean.OrderStatus.SETTLE) { //发货
                    ((OrderPresenter) mPresenter).updateOrderStatus(bean.id,way,num ,OrderBean.OrderStatus.RECEIVED.name());
                }
        }
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
    public void updateSuccess() {
        EventBus.getDefault().post(new PaymentEvent());
        ToastUtils.ToastMessage(this, R.string.update_success);
        finish();
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this, res);
    }

    @Override
    public void getOrderInfoSuccess(OrderResponse.OrderResponseBean data) {
    }

    //待付款订单页面 付款成功的通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void paySuccess(PaymentEvent event) {
        bean.status = OrderBean.OrderStatus.SETTLE;
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
