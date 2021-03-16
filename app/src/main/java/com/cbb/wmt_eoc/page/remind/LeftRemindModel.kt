package com.cbb.wmt_eoc.page.remind

/**
 * Created by 坎坎.
 * Date: 2020/1/7
 * Time: 21:46
 * describe:
 */
data class LeftRemindModel(
    val records:List<LeftRemind>
)
data class LeftRemind(
    val gmtCreate: String,
    val planName: String,
    val pointName: String,
    val state: String
)