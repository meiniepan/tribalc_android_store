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
import com.gs.buluo.store.view.widget.wheel.OnWheelChangedListener;
import com.gs.buluo.store.view.widget.wheel.WheelView;
import com.gs.buluo.store.view.widget.wheel.adapters.ArrayWheelAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hjn on 2017/1/20.
 */
public class ServeTimePicker extends Dialog {
    private WheelView mNoon;
    private WheelView mHour;
    private WheelView mMini;

    private String noon = "上午";
    private String hour = "9";
    private String mini = "30";

    private OnSelectedFinished onSelectedFinished;
    Context context;
    private List<String> hourList;
    private List<String> noonList;
    private List<String> minList;

    public ServeTimePicker(Context context, OnSelectedFinished onSelectedFinished) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.onSelectedFinished = onSelectedFinished;
        initView();
        initData();
    }

    private void initData() {
        noonList = new ArrayList<>();
        noonList.add("上午");
        noonList.add("下午");
//        mNoon.setViewAdapter(new ArrayWheelAdapter<>(context, noonList.toArray()));
        hourList = new ArrayList<>();
        for (int i = 0;i<24; i++){
            if (i<10){
                hourList.add("0"+i);
            }else {
                hourList.add(i+"");
            }

        }
        mHour.setViewAdapter(new ArrayWheelAdapter<>(context, hourList.toArray()));
        minList = new ArrayList<>();
        for (int j = 0 ; j<60;j++){
            if (j<10){
                minList.add("0"+j);
            }else {
                minList.add(j+"");
            }

        }
        mMini.setViewAdapter(new ArrayWheelAdapter<>(context, minList.toArray()));

        mHour.setCurrentItem(9);
        mMini.setCurrentItem(30);
    }

    private void initView() {
        View rootView = LayoutInflater.from(context).inflate(R.layout.info_time_picker_board, null);
        setContentView(rootView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

//        mNoon = (WheelView) rootView.findViewById(R.id.id_noon);
        mHour = (WheelView) rootView.findViewById(R.id.id_hour);
        mMini = (WheelView) rootView.findViewById(R.id.id_mini);

//        mNoon.addChangingListener(new OnWheelChangedListener() {
//            @Override
//            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                noon= noonList.get(newValue);
//            }
//        });
        mHour.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                hour = hourList.get(newValue);
            }
        });
        mMini.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                mini = minList.get(newValue);
            }
        });

        rootView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootView.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedFinished.onSelected(hour+":"+mini);
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();

    }

    public interface OnSelectedFinished {
        void onSelected(String time);
    }
}
