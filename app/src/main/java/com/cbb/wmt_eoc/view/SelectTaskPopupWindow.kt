package com.cbb.wmt_eoc.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cbb.baselib.adapter.CommonAdapter
import com.cbb.baselib.adapter.ViewHolder
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.schedule.ScheduleLoader
import com.cbb.wmt_eoc.page.schedule.ScheduleModel
import com.cbb.wmt_eoc.page.schedule.ScheduleRecord
import com.cbb.wmt_eoc.page.schedule.ScheduleRecord2
import kotlinx.android.synthetic.main.activity_schedule.*

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 21:19
 * describe:
 */
class SelectTaskPopupWindow(context: Context,call:SelectCallBack) : PopupWindow(
    null, ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT
) {
    private var recycler_task: RecyclerView
    private val list = ArrayList<ScheduleRecord2>()
    private var select = 0

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_select_task, null)
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        contentView.findViewById<View>(R.id.tv_confirm).setOnClickListener {
            call.select(list[select].workPlanId)
            dismiss()
        }

        recycler_task = contentView.findViewById(R.id.recycler_task)
        recycler_task.layoutManager = LinearLayoutManager(context)
        recycler_task.adapter =
            object : CommonAdapter<ScheduleRecord2>(context, list, R.layout.item_schedule) {
                override fun convert(holder: ViewHolder, t: MutableList<ScheduleRecord2>) {
                    val root = holder.getView<View>(R.id.root)
                    val tv_schedule = holder.getView<TextView>(R.id.tv_schedule)
                    val checkbox = holder.getView<CheckBox>(R.id.checkbox)
                    tv_schedule.text = t[holder.realPosition].planName
                    checkbox.isChecked = select == holder.realPosition

                    root.setOnClickListener {
                        select = holder.realPosition
                        notifyDataSetChanged()
                    }
                }
            }
        request()
    }

    fun show(parent: View) {
        showAtLocation(parent, Gravity.CENTER, 0, 0)
    }


    @SuppressLint("CheckResult")
    fun request() {
        val map = HashMap<String, String>()
        map["current"] = "1"
        map["size"] = "100"
        ScheduleLoader().request(map).subscribe({
            list.clear()
            list.addAll(it)
            recycler_task.adapter!!.notifyDataSetChanged()
        }, {
        })
    }

    interface SelectCallBack {
        fun select(id:String)
    }
}