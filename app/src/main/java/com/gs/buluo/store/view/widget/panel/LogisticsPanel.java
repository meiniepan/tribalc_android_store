package com.gs.buluo.store.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.OrderBean;
import com.gs.buluo.store.presenter.OrderPresenter;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by hjn on 2017/2/4.
 */
public class LogisticsPanel extends Dialog implements View.OnClickListener {
    private MaterialSpinner spinner;
    private TextView tvNum;
    private String name ="顺丰";
    private OrderPresenter presenter;
    private String id;

    public LogisticsPanel(Context context) {
        super(context, R.style.my_dialog);
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.logistics_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(getContext(), 360);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        rootView.findViewById(R.id.logistics_send).setOnClickListener(this);
        spinner= (MaterialSpinner) rootView.findViewById(R.id.logistics_spinner);
//        spinner = (Spinner) rootView.findViewById(R.id.logistics_spinner);
        tvNum = (TextView) rootView.findViewById(R.id.logistics_number);

        initLogistics();
    }

    private void initLogistics() {
        final ArrayList<String> list =new ArrayList<>();
        list.add("顺丰");
        list.add("韵达");
        list.add("EMS");
        list.add("中通");
        list.add("圆通");
        list.add("申通");
        list.add("天天快递");
        list.add("其他");
        spinner.setItems(list);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                name = item;
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.logistics_send){
            String num = tvNum.getText().toString().trim();
            LoadingDialog.getInstance().show(getContext(),R.string.loading,true);
            presenter.updateOrderStatus(id,num,name,OrderBean.OrderStatus.DELIVERY.name());
            dismiss();
        }
    }

    public void setPresenter(OrderPresenter presenter) {
        this.presenter = presenter;
    }

    public void setData(String data) {
        this.id = data;
    }
}
