package com.cbb.wmt_eoc.page.schedule

/**
 * Created by 坎坎.
 * Date: 2019/12/31
 * Time: 16:46
 * describe:
 */data class ScheduleModel(
    val records:List<ScheduleRecord>
)
data class ScheduleRecord2( val workPlanId: String,
                           val isHoliday: Int,
                           val planName: String,
var check:Boolean?)

data class ScheduleRecord( val gmtCreate: String,
                   val gmtModified: String,
                   val id: String,
                   val mchId: Int,
                   val planName: String,
                   val storeId: Int,
                   var check:Boolean)