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
import android.widget.ProgressBar
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
class ProgressAlertPopupWindow(context: Activity,listener:OnCancelClickListener) :PopupWindow(null, ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT){
    private lateinit var tv_progress:TextView
    private lateinit var progress_bar:ProgressBar
    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_progress,null)
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tv_progress = contentView.findViewById<TextView>(R.id.tv_progress)
        progress_bar = contentView.findViewById<ProgressBar>(R.id.progress_bar)

        contentView.findViewById<View>(R.id.tv_cancel).setOnClickListener {
            listener.click()
            dismiss()
        }
//        contentView.setOnClickListener {
//            dismiss()
//        }
    }
    fun show(parent: View){
        showAtLocation(parent,Gravity.CENTER,0,0)
    }
    fun updateProgress(progress:Int){
        progress_bar.progress = progress
        tv_progress.text = "${progress}%"
    }

    interface OnCancelClickListener{
        fun click()
    }

}