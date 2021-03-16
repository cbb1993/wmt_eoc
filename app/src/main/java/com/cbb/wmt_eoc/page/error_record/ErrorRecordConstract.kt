package com.cbb.wmt_eoc.page.error_record

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 9:30
 * describe:
 */
interface ErrorRecordConstract {
    interface View {
        fun requestSuccess(result: List<ErrorRecordModel>)
        fun requestFail(e: String)
    }

    interface Presenter {
        fun request(current:Int)
    }
}