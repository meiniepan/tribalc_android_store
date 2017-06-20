package com.gs.buluo.store.kotlin.presenter

import com.gs.buluo.common.network.BaseResponse
import com.gs.buluo.common.network.BaseSubscriber
import com.gs.buluo.store.TribeApplication
import com.gs.buluo.store.bean.ResponseBody.CodeResponse
import com.gs.buluo.store.bean.StoreMeta
import com.gs.buluo.store.bean.StoreSetMealCreation
import com.gs.buluo.store.dao.StoreInfoDao
import com.gs.buluo.store.eventbus.SelfEvent
import com.gs.buluo.store.network.MainApis
import com.gs.buluo.store.network.TribeRetrofit
import com.gs.buluo.store.view.impl.IInfoView
import org.greenrobot.eventbus.EventBus
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by hjn on 2017/2/9.
 */
class StoreInfoPresenter(var mView: IInfoView) : KotBasePresenter() {

    fun updateStore(storeBean: StoreMeta) {
        TribeRetrofit.getInstance().createApi(MainApis::class.java).updateStore(TribeApplication.getInstance().userInfo.id, storeBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<CodeResponse>>() {
                    override fun onNext(response: BaseResponse<CodeResponse>?) {
                        saveStore(storeBean)
                        mView.updateSuccess()
                    }
                })
    }

    private fun saveStore(data: StoreMeta) {
        val storeInfoDao = StoreInfoDao()
        val first = storeInfoDao.findFirst()
        first.setLogo(data.getLogo())
        first.setName(data.getName())
        storeInfoDao.update(first)
        EventBus.getDefault().post(SelfEvent())
    }

    fun updateMeal(mealCreation: StoreSetMealCreation) {
        TribeRetrofit.getInstance().createApi(MainApis::class.java).updateMeal(mealCreation.id, TribeApplication.getInstance().userInfo.id, mealCreation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<CodeResponse>>() {
                    override fun onNext(response: BaseResponse<CodeResponse>?) {}
                })
    }

    fun getDetailStoreInfo() {
        val id = TribeApplication.getInstance().userInfo.id
        TribeRetrofit.getInstance().createApi(MainApis::class.java).getStoreMeta(id, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<StoreMeta>>() {
                    override fun onNext(response: BaseResponse<StoreMeta>?) {
                        mView.setData(response!!.data)
                    }
                })
    }

    fun getSetMeal() {
        TribeRetrofit.getInstance().createApi(MainApis::class.java).getCreateSetMeal(TribeApplication.getInstance().userInfo.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<List<StoreSetMealCreation>>>() {
                    override fun onNext(response: BaseResponse<List<StoreSetMealCreation>>?) {
                        val data = response!!.data
                        mView.setMealData(data[0])
                    }
                })
    }
}
