package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.StandardListAdapter;
import com.gs.buluo.store.bean.GoodsStandard;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.GoodsStandardResponse;
import com.gs.buluo.store.network.GoodsApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/1/20.
 */
public class GoodsStandardActivity extends BaseActivity {
    @Bind(R.id.good_standard_list)
    RefreshRecyclerView refreshRecyclerView;

    StandardListAdapter listAdapter;
    private List<GoodsStandardMeta> standardMetas;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        final String category = getIntent().getStringExtra(Constant.ForIntent.GOODS_CATEGORY);
        findViewById(R.id.choose_standard_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listAdapter==null)return;
                if (listAdapter.getCurrentPos() == 0){
                    Intent intent = new Intent(GoodsStandardActivity.this, NewGoodsActivity.class);
                    intent.putExtra(Constant.ForIntent.GOODS_CATEGORY,category);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(GoodsStandardActivity.this, AddGoodsWithStandardActivity.class);
                    intent.putExtra(Constant.ForIntent.GOODS_STANDARD,standardMetas.get(listAdapter.getCurrentPos()));
                    intent.putExtra(Constant.ForIntent.GOODS_CATEGORY,category);
                    startActivity(intent);
                }
            }
        });
        findView(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GoodsStandardMeta goodsStandardMeta= new GoodsStandardMeta();
        goodsStandardMeta.title = getString(R.string.create_goods_standard);
        standardMetas = new ArrayList<>();
        standardMetas.add(goodsStandardMeta);
        listAdapter=new StandardListAdapter(this, standardMetas);
        refreshRecyclerView.setAdapter(listAdapter);
        TribeRetrofit.getInstance().createApi(GoodsApis.class).getStandardList(TribeApplication.getInstance().getUserInfo().getId(),category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<GoodsStandardResponse>>() {
                    @Override
                    public void onNext(BaseResponse<GoodsStandardResponse> goodListBaseResponse) {
                        List<GoodsStandardMeta> content = goodListBaseResponse.data.content;
                        listAdapter.addAll(content);
                        refreshRecyclerView.setAdapter(listAdapter);
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_standard_choose;
    }
}
