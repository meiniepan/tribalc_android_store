package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.view.widget.panel.ServeTimePicker;

import butterknife.BindView;

/**
 * Created by hjn on 2017/1/20.
 */
public class ServeTimeActivity extends BaseActivity{
    private String result;
    @BindView(R.id.info_time_start)
    TextView tvStart;
    @BindView(R.id.info_time_finish)
    TextView tvFinish;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.info_time_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = tvStart.getText().toString().trim()+" - "+tvFinish.getText().toString().trim();
                Intent intent =new Intent();
                intent.putExtra(Constant.SERVE_TIME,result);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        showTimePicker(tvStart,true);

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(tvStart,false);
            }
        });

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(tvFinish,false);
            }
        });
    }

    private void showTimePicker(final TextView des, final boolean isFirst) {
        ServeTimePicker picker =new ServeTimePicker(this, new ServeTimePicker.OnSelectedFinished() {
            @Override
            public void onSelected(String time) {
                des.setText(time);
                if (isFirst){
                    showTimePicker(tvFinish,false);
                }
            }
        });
        picker.show();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_time_choose;
    }
}
