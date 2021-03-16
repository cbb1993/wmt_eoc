package com.cbb.wmt_eoc.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.check.PointStateLoader
import com.cbb.wmt_eoc.page.check.TaskStateChanged
import com.cbb.wmt_eoc.page.error_report.ReportErrorActivity
import org.greenrobot.eventbus.EventBus

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 21:19
 * describe:
 */
class AlertPopupWindow(context: Activity, content:String) :PopupWindow(null, ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT){
    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_check_alert,null)
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tv_content = contentView.findViewById<TextView>(R.id.tv_content)
        tv_content.text = content
        contentView.findViewById<View>(R.id.tv_confirm).setOnClickListener {
            dismiss()
        }
//        contentView.setOnClickListener {
//            dismiss()
//        }
    }
    fun show(parent: View){
        showAtLocation(parent,Gravity.CENTER,0,0)
    }


}