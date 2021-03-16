package com.cbb.wmt_eoc.page.check

import java.io.Serializable

/**
 * Created by 坎坎.
 * Date: 2020/1/6
 * Time: 11:17
 * describe:
 */
data class MyTaskModel(
    val times: ArrayList<MyTask>,
    val planName: String
)
data class MyTask(
    val isUntying:Int?,
    val content: String,
    val gmtEnd: String,
    val gmtStart: String,
    val id: Int,
    val isHoliday: Int,
    val patrolNum: Int,
    val planName: String,
    val planTimeId: String,
    val userId: Int,
    val workPlanId: String,
    val state:Int  // 0 没开始  1进行中
):Serializable{
    override fun toString(): String {
        return "MyTask(content='$content', gmtEnd='$gmtEnd', gmtStart='$gmtStart', id=$id, isHoliday=$isHoliday, patrolNum=$patrolNum, planName='$planName', planTimeId='$planTimeId', userId=$userId, workPlanId='$workPlanId')"
    }
}