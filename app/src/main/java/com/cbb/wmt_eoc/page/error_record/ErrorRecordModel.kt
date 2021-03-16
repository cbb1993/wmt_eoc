package com.cbb.wmt_eoc.page.error_record

/**
 * Created by 坎坎.
 * Date: 2019/12/30
 * Time: 17:55
 * describe:
 */
data class ErrorRecordModel(
    val buildingId: Int?,
    val buildingName: String?,
    val content: String?,
    val floorId: Int?,
    val floorName: String?,
    val gmtCreate: String?,
    val gmtModified: String?,
    val id: String?,
    val mchId: Int?,
    val patorlId: Int?,
    val pics: List<Pic>,
    val pointId: Int?,
    val pointName: String?,
    val state: String?,
    val storeId: Int?,
    val userId: Int?,
    val userName: String?,
    val gmtStart: String?,
    val gmtEnd: String?,
    val wokePlanName: String?,
    val pointRemark: String?
)

data class Pic(val redirect:String?)