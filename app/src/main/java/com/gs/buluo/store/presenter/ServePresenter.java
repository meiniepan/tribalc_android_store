package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.ServeResponseBody;
import com.gs.buluo.store.network.ServeApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IServeView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/29.
 */
public class ServePresenter extends BasePresenter<IServeView> {
    private String nextSkip;

    public void getServeListFirst(String category, String sort) {
        String coordinate;
        if (sort.contains(Constant.SORT_COORDINATE_DESC)) {
            coordinate = TribeApplication.getInstance().getPosition().longitude + "," + TribeApplication.getInstance().getPosition().latitude;
            TribeRetrofit.getInstance().createApi(ServeApis.class).getServiceListFirst(category, 20, sort, coordinate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<ServeResponseBody>>() {
                        @Override
                        public void onNext(BaseResponse<ServeResponseBody> goodListBaseResponse) {
                            ServeResponseBody data = goodListBaseResponse.data;
                            nextSkip = data.nextSkip;
                            if (isAttach()) mView.getServerSuccess(data);
                        }
                    });
        } else {
            TribeRetrofit.getInstance().createApi(ServeApis.class).getServiceListFirst(category, 20, sort)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<ServeResponseBody>>() {
                        @Override
                        public void onNext(BaseResponse<ServeResponseBody> goodListBaseResponse) {
                            ServeResponseBody data = goodListBaseResponse.data;
                            nextSkip = data.nextSkip;
                            if (isAttach()) mView.getServerSuccess(data);
                        }
                    });
        }
    }

    public void getServeMore(String category, String sort) {
        String coordinate;
        if (sort.contains(Constant.SORT_COORDINATE_DESC)) {
            coordinate = TribeApplication.getInstance().getPosition().longitude + "," + TribeApplication.getInstance().getPosition().latitude;
            TribeRetrofit.getInstance().createApi(ServeApis.class).getServiceList(category, 20, sort, coordinate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<ServeResponseBody>>() {
                        @Override
                        public void onNext(BaseResponse<ServeResponseBody> goodListBaseResponse) {
                            if (isAttach()) mView.getServerSuccess(goodListBaseResponse.data);
                        }
                    });
        } else {
            TribeRetrofit.getInstance().createApi(ServeApis.class).getServiceList(category, 20, sort, nextSkip)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<ServeResponseBody>>() {
                        @Override
                        public void onNext(BaseResponse<ServeResponseBody> goodListBaseResponse) {
                            if (isAttach()) mView.getServerSuccess(goodListBaseResponse.data);
                        }
                    });
        }
    }
}
