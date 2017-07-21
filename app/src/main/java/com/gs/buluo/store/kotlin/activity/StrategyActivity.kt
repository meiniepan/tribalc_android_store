package com.gs.buluo.store.kotlin.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.gs.buluo.common.network.BaseResponse
import com.gs.buluo.common.network.BaseSubscriber
import com.gs.buluo.store.R
import com.gs.buluo.store.TribeApplication
import com.gs.buluo.store.bean.Privilege
import com.gs.buluo.store.bean.ResponseBody.PrivilegeResponse
import com.gs.buluo.store.network.MoneyApis
import com.gs.buluo.store.network.TribeRetrofit
import com.gs.buluo.store.utils.TribeDateUtils
import kotlinx.android.synthetic.main.activity_stategy.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by hjn on 2017/7/14.
 */

class StrategyActivity : KotBaseActivity() {
    override fun bindView(savedInstanceState: Bundle?) {
        val uid = TribeApplication.getInstance().userInfo.id
        showLoadingDialog()
        TribeRetrofit.getInstance().createApi(MoneyApis::class.java).getAllPrivilage(uid, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<BaseResponse<PrivilegeResponse>>() {
                    override fun onNext(goodListBaseResponse: BaseResponse<PrivilegeResponse>?) {
                        setData(goodListBaseResponse!!.data.privileges)
                    }
                })
    }

    private fun setData(data: List<Privilege>?) {
        strategy_list.adapter = StrategyItemAdapter(this, data)
    }

    override val contentLayout: Int
        get() = R.layout.activity_stategy

}

class StrategyItemAdapter(var context: Context, var data: List<Privilege>?) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.strategy_item, parent, false)
        }
        var value = convertView!!.findViewById(R.id.strategy_item_value) as TextView
        var time = convertView.findViewById(R.id.strategy_item_time) as TextView
        var limit = convertView.findViewById(R.id.strategy_item_limit) as TextView

        val pri = data!![position]
        var va = ""
        when (pri.type) {
            Privilege.PrivilegeType.DISCOUNT -> {
                va = (pri.value.toFloat()*10).toString() + "折"
            }
            Privilege.PrivilegeType.REDUCE -> {
                va = "满" + pri.condition + "减" + pri.value + "元"
            }
            Privilege.PrivilegeType.ALIQUOT -> {
                va = "每满" + pri.condition + "减" + pri.value + "元"
            }
        }
        value.text = va
        if (pri.activityTime!=null){
            val beginClock = add0ToClock(pri.activityTime[0] / 3600, pri.activityTime[0] % 3600 / 60)
            val endClock = add0ToClock(pri.activityTime[1] / 3600, pri.activityTime[1] % 3600 / 60)

            if (pri.activityTime[0] < pri.activityTime[1]) {   //同一天
                time.text = "(使用时间:每天$beginClock-$endClock)"
            } else {
                time.text = "(使用时间:每天$beginClock-次日$endClock)"
            }
        }

        val start = TribeDateUtils.dateFormat5(Date(pri.startDate))
        val end = TribeDateUtils.dateFormat5(Date(pri.endDate))
        limit.text = "$start 至 $end"

        return convertView
    }

    private fun add0ToClock(startTime1: Int, endTime1: Int): String {
        var s1: String = ""
        var e1: String = ""
        if (startTime1 < 10) {
            s1 = "0" + startTime1.toString()
        } else {
            s1 = startTime1.toString()
        }
        if (endTime1 < 10) {
            e1 = "0" + endTime1.toString()
        } else {
            e1 = endTime1.toString()
        }
        var beginClock = s1 + ":" + e1
        return beginClock
    }

    override fun getItem(position: Int): Any {
        return data!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data!!.size
    }

}
