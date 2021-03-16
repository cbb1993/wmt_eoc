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
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cbb.baselib.adapter.CommonAdapter
import com.cbb.baselib.adapter.ViewHolder
import com.cbb.baselib.net.RetrofitServiceManager
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.bind.*

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 21:19
 * describe:
 */
class BindHostPopupWindow(context: Context) : PopupWindow(
    null, ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT
) {

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_bind_host, null)
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isFocusable = true

        val et_host = contentView.findViewById<EditText>(R.id.et_host)

        contentView.findViewById<View>(R.id.tv_cancel).setOnClickListener {
            dismiss()
        }
        contentView.findViewById<View>(R.id.tv_confirm).setOnClickListener {
            RetrofitServiceManager.getInstance().reInit(et_host.text.toString())
            dismiss()
        }
        contentView.findViewById<View>(R.id.view_bg).setOnClickListener {
            dismiss()
        }
    }

    fun show(parent: View) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0)
    }

}