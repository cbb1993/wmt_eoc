package com.cbb.wmt_eoc.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.cbb.wmt_eoc.R

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 21:19
 * describe:
 */
class SelectCameraPopupWindow(context: Context, selectCallBack:SelectCallBack) : PopupWindow(
    null, ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT
) {
    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_select_photo, null)
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        contentView.findViewById<View>(R.id.tv_album).setOnClickListener {
            dismiss()
            selectCallBack.select(0)
        }
        contentView.findViewById<View>(R.id.tv_camera).setOnClickListener {
            dismiss()
            selectCallBack.select(1)
        }
        contentView.findViewById<View>(R.id.tv_cancel).setOnClickListener {
            dismiss()
        }

    }

    fun show(parent: View) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0)
    }

    interface SelectCallBack {
        fun select(flag: Int)
    }

}