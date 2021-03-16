package com.cbb.wmt_eoc.page.bind

/**
 * Created by 坎坎.
 * Date: 2020/1/2
 * Time: 17:09
 * describe:
 */
data class PointModel(
    val records: List<Point>
)
data class Point(
    val id: String,
    val pointName: String

)