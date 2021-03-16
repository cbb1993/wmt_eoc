package com.cbb.baselib.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.cbb.baselib.base.BaseApplication
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


/**
 * Created by 坎坎.
 * Date: 2019/12/24
 * Time: 19:55
 * describe: 屏幕相关处理
 */
class ScreenUtil {
    companion object {
        fun initStatusBar(context: Activity, colorId: Int) {
            val window = context.window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = colorId
                return
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        /** dp转px  */
        fun dp2px(context: Context, dpVal: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpVal,
                context.resources.displayMetrics
            ).toInt()
        }

        /** sp转px  */
        fun sp2px(context: Context, spVal: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                spVal,
                context.resources.displayMetrics
            ).toInt()
        }

        /** px转dp  */
        fun px2dp(context: Context, pxVal: Float): Float {
            val scale = context.resources.displayMetrics.density
            return pxVal / scale
        }

        /** px转sp  */
        fun px2sp(context: Context, pxVal: Float): Float {
            return pxVal / context.resources.displayMetrics.scaledDensity
        }

        /**
         * 获取屏幕宽度
         */
        fun getWidth(activity: Activity): Float {
            val dm = DisplayMetrics()
            // 取得窗口属性
            activity.windowManager.defaultDisplay.getMetrics(dm)
            // 窗口的宽度
            return dm.widthPixels.toFloat()
        }

        /**
         * 获取屏幕高度
         */
        fun getHeight(activity: Activity): Float {
            val dm = DisplayMetrics()
            // 取得窗口属性
            activity.windowManager.defaultDisplay.getMetrics(dm)
            // 窗口的高度
            return dm.heightPixels.toFloat()
        }

        fun getStatusBarHeight(activity: Context): Float {
            var result = 0f
            val resourceId =
                activity.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = activity.resources.getDimension(resourceId)
            }
            return result
        }
    }
}
