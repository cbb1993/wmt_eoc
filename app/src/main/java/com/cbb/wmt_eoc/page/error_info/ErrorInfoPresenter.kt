package com.cbb.wmt_eoc.page.error_info

import android.annotation.SuppressLint
import com.cbb.baselib.net.ThrowableUtils

/**
 * Created by 坎坎.
 * Date: 2019/12/24
 * Time: 22:51
 * describe:
 */
class ErrorInfoPresenter(var view: ErrorInfoConstract.View): ErrorInfoConstract.Presenter{
    @SuppressLint("CheckResult")
    override fun request(id:String) {
        val map = HashMap<String, String>()
        map["faultId"] = id
        ErrorInfoLoader().request(map).subscribe({
            view.requestSuccess(it)
        }, {
            view.requestFail(ThrowableUtils.getThrowableMessage(it))
        })
    }

}