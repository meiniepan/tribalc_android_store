package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.store.R;

/**
 * Created by hjn on 2017/1/12.
 */
public class QualificationActivity extends BaseActivity{
    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.qualification_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QualificationActivity.this,CreateStateActivity.class));
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_qualification;
    }
}
