package com.cbb.wmt_eoc.page.schedule

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbb.baselib.adapter.CommonAdapter
import com.cbb.baselib.adapter.ViewHolder
import com.cbb.baselib.base.BaseActivity
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.check.ScheduleActivityBack
import kotlinx.android.synthetic.main.activity_schedule.*
import org.greenrobot.eventbus.EventBus
import java.lang.StringBuilder

/**
 * Created by 坎坎.
 * Date: 2019/12/27
 * Time: 15:44
 * describe:
 */
class ScheduleActivity : BaseActivity<SchedulePresenter>(), ScheduleConstract.View {

    override fun initPresenter(): SchedulePresenter? = SchedulePresenter(this)
    override fun getLayoutId(): Int = R.layout.activity_schedule
    private val list = ArrayList<ScheduleRecord2>()
    override fun initView() {
        super.initView()
        recycler_schedule.layoutManager = LinearLayoutManager(this)
        recycler_schedule.adapter =
            object : CommonAdapter<ScheduleRecord2>(this, list, R.layout.item_schedule) {
                override fun convert(holder: ViewHolder, t: MutableList<ScheduleRecord2>) {
                    val root = holder.getView<View>(R.id.root)
                    val tv_schedule = holder.getView<TextView>(R.id.tv_schedule)
                    val checkbox = holder.getView<CheckBox>(R.id.checkbox)
                    tv_schedule.text = t[holder.realPosition].planName
                    root.setOnClickListener {
                        checkbox.isChecked = !checkbox.isChecked
                        t[holder.realPosition].check = checkbox.isChecked
                    }
                }
            }

        btn_confirm.setOnClickListener {
            request()
        }
    }

    override fun initData() {
        super.initData()
        presenter!!.request()
    }


    @SuppressLint("CheckResult")
    fun request() {
        val builder = StringBuilder()
        list.forEach {
            if(it.check!=null&& it.check ==true){
                builder.append(it.workPlanId).append(",")
            }
        }
        if(builder.isEmpty()){
            showToast("请选择任务")
            return
        }
        builder.deleteCharAt(builder.length - 1)
        val map = HashMap<String, String>()
        map["ids"] = builder.toString()
        AddTaskLoader().request(map).subscribe({
            showToast("任务领取成功")
            finish()
        }, {
            showToast(ThrowableUtils.getThrowableMessage(it))
        })
    }

    override fun requestSuccess(result: List<ScheduleRecord2>) {
        list.clear()
        list.addAll(result)
        recycler_schedule.adapter!!.notifyDataSetChanged()
    }

    override fun requestFail(e: String) {
        showToast(e)
    }

    override fun onBackPressed() {
        Log.e("-------","----------")
        EventBus.getDefault().post(ScheduleActivityBack())
        super.onBackPressed()
    }
}