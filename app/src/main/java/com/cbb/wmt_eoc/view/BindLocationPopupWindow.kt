package com.cbb.wmt_eoc.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cbb.baselib.adapter.CommonAdapter
import com.cbb.baselib.adapter.ViewHolder
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.bind.*

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 21:19
 * describe:
 */
class BindLocationPopupWindow(context: Context, selectCallBack: SelectCallBack) : PopupWindow(
    null, ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT
) {
    private var recycler_build: RecyclerView
    private val list = ArrayList<FloorModel>()
    private val points = ArrayList<Point>()
    private var buildSelectPosition = -1
    private var floorSelectPosition = -1
    private var pointSelect :Point?= null
    private var context: Context = context

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_bind_location, null)
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        recycler_build = contentView.findViewById(R.id.recycler_build)
        recycler_build.layoutManager = LinearLayoutManager(context)

        recycler_build.adapter =
            object : CommonAdapter<FloorModel>(context, list, R.layout.item_build) {
                @SuppressLint("SetTextI18n")
                override fun convert(holder: ViewHolder, t: MutableList<FloorModel>) {
                    val tv_build = holder.getView<TextView>(R.id.tv_build)
                    val recycler_floor = holder.getView<RecyclerView>(R.id.recycler_floor)
                    t[holder.realPosition].run {
                        tv_build.text = this.buildingName
                    }
                    tv_build.setOnClickListener {
                        if (buildSelectPosition == holder.realPosition) {
                            buildSelectPosition = -1
                            notifyItemChanged(holder.realPosition)
                        } else {
                            if (t[holder.realPosition].floors.isEmpty()) {
                                Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            floorSelectPosition = -1
                            buildSelectPosition = holder.realPosition
                            notifyDataSetChanged()
                        }
                    }
                    if (buildSelectPosition == holder.realPosition) {
                        recycler_floor.visibility = View.VISIBLE
                        recycler_floor.layoutManager = LinearLayoutManager(context)
                        recycler_floor.adapter =
                            object : CommonAdapter<Floor>(
                                context,
                                t[holder.realPosition].floors,
                                R.layout.item_floor
                            ) {
                                @SuppressLint("SetTextI18n")
                                override fun convert(
                                    viewHolder: ViewHolder,
                                    data: MutableList<Floor>
                                ) {
                                    val tv_floor = viewHolder.getView<TextView>(R.id.tv_floor)
                                    data[viewHolder.realPosition].run {
                                        tv_floor.text = this.floorName
                                    }
                                    tv_floor.setOnClickListener {
                                        if (floorSelectPosition == viewHolder.realPosition) {
                                            floorSelectPosition = -1
                                            recycler_floor.adapter!!.notifyItemChanged(viewHolder.realPosition)
                                        } else {
                                            floorSelectPosition = viewHolder.realPosition
                                            requestPoint(data[viewHolder.realPosition].id)
                                        }
                                    }

                                    val recycler_point =
                                        viewHolder.getView<RecyclerView>(R.id.recycler_point)

                                    if (floorSelectPosition == viewHolder.realPosition) {
                                        recycler_point.visibility = View.VISIBLE
                                        recycler_point.layoutManager = LinearLayoutManager(context)
                                        recycler_point.adapter =
                                            object : CommonAdapter<Point>(
                                                context,
                                                points,
                                                R.layout.item_point
                                            ) {
                                                @SuppressLint("SetTextI18n")
                                                override fun convert(
                                                    myViewHolder: ViewHolder,
                                                    myData: MutableList<Point>
                                                ) {
                                                    val tv_point =
                                                        myViewHolder.getView<TextView>(R.id.tv_point)
                                                    val checkbox =
                                                        myViewHolder.getView<CheckBox>(R.id.checkbox)
                                                    myData[myViewHolder.realPosition].run {
                                                        tv_point.text = this.pointName
                                                    }
                                                    checkbox.isChecked =
                                                        pointSelect == myData[myViewHolder.realPosition]

                                                    tv_point.setOnClickListener {
                                                        pointSelect =
                                                            myData[myViewHolder.realPosition]
                                                        notifyDataSetChanged()
                                                    }
                                                }
                                            }
                                    } else recycler_point.visibility = View.GONE
                                }
                            }
                    } else {
                        recycler_floor.visibility = View.GONE
                    }
                }
            }

        contentView.findViewById<View>(R.id.btn_submit).setOnClickListener {
            if(pointSelect == null){
                Toast.makeText(context, "请选择点位", Toast.LENGTH_SHORT)
                    .show()
            }else{
                selectCallBack.select(pointSelect!!)
                dismiss()
            }
        }
        contentView.findViewById<View>(R.id.view_bg).setOnClickListener {
           dismiss()
        }

        request()
    }

    @SuppressLint("CheckResult")
    fun request() {
        val map = HashMap<String, String>()
        FloorListLoader().request(map).subscribe({
            list.clear()
            list.addAll(it)
            recycler_build.adapter!!.notifyDataSetChanged()
        }, {
            Toast.makeText(context, ThrowableUtils.getThrowableMessage(it), Toast.LENGTH_SHORT)
                .show()
        })
    }

    @SuppressLint("CheckResult")
    fun requestPoint(id: String) {
        val map = HashMap<String, String>()
        map["floorId"] = id
        map["current"] = "1"
        map["size"] = "100"
        PointListLoader().request(map).subscribe({
            points.clear()
            points.addAll(it.records)
            recycler_build.adapter!!.notifyItemChanged(buildSelectPosition)
        }, {
            Toast.makeText(context, ThrowableUtils.getThrowableMessage(it), Toast.LENGTH_SHORT)
                .show()
        })
    }

    fun show(parent: View) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0)
    }

    interface SelectCallBack {
        fun select(point:Point)
    }

}