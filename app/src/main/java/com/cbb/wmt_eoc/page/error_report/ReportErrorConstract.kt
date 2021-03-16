package com.cbb.wmt_eoc.page.error_report

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 16:35
 * describe:
 */
interface ReportErrorConstract {
    interface Model
    interface View {
        fun selectImage()
        fun selectImageState(state: Int)
    }

    interface Presenter
}