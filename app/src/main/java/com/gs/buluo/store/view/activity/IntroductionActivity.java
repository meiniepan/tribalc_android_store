package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.utils.zxing.decoding.Intents;

import butterknife.Bind;

/**
 * Created by admin on 2017/2/2.
 */
public class IntroductionActivity extends BaseActivity{
    @Bind(R.id.store_introduction_text)
    EditText etIntro;
    @Bind(R.id.introduction_title)
    TextView tvTitle;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        String flag = getIntent().getStringExtra(Constant.ForIntent.FLAG);
        String intro = getIntent().getStringExtra(Constant.ForIntent.INTRODUCTION);
        etIntro.setText(intro);
        if (TextUtils.equals(flag,Constant.GOODS)){
            tvTitle.setText("商品描述");
            etIntro.setHint("请输入商品描述");
        }

        findView(R.id.store_introduct_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = etIntro.getText().toString().trim();
                Intent intent =new Intent();
                intent.putExtra(Constant.ForIntent.INTRODUCTION,info);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_store_introduction;
    }
}
