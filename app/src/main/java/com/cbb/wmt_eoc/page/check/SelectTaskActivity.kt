package com.cbb.wmt_eoc.page.check

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbb.baselib.adapter.CommonAdapter
import com.cbb.baselib.adapter.ViewHolder
import com.cbb.baselib.base.BaseActivity
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.schedule.ScheduleActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_select_task.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.MutableList

/**
 * Created by 坎坎.
 * Date: 2020/1/6
 * Time: 9:19
 * describe: 展示已接受的任务列表
 *           如果没有任务 就要去班次列表页面  选择接受
 *           选择需要执行的任务
 */
class SelectTaskActivity : BaseActivity<Any>() {
    override fun initPresenter(): Any? = null

    override fun getLayoutId(): Int = R.layout.activity_select_task
    private val list = ArrayList<MyTaskModel>()
    private var tasks = ArrayList<MyTask>()
    private var select = -1

    override fun initView() {
        super.initView()
        recycler_task.layoutManager = LinearLayoutManager(this)
        recycler_task.adapter =
            object : CommonAdapter<MyTask>(this, tasks, R.layout.item_task2) {
                @SuppressLint("SetTextI18n")
                override fun convert(holder: ViewHolder, t: MutableList<MyTask>) {
                    val root = holder.getView<View>(R.id.root)
                    val tv_time = holder.getView<TextView>(R.id.tv_time)
                    val tv_content = holder.getView<TextView>(R.id.tv_content)
                    tv_time.text =
                        "${t[holder.realPosition].gmtStart}-${t[holder.realPosition].gmtEnd}"
                    tv_content.text = t[holder.realPosition].content

                    root.isActivated = select == holder.realPosition
                    tv_time.isActivated = select == holder.realPosition
                    tv_content.isActivated = select == holder.realPosition

                    root.setOnClickListener {
                        select = holder.realPosition
                        notifyDataSetChanged()
                    }
                }
            }


        tab_.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab) {
                select = -1
                tasks.clear()
                tasks.addAll(list[p0.position].times)
                recycler_task.adapter!!.notifyDataSetChanged()
            }
        })

        btn_confirm.setOnClickListener {
            if (select < tasks.size && select > -1) {
                val myTask = tasks[select]
                isHoliday(myTask)
            }
        }
    }

    private fun canSelect(myTask:MyTask){
        val format = SimpleDateFormat("HH:mm").format(Date())
        // 结束时间大于开始时间 判断为跨天
        if(beforeMax(myTask.gmtStart,myTask.gmtEnd)){
            // 当前时间大于开始时间同时也大于结束时间  符合
            // 当前时间小于开始时间同时也小于结束时间  符合
            if(beforeMax(format,myTask.gmtStart)&&beforeMax(format,myTask.gmtEnd)){
                EventBus.getDefault().post(myTask)
                finish()
            }else if(beforeMax(myTask.gmtStart,format)&&beforeMax(myTask.gmtEnd,format)){
                EventBus.getDefault().post(myTask)
                finish()
            }else{
                showToast("当前时间不在此时间段，无法选择")
            }
        }else{
            if (beforeMax(format, myTask.gmtStart) && beforeMax(myTask.gmtEnd, format)) {
                EventBus.getDefault().post(myTask)
                finish()
            } else {
                showToast("当前时间不在此时间段，无法选择")
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun isHoliday(myTask:MyTask) {
        val map = HashMap<String, String>()
        HolidayLoader().request(map).subscribe({
            if(it == "1"){
                // 非工作日
                if(myTask.isHoliday != 0){
                    canSelect(myTask)
                }else{
                    showToast("今天是非工作日，无法选择工作日任务")
                }
            }else{
                // 工作日
                if(myTask.isHoliday == 0){
                    canSelect(myTask)
                }else{
                    showToast("今天是工作日，无法选择非工作日任务")
                }
            }
        }, {

        })
    }

    override fun onResume() {
        super.onResume()
        requestList()
    }


    private fun beforeMax(a: String, b: String): Boolean {
        val aa = a.split(":")
        val bb = b.split(":")
        val ah = aa[0].toInt()
        val bh = bb[0].toInt()
        if (ah > bh) {
            return true
        }
        if (ah < bh) {
            return false
        }
        val am = aa[1].toInt()
        val bm = bb[1].toInt()
        if (am > bm) {
            return true
        }
        if (am < bm) {
            return false
        }
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun needFinish(e:ScheduleActivityBack){
        Log.e("---","finish")
        finish()
    }

    @SuppressLint("CheckResult")
    fun requestList() {
        showLoading()
        val map = HashMap<String, String>()
        MyTasksLoader().request(map).subscribe({ it ->
            dismissLoading()
            if (it.isEmpty()) {
                startActivity(Intent(this, ScheduleActivity::class.java))
                showToast("请先选择班次")
            } else {
                list.clear()
                list.addAll(it)
               /* //过滤task 如果在在结束当前时间之前的都删除
               val iterator= list.iterator()
                while (iterator.hasNext()){
                    val task = iterator.next()
                    val timeIterator = task.times.iterator()
                    while (timeIterator.hasNext()){
                        val time = timeIterator.next()
                        val format = SimpleDateFormat("HH:mm").format(Date())
                        // 如果当前时间大于结束时间 表示可以移除
                        if(beforeMax(format,time.gmtEnd)){
                            timeIterator.remove()
                        }
                    }
                    // 该任务下时间段都不适合  该任务不显示
                    if(task.times.size ==0){
                        iterator.remove()
                    }
                }*/
                tab_.removeAllTabs()
                for (myTaskModel in list) {
                    tab_.addTab(tab_.newTab().setText(myTaskModel.planName))
                }
//                tasks.addAll(list[0].times)
            }
//            recycler_task.adapter!!.notifyDataSetChanged()
        }, {
            dismissLoading()
            showToast(ThrowableUtils.getThrowableMessage(it))
        })
    }
}