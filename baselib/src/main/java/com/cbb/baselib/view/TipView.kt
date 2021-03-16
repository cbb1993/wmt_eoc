package com.cbb.baselib.view

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import com.cbb.baselib.R

/**
 * Created by 坎坎.
 * Date: 2019/12/24
 * Time: 20:05
 * describe:
 */
class TipView(context: Context) : RelativeLayout(context) {
    private lateinit var mWindowManager: WindowManager
    private lateinit var mLayoutParams: WindowManager.LayoutParams

    init {
        initTipView()
    }

    private fun initTipView() {
        var mTipView = LayoutInflater.from(context).inflate(R.layout.layout_network_tip, null)
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayoutParams = WindowManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )
        mLayoutParams.gravity = Gravity.TOP
        mLayoutParams.x = 0
        mLayoutParams.y = 0
        mLayoutParams.windowAnimations = R.style.anim_float_view

        addView(mTipView)
    }

     fun checkNetwork(isConnected: Boolean) {
        if (isConnected) {
            if (parent != null) {
                mWindowManager.removeView(this)
            }
        } else {
            if (parent == null) {
                mWindowManager.addView(this,mLayoutParams)
            }
        }
    }
}