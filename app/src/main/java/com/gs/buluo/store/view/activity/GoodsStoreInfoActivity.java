package com.gs.buluo.store.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.CreateStoreBean;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.model.MainModel;
import com.gs.buluo.store.utils.ToastUtils;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2017/1/20.
 */
public class GoodsStoreInfoActivity extends BaseActivity implements Callback<BaseResponse<CreateStoreBean>>, View.OnClickListener {
    @Bind(R.id.info_store_name)
    EditText tvName;
    @Bind(R.id.info_sub_store_name)
    EditText tvSubName;
    @Bind(R.id.info_store_category)
    TextView tvCategory;
    @Bind(R.id.info_send_address)
    TextView tvSend;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.info_store_save).setOnClickListener(this);
        findViewById(R.id.info_store_logo).setOnClickListener(this);
        findViewById(R.id.info_store_save).setOnClickListener(this);
        findViewById(R.id.info_store_save).setOnClickListener(this);
        new MainModel().getDetailStoreInfo(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_store_activity;
    }

    @Override
    public void onResponse(Call<BaseResponse<CreateStoreBean>> call, Response<BaseResponse<CreateStoreBean>> response) {
        if (response != null && response.body() != null && response.body().code == 200) {
            initData(response.body().data);
        }
    }

    private void initData(CreateStoreBean data) {
        tvName.setText(data.name);
        tvSubName.setText(data.subbranchName);
        tvCategory.setText(data.category.toString());
        tvSend.setText(data.province + data.city + data.district + data.address);
    }

    @Override
    public void onFailure(Call<BaseResponse<CreateStoreBean>> call, Throwable t) {
        ToastUtils.ToastMessage(this, R.string.connect_fail);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_store_logo:

                break;
            case R.id.back:
                finish();
                break;
            case R.id.info_store_save:

                break;
        }
    }
}
