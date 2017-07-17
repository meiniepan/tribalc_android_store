package com.gs.buluo.store.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import com.gs.buluo.common.utils.ToastUtils
import com.gs.buluo.store.Constant
import com.gs.buluo.store.R
import com.gs.buluo.store.TribeApplication
import com.gs.buluo.store.adapter.BankCardListAdapter
import com.gs.buluo.store.bean.BankCard
import com.gs.buluo.store.kotlin.activity.KotBaseActivity
import com.gs.buluo.store.presenter.BankCardPresenter
import com.gs.buluo.store.view.impl.ICardView
import kotlinx.android.synthetic.main.activity_card_list.*

/**
 * Created by hjn on 2016/11/23.
 */
class BankCardActivity : KotBaseActivity(), ICardView {
    private var adapter: BankCardListAdapter? = null

    private var canDelete = false
    private var isFromCash: Boolean = false

    override fun bindView(savedInstanceState: Bundle?) {
        mPresenter = BankCardPresenter(this)
        isFromCash = intent.getBooleanExtra(Constant.CASH_FLAG, false)
        adapter = BankCardListAdapter(this)
        card_list!!.adapter = adapter
        getData()
        card_add_card.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@BankCardActivity, AddBankCardActivity::class.java))
        })
        card_manager!!.setOnClickListener {
            if (canDelete) {
                hideDeleteView()
            } else {
                showDeleteView()
            }
        }
        if (isFromCash) {
            card_list!!.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val card = adapter!!.getItem(position) as BankCard
                val intent = Intent()
                intent.putExtra(Constant.BANK_CARD, card)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
        card_status_layout!!.setErrorAction { getData() }
    }

    override val contentLayout: Int
        get() = R.layout.activity_card_list

    private fun getData() {
        card_status_layout!!.showProgressView()
        (mPresenter as BankCardPresenter).getCardList(TribeApplication.getInstance().userInfo.id)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        getData()
    }

    private fun hideDeleteView() {
        adapter!!.hideDelete()
        canDelete = false
        card_manager!!.setText(R.string.manage)
    }

    private fun showDeleteView() {
        adapter!!.showDelete()
        card_manager!!.setText(R.string.finish)
        canDelete = true
    }


    override fun getCardInfoSuccess(data: List<BankCard>) {
        if (data.isEmpty()) {
            card_status_layout!!.showEmptyView("您还未添加银行卡")
            return
        }
        card_status_layout!!.showContentView()
        adapter!!.setData(data)
    }

    override fun showError(res: Int) {
        card_status_layout!!.showErrorView(getString(res))
    }
}
