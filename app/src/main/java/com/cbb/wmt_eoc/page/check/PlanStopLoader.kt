package com.cbb.wmt_eoc.page.check

import com.cbb.baselib.StringConstant
import com.cbb.baselib.net.BaseResponse
import com.cbb.baselib.net.ObjectLoader
import com.cbb.baselib.net.PayLoad
import com.cbb.baselib.net.RetrofitServiceManager
import com.cbb.baselib.util.SharedPreferencesUtils
import com.cbb.wmt_eoc.Constant
import io.reactivex.Observable
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * Created by 坎坎.
 * Date: 2019/12/31
 * Time: 13:43
 * describe:
 */
class PlanStopLoader : ObjectLoader() {
    private var mScanService: Service

    init {
        mScanService = RetrofitServiceManager.getInstance().create<Service>(Service::class.java)
    }

    fun request(map: Map<String, String>): Observable<Any?> {
        val token =  SharedPreferencesUtils.readData(StringConstant.TOKEN, "")
        return observe<BaseResponse<Any?>>(
            mScanService.request(token, Constant.apply_add, map)).map(PayLoad<Any?>())
    }

    interface Service {
        @POST
        fun request(@Header("Authorization") token: String, @Url url: String,
                    @QueryMap bean: Map<String, String>): Observable<BaseResponse<Any?>>
    }
}