package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.GoodsStandardResponse;
import com.gs.buluo.store.bean.StoreGoodsList;
import com.gs.buluo.store.network.GoodsApis;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IStoreGoodsView;

import java.util.List;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/1/24.
 */
public class StoreGoodsPresenter extends BasePresenter<IStoreGoodsView> {
    private String sortSkip;

    public void getGoodsListFirst(final boolean published) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).getStoreGoodsListFirst(TribeApplication.getInstance().getUserInfo().getId(), published)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreGoodsList>>(false) {
                    @Override
                    public void onNext(BaseResponse<StoreGoodsList> goodListBaseResponse) {
                        StoreGoodsList data = goodListBaseResponse.data;
                        sortSkip = data.nextSkip;
                        mView.getGoodsSuccess(data,published);
                        if (!data.hasMore) {
                            if (isAttach()) mView.showNoMore(published);
                        }
                        if (data.content.size()==0&&isAttach()){
                            mView.showNoData(published);
                        }
                    }
                });
    }

    public void getMore(final boolean published) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).getStoreGoodsListMore(TribeApplication.getInstance().getUserInfo().getId(), published,sortSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreGoodsList>>(false) {
                    @Override
                    public void onNext(BaseResponse<StoreGoodsList> goodListBaseResponse) {
                        StoreGoodsList data = goodListBaseResponse.data;
                        mView.getGoodsSuccess(data,published);
                        sortSkip = data.nextSkip;
                        if (!data.hasMore) {
                            if (isAttach())mView.showNoMore(published);
                        }
                    }
                });
    }
}
