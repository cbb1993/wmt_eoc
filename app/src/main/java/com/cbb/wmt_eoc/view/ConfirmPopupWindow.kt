package com.cbb.wmt_eoc.view

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.cbb.wmt_eoc.R

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 21:19
 * describe:
 */
class ConfirmPopupWindow(context: Activity, content:String, callback: ConfirmCallback) :PopupWindow(null, ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT){
    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_confirm,null)
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tv_content = contentView.findViewById<TextView>(R.id.tv_content)
        tv_content.text = content
        contentView.findViewById<View>(R.id.tv_cancel).setOnClickListener {
            dismiss()
        }
        contentView.findViewById<View>(R.id.tv_confirm).setOnClickListener {
            callback.confirm()
            dismiss()
        }
    }
    fun show(parent: View){
        showAtLocation(parent,Gravity.CENTER,0,0)
    }

    interface ConfirmCallback{
        fun confirm()
    }
}