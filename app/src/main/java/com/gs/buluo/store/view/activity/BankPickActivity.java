package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.BankListAdapter;
import com.gs.buluo.store.bean.BankCard;
import com.gs.buluo.store.network.MoneyApis;
import com.gs.buluo.store.network.TribeRetrofit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BankPickActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.bank_pick_list)
    ListView mListView;
    @BindView(R.id.bank_status_list)
    StatusLayout statusLayout;

    private List<BankCard> mList =new ArrayList<>();
    private BankListAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        adapter = new BankListAdapter(this);
        mListView.setOnItemClickListener(this);
        setAllBank();
    }


    private void setAllBank() {
        statusLayout.showProgressView();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getAllCardList(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<BankCard>>>() {
                    @Override
                    public void onNext(BaseResponse<List<BankCard>> listBaseResponse) {
                        statusLayout.showContentView();
                        mList.addAll(listBaseResponse.data);
                        if (mList.size()==0){
                            statusLayout.showErrorView("您尚未添加银行卡");
                        }
                        mListView.setAdapter(adapter);
                        adapter.setData(mList);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        statusLayout.showErrorView(e.getDisplayMessage());
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_back_pick;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(Constant.ForIntent.FLAG, mList.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}
