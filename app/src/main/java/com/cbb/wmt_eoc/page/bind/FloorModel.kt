package com.cbb.wmt_eoc.page.bind

/**
 * Created by 坎坎.
 * Date: 2020/1/2
 * Time: 16:32
 * describe:
 */
data class FloorModel(
    val buildingName: String,
    val floors: List<Floor>
)
data class Floor(
    val buildingId: Int,
    val buildingName: String,
    val floorName: String,
    val gmtCreate: Any,
    val gmtModified: Any,
    val id: String,
    val mchId: Int,
    val storeId: Int
)
