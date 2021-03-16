package com.cbb.wmt_eoc.page.error_record

import android.annotation.SuppressLint
import com.cbb.baselib.StringConstant
import com.cbb.baselib.net.ThrowableUtils

/**
 * Created by 坎坎.
 * Date: 2019/12/24
 * Time: 22:51
 * describe:
 */
class ErrorRecordPresenter(var view: ErrorRecordConstract.View): ErrorRecordConstract.Presenter{
    @SuppressLint("CheckResult")
    override fun request(current:Int) {
        val map = HashMap<String, String>()
        map["current"] = "$current"
        map["size"] = StringConstant.SIZE
        ErrorRecordLoader().request(map).subscribe({
            view.requestSuccess(it)
        }, {
            view.requestFail(ThrowableUtils.getThrowableMessage(it))
        })
    }

}