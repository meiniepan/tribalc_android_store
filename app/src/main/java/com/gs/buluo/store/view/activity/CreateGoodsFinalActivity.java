package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.GoodsPriceAndRepertory;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.store.bean.RequestBodyBean.CreateGoodsRequestBody;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.bean.ResponseBody.CreateGoodsResponse;
import com.gs.buluo.store.bean.SerializableHashMap;
import com.gs.buluo.store.network.GoodsService;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.utils.AppManager;
import com.gs.buluo.store.utils.ToastUtils;

import org.xutils.x;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2017/1/23.
 */
public class CreateGoodsFinalActivity extends BaseActivity implements View.OnClickListener, Callback<CreateGoodsResponse> {
    @Bind(R.id.create_goods_number)
    EditText etNum;
    @Bind(R.id.create_goods_fee)
    EditText etFee;
    private GoodsMeta goodsMeta;
    private GoodsStandardMeta standardMeta;

    private boolean published;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        goodsMeta = intent.getParcelableExtra(Constant.ForIntent.META);
        Bundle bundle = intent.getExtras();
        standardMeta = bundle.getParcelable("data");
        SerializableHashMap<String, GoodsPriceAndRepertory> serializableHashMap = (SerializableHashMap<String, GoodsPriceAndRepertory>) bundle.getSerializable("map");
        if (serializableHashMap!=null) standardMeta.priceAndRepertoryMap = serializableHashMap.getMap();

        findView(R.id.create_goods_save).setOnClickListener(this);
        findView(R.id.create_goods_publish).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_create_goods_final;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_goods_save:
                createGoods(false);
                published=false;
                break;
            case R.id.create_goods_publish:
                createGoods(true);
                published =true;
                break;

        }
    }

    private void createGoods(boolean b) {
        if (etFee.length() != 0)goodsMeta.expressFee = Float.parseFloat(etFee.getText().toString().trim());
        goodsMeta.number = etNum.getText().toString().trim();
        goodsMeta.published = b;
        CreateGoodsRequestBody body = new CreateGoodsRequestBody();
        body.goodsMeta = goodsMeta;
        if (standardMeta!=null){
            body.standardMeta = standardMeta;
        }

        TribeRetrofit.getInstance().createApi(GoodsService.class).createGoods(TribeApplication.getInstance().getUserInfo().getId(), body).enqueue(this);
    }

    @Override
    public void onResponse(Call<CreateGoodsResponse> call, Response<CreateGoodsResponse> response) {
        if (response != null && response.body() != null && response.body().code == 201) {
            ToastUtils.ToastMessage(this, R.string.add_success);
            Intent intent = new Intent(getCtx(), MainActivity.class);
            intent.putExtra(Constant.ForIntent.FLAG,Constant.GOODS);
            intent.putExtra(Constant.PUBLISHED,published);
            startActivity(intent);
            finish();
        } else {
            ToastUtils.ToastMessage(this, R.string.connect_fail);
        }
    }

    @Override
    public void onFailure(Call<CreateGoodsResponse> call, Throwable t) {
        ToastUtils.ToastMessage(this, R.string.connect_fail);
    }
}
