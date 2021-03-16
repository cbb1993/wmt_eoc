package com.cbb.wmt_eoc.page.schedule

import android.annotation.SuppressLint
import android.util.Log
import com.cbb.baselib.net.ThrowableUtils

/**
 * Created by 坎坎.
 * Date: 2019/12/24
 * Time: 22:51
 * describe:
 */
class SchedulePresenter(var view: ScheduleConstract.View): ScheduleConstract.Presenter{
    @SuppressLint("CheckResult")
    override fun request() {
        val map = HashMap<String, String>()
        map["current"] = "1"
        map["size"] = "100"
        ScheduleLoader().request(map).subscribe({
            view.requestSuccess(it)
        }, {
            view.requestFail(ThrowableUtils.getThrowableMessage(it))
        })
    }

}