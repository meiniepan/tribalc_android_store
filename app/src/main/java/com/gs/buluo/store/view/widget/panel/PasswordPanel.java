package com.gs.buluo.store.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.common.widget.PwdEditText;
import com.gs.buluo.store.R;
import com.gs.buluo.store.ResponseCode;
import com.gs.buluo.store.bean.OrderBean;
import com.gs.buluo.store.bean.OrderPayment;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.eventbus.PaymentEvent;
import com.gs.buluo.store.model.MoneyModel;
import com.gs.buluo.store.utils.DensityUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.MD5;

import java.util.List;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/12/13.
 */
public class PasswordPanel extends Dialog implements Callback<BaseResponse<OrderPayment>> {
    private OnPasswordPanelDismissListener onPasswordPanelDismissListener;
    private List<String> orderId;
    private Context mContext;
    @Bind(R.id.pwd_board_pet)
    PwdEditText pwdEditText;
    private final String myPwd;
    private OrderBean.PayChannel payChannel;
    private String type;

    public PasswordPanel(Context context, String pwd, List<String> orderId, OrderBean.PayChannel channel, String type, OnPasswordPanelDismissListener onPasswordPanelDismissListener) {
        super(context, R.style.pay_dialog);
        mContext = context;
        myPwd = pwd;
        this.orderId = orderId;
        this.payChannel = channel;
        this.type = type;
        this.onPasswordPanelDismissListener = onPasswordPanelDismissListener;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.pwd_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mContext, 450);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        pwdEditText.showKeyBoard();

        pwdEditText.setInputCompleteListener(new PwdEditText.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if (TextUtils.equals(MD5.md5(pwdEditText.getStrPassword()), myPwd)) {
                    payMoney();
                } else {
                    ToastUtils.ToastMessage(getContext(), R.string.wrong_pwd);
                    pwdEditText.clear();
                }
            }
        });
        rootView.findViewById(R.id.pwd_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void payMoney() {
        LoadingDialog.getInstance().show(mContext, R.string.paying, true);
        new MoneyModel().createPayment(orderId, payChannel.name(), type, this);
    }

    @Override
    public void onResponse(Call<BaseResponse<OrderPayment>> call, Response<BaseResponse<OrderPayment>> response) {
//        pwdEditText.dismissKeyBoard();
        if (response.body() != null && response.code() == ResponseCode.GET_SUCCESS) {
            setStatus(response.body().data);
        } else {
            ToastUtils.ToastMessage(mContext, R.string.connect_fail);
            LoadingDialog.getInstance().dismissDialog();
        }
    }

    @Override
    public void onFailure(Call<BaseResponse<OrderPayment>> call, Throwable t) {
        LoadingDialog.getInstance().dismissDialog();
        ToastUtils.ToastMessage(mContext, R.string.connect_fail);
    }

    public void setStatus(final OrderPayment data) {
        if (data.status == OrderPayment.PayStatus.FINISHED || data.status == OrderPayment.PayStatus.PAYED) {
            LoadingDialog.getInstance().dismissDialog();
            ToastUtils.ToastMessage(getContext(), R.string.pay_success);
            EventBus.getDefault().post(new PaymentEvent());
            dismiss();
        } else {
            new Handler().postDelayed(new TimerTask() {
                @Override
                public void run() {
                    getPaymentInfo(data);
                }
            }, 1000);
        }
    }

    public void getPaymentInfo(OrderPayment data) {
        new MoneyModel().getPaymentStatus(data.id, new Callback<BaseResponse<OrderPayment>>() {
            @Override
            public void onResponse(Call<BaseResponse<OrderPayment>> call, Response<BaseResponse<OrderPayment>> response) {
                if (response.body() != null && response.code() == ResponseCode.GET_SUCCESS) {
                    setStatusAgain(response.body().data);
                } else {
                    ToastUtils.ToastMessage(mContext, R.string.connect_fail);
                    LoadingDialog.getInstance().dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<OrderPayment>> call, Throwable t) {
                ToastUtils.ToastMessage(mContext, R.string.connect_fail);
                LoadingDialog.getInstance().dismissDialog();
            }
        });
    }

    public void setStatusAgain(final OrderPayment data) {
        LoadingDialog.getInstance().dismissDialog();
        if (data.status == OrderPayment.PayStatus.FINISHED || data.status == OrderPayment.PayStatus.PAYED) {
            ToastUtils.ToastMessage(getContext(), R.string.pay_success);
            EventBus.getDefault().post(new PaymentEvent());
            dismiss();
        } else {
            ToastUtils.ToastMessage(getContext(), data.note);
        }
    }

    public interface OnPasswordPanelDismissListener {
        void onPasswordPanelDismiss();
    }

    @Override
    public void dismiss() {
        if (onPasswordPanelDismissListener != null) {
            onPasswordPanelDismissListener.onPasswordPanelDismiss();
        }
        super.dismiss();
    }
}
