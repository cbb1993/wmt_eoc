package com.cbb.wmt_eoc.page.remind

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbb.baselib.StringConstant
import com.cbb.baselib.adapter.CommonAdapter
import com.cbb.baselib.adapter.ViewHolder
import com.cbb.baselib.base.BaseActivity
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.check.MyTasksLoader
import com.cbb.wmt_eoc.page.schedule.ScheduleActivity
import kotlinx.android.synthetic.main.activity_left_remind.*
import kotlinx.android.synthetic.main.activity_select_task.*

/**
 * Created by 坎坎.
 * Date: 2019/12/27
 * Time: 11:19
 * describe:
 */
class LeftRemindActivity :BaseActivity<LeftRemindPresenter>(){
    override fun initPresenter(): LeftRemindPresenter? = null

    override fun getLayoutId(): Int = R.layout.activity_left_remind

    private val list = ArrayList<LeftRemind>()

    override fun initView() {
        super.initView()
        recycler_left.layoutManager = LinearLayoutManager(this)
        recycler_left.adapter =
            object : CommonAdapter<LeftRemind>(this, list, R.layout.item_left) {
                @SuppressLint("SetTextI18n")
                override fun convert(holder: ViewHolder, t: MutableList<LeftRemind>) {
                    val tv_task = holder.getView<TextView>(R.id.tv_task)
                    val tv_time = holder.getView<TextView>(R.id.tv_time)
                    val tv_point = holder.getView<TextView>(R.id.tv_point)
                    val tv_state = holder.getView<TextView>(R.id.tv_state)

                    t[holder.realPosition].run {
                        tv_task.text = "班    次：${this.planName}"
                        tv_time.text = "漏检时间：${this.gmtCreate}"
                        tv_point.text = "漏检点位：${this.pointName}"
                        tv_state.text = this.state
                    }
                }
            }
        request()


        smart_.setOnRefreshListener {
            current =1
            request()
        }
        smart_.setOnLoadMoreListener {
            current++
            request()
        }
    }

    var current = 1
    fun finishLoad() {
        smart_.finishRefresh()
        smart_.finishLoadMore()
    }

    @SuppressLint("CheckResult")
    fun request(){
        showLoading()
        val map = HashMap<String,String>()
        map["current"] = "$current"
        map["size"] = StringConstant.SIZE
        LeftRemindLoader().request(map).subscribe({ it ->
            if(current==1){
                list.clear()
            }
            list.addAll(it.records)
            recycler_left.adapter!!.notifyDataSetChanged()
            dismissLoading()
            finishLoad()
        }, {
            dismissLoading()
            showToast(ThrowableUtils.getThrowableMessage(it))
            finishLoad()
        })
    }

}