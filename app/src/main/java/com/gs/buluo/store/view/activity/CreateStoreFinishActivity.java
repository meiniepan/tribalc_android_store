package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.store.R;

/**
 * Created by hjn on 2017/1/18.
 */
public class CreateStoreFinishActivity extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.create_to_auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateStoreFinishActivity.this,Authentication1Activity.class));
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getCtx(),MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_create_finish;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
