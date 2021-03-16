package com.cbb.baselib.view

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import com.cbb.baselib.R

/**
 * Created by 坎坎.
 * Date: 2020/1/7
 * Time: 10:09
 * describe:
 */
class LoadingDialog(context: Context) :Dialog(context, R.style.dialog) {
    init {
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.dialog_loading)
    }

}