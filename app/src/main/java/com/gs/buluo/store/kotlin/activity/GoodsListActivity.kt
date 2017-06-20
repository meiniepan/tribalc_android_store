package com.gs.buluo.store.kotlin.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.gs.buluo.common.widget.RecycleViewDivider
import com.gs.buluo.store.R
import com.gs.buluo.store.adapter.GoodsListAdapter
import com.gs.buluo.store.bean.GoodList
import com.gs.buluo.store.bean.ListGoods
import com.gs.buluo.store.kotlin.presenter.GoodsPresenter
import com.gs.buluo.store.view.impl.IGoodsView
import kotlinx.android.synthetic.main.activity_goods.*
import java.util.*

/**
 * Created by hjn on 2016/11/16.
 */
class GoodsListActivity : KotBaseActivity(), IGoodsView {

    override fun bindView(savedInstanceState: Bundle?) {
        presenter = GoodsPresenter(this)
        adapter = GoodsListAdapter(this)
        goods_list.setAdapter(adapter)
        goods_list.setLayoutManager(GridLayoutManager(this, 2))
        goods_list.addItemDecoration(RecycleViewDivider(
                this, GridLayoutManager.HORIZONTAL, 16, resources.getColor(R.color.tint_bg)))
        goods_list.addItemDecoration(RecycleViewDivider(
                this, GridLayoutManager.VERTICAL, 12, resources.getColor(R.color.tint_bg)))
        goods_list_wrap.showProgressView()
        (presenter as GoodsPresenter).getGoodsList()

        goods_list.setLoadMoreAction { (presenter as GoodsPresenter).loadMore() }

        goods_list_wrap.setErrorAndEmptyAction { (presenter as GoodsPresenter).getGoodsList() }
    }

    override val contentLayout: Int
        get() = R.layout.activity_goods

    internal var list: MutableList<ListGoods> = ArrayList()
    private var hasMore: Boolean = false
    private var adapter: GoodsListAdapter? = null


    override fun getGoodsInfo(responseList: GoodList) {
        list.addAll(responseList.content)
        if (list.size == 0) {
            goods_list_wrap.showErrorView(getString(R.string.no_goods))
            return
        }
        goods_list_wrap.showContentView()
        adapter!!.addAll(list)
        hasMore = responseList.hasMore
        if (!hasMore) {
            adapter!!.showNoMore()
            return
        }
    }

    override fun showError(res: Int) {
        goods_list_wrap.showErrorView(getString(res))
    }

}
