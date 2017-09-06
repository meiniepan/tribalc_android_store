package com.gs.buluo.store.kotlin.activity

import com.gs.buluo.common.network.ApiException
import com.gs.buluo.common.network.BaseResponse
import com.gs.buluo.common.network.BaseSubscriber
import com.gs.buluo.store.Constant
import com.gs.buluo.store.TribeApplication
import com.gs.buluo.store.bean.RequestBodyBean.LoginBody
import com.gs.buluo.store.bean.RequestBodyBean.PhoneUpdateBody
import com.gs.buluo.store.bean.RequestBodyBean.ThirdLoginRequest
import com.gs.buluo.store.bean.RequestBodyBean.ValueRequestBody
import com.gs.buluo.store.bean.ResponseBody.CodeResponse
import com.gs.buluo.store.bean.StoreInfo
import com.gs.buluo.store.dao.StoreInfoDao
import com.gs.buluo.store.kotlin.presenter.KotBasePresenter
import com.gs.buluo.store.network.MainApis
import com.gs.buluo.store.network.TribeRetrofit
import com.gs.buluo.store.view.impl.ILoginView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by hjn on 2016/11/3.
 */
class KotLoginPresenter(val mView: ILoginView) : KotBasePresenter() {
    private var token: String? = null

    fun doLogin(params: Map<String, String>) {
        val bean = LoginBody()
        bean.phone = params[Constant.PHONE]
        bean.verificationCode = params[Constant.VERIFICATION]

        TribeRetrofit.getInstance().createApi(MainApis::class.java).doLogin(bean)
                .subscribeOn(Schedulers.io())
                .flatMap { response ->
                    val uid = response.data.assigned
                    token = response.data.token
                    val entity = StoreInfo()
                    entity.id = uid
                    entity.setToken(token)
                    TribeApplication.getInstance().userInfo = entity
                    TribeRetrofit.getInstance().createApi(MainApis::class.java).getStoreInfo(uid, uid)
                }
                .subscribeOn(Schedulers.io())
                .doOnNext { response -> setStoreInfo(response.data) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<StoreInfo>>() {
                    override fun onNext(response: BaseResponse<StoreInfo>?) {
                        mView.loginSuccess()
                    }

                    override fun onFail(e: ApiException) {
                        mView.dealWithIdentify(e.code)
                    }
                })
    }

    fun doVerify(phone: String) {
        TribeRetrofit.getInstance().createApi(MainApis::class.java).doVerify(ValueRequestBody(phone))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<CodeResponse>>(false) {
                    override fun onNext(response: BaseResponse<CodeResponse>?) {
                        mView.dealWithIdentify(202)
                    }

                    override fun onFail(e: ApiException) {
                        mView.dealWithIdentify(e.code)
                    }
                })
    }

    private fun setStoreInfo(storeInfo: StoreInfo) {
        val dao = StoreInfoDao()
        storeInfo.setToken(token)
        dao.saveBindingId(storeInfo)
    }

    fun doThirdLogin(phone: String, verify: String, wxCode: String) {
        val bean = LoginBody()
        bean.phone = phone
        bean.verificationCode = verify
        val subscribe = TribeRetrofit.getInstance().createApi(MainApis::class.java).doLogin(bean)
                .subscribeOn(Schedulers.io())
                .flatMap { response ->
                    val data = response.data
                    val uid = data.assigned
                    token = data.token

                    val entity = StoreInfo()
                    entity.id = uid
                    entity.setToken(token)
                    TribeApplication.getInstance().userInfo = entity
                    bindThird(uid, wxCode)
                    TribeRetrofit.getInstance().createApi(MainApis::class.java).getStoreInfo(uid, uid)
                }
                .doOnNext { response -> setStoreInfo(response.data) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<StoreInfo>>() {
                    override fun onNext(userBeanResponse: BaseResponse<StoreInfo>?) {
                        mView.loginSuccess()
                    }

                    override fun onFail(e: ApiException) {
                        mView.dealWithIdentify(e.code)
                    }

                })
        mSubscription.add(subscribe)
    }

    private fun bindThird(uid: String, wxCode: String) {
        val request = ThirdLoginRequest()
        request.memberType = "STORE"
        request.memberId = uid
        request.code = wxCode
        TribeRetrofit.getInstance().createApi(MainApis::class.java).bindThirdLogin(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<*>>() {
                    override fun onNext(baseResponse: BaseResponse<*>?) {}

                    override fun onFail(e: ApiException) {
                        mView.showError(e.code, e.displayMessage)
                    }
                })
    }

    fun changePhone(phone: String, verify: String) {
        val body = PhoneUpdateBody()
        body.phone = phone
        body.verificationCode = verify
        TribeRetrofit.getInstance().createApi(MainApis::class.java).updatePhone(TribeApplication.getInstance().userInfo.id, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<CodeResponse>>() {
                    override fun onNext(response: BaseResponse<CodeResponse>?) {
                        getUserInfo()
                        mView.loginSuccess()
                    }

                    override fun onFail(e: ApiException) {
                        mView.dealWithIdentify(e.code)
                    }
                })
    }

    fun getUserInfo() {
        TribeRetrofit.getInstance().createApi(MainApis::class.java).getStoreInfo(TribeApplication.getInstance().userInfo.id, TribeApplication.getInstance().userInfo.id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(object : BaseSubscriber<BaseResponse<StoreInfo>>() {
                    override fun onNext(response: BaseResponse<StoreInfo>?) {
                        token = TribeApplication.getInstance().userInfo.token
                        setStoreInfo(response!!.data)
                    }

                    override fun onFail(e: ApiException) {
                    }
                })
    }
}
