package com.cbb.wmt_eoc.page.schedule

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 9:30
 * describe:
 */
interface ScheduleConstract {
    interface View {
        fun requestSuccess(result: List<ScheduleRecord2>)
        fun requestFail(e: String)
    }

    interface Presenter {
        fun request()
    }
}