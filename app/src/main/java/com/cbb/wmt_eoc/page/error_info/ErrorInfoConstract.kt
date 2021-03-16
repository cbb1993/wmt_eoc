package com.cbb.wmt_eoc.page.error_info

import com.cbb.wmt_eoc.page.error_record.ErrorRecordModel

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 9:30
 * describe:
 */
interface ErrorInfoConstract {
    interface View {
        fun requestSuccess(result: ErrorRecordModel)
        fun requestFail(e: String)
    }

    interface Presenter {
        fun request(id: String)
    }
}