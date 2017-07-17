package com.gs.buluo.store.kotlin.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gs.buluo.common.utils.ToastUtils
import com.gs.buluo.common.widget.CustomAlertDialog
import com.gs.buluo.store.Constant
import com.gs.buluo.store.R
import com.gs.buluo.store.bean.StoreMeta
import com.gs.buluo.store.bean.StoreSetMealCreation
import com.gs.buluo.store.kotlin.presenter.StoreInfoPresenter
import com.gs.buluo.store.view.activity.PhotoActivity
import com.gs.buluo.store.view.impl.IInfoView
import kotlinx.android.synthetic.main.activity_store_activity.*

/**
 * Created by hjn on 2017/1/20.
 */
class GoodsStoreInfoActivity : KotBaseActivity(), View.OnClickListener, IInfoView {
    private var storeBean: StoreMeta? = null

    override fun bindView(savedInstanceState: Bundle?) {
        info_store_logo.setOnClickListener(this)
        info_store_save.setOnClickListener(this)
        ll_store_info_address.setOnClickListener(this)
        info_store_auth.setOnClickListener(this)
        showLoadingDialog()
        mPresenter = StoreInfoPresenter(this)
        (mPresenter as StoreInfoPresenter).getDetailStoreInfo()
    }

    override val contentLayout: Int
        get() = R.layout.activity_store_activity

    override fun onClick(v: View) {
        val intent = Intent()
        when (v.id) {
            R.id.info_store_logo -> {
                intent.setClass(ctx, PhotoActivity::class.java)
                intent.putExtra(Constant.ForIntent.PHOTO_TYPE, "logo")
                startActivityForResult(intent, 200)
            }
            R.id.info_store_save -> updateStoreInfo()
            R.id.info_store_auth -> {
            }
        }
    }

    private fun updateStoreInfo() {
        showLoadingDialog(R.string.saving_now)
        storeBean!!.name = info_store_name.text.toString()
        storeBean!!.desc = info_store_introduction!!.text.toString().trim { it <= ' ' }
        (mPresenter as StoreInfoPresenter).updateStore(storeBean!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 200 && resultCode == 201) {  //logo
            storeBean!!.logo = data.getStringExtra(Constant.LOGO)
            store_info_logo!!.text = "1张"
        } else if (data != null && requestCode == 202 && resultCode == Activity.RESULT_OK) {
            val area = data.getStringExtra(Constant.AREA)
            val address = data.getStringExtra(Constant.ADDRESS)
            val arrs = area.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            storeBean!!.province = arrs[0]
            storeBean!!.city = arrs[1]
            storeBean!!.district = arrs[2]
            storeBean!!.address = address
            val lan = data.getDoubleExtra(Constant.LATITUDE, 0.0)
            val lon = data.getDoubleExtra(Constant.LONGITUDE, 0.0)
            storeBean!!.coordinate = doubleArrayOf(lon, lan)
            val totalAddress = storeBean!!.province + storeBean!!.city + storeBean!!.district + storeBean!!.address
            info_send_address.text = if (storeBean!!.province == null) "" else totalAddress
        }
    }

    override fun onBackPressed() {
        back()
    }

    private fun back() {
        CustomAlertDialog.Builder(this).setTitle(getString(R.string.reminder))
                .setMessage(getString(R.string.save_update)).
                setPositiveButton(getString(R.string.yes)) { _, _ -> updateStoreInfo() }
                .setNegativeButton(getString(R.string.not_save)) { _, _ -> finish() }.create().show()
    }

    override fun updateSuccess() {
        dismissDialog()
        ToastUtils.ToastMessage(this, R.string.update_success)
        finish()
    }

    override fun setData(data: StoreMeta) {
        dismissDialog()
        storeBean = data
        info_store_name.setText(data.name)
        info_store_phone!!.text = data.phone
        if (data.category != null) info_store_category!!.text = data.category.toString()
        val totalAddress = data.province + data.city + data.district + data.address
        info_send_address!!.text = if (data.province == null) "" else totalAddress
        info_store_introduction!!.setText(data.desc)
    }

    override fun setMealData(mealCreation: StoreSetMealCreation) {}

    override fun showError(res: Int) {
        dismissDialog()
        ToastUtils.ToastMessage(this, res)
    }
}
