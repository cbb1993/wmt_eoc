package com.cbb.wmt_eoc.page.error_record

import com.cbb.baselib.StringConstant
import com.cbb.baselib.net.BaseResponse
import com.cbb.baselib.net.ObjectLoader
import com.cbb.baselib.net.PayLoad
import com.cbb.baselib.net.RetrofitServiceManager
import com.cbb.baselib.util.SharedPreferencesUtils
import com.cbb.wmt_eoc.Constant
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * Created by 坎坎.
 * Date: 2019/12/31
 * Time: 13:43
 * describe:
 */
class ErrorRecordLoader : ObjectLoader() {
    private var mScanService: Service

    init {
        mScanService = RetrofitServiceManager.getInstance().create<Service>(Service::class.java)
    }

    fun request(map: Map<String, String>): Observable<List<ErrorRecordModel>> {
        val token =  SharedPreferencesUtils.readData(StringConstant.TOKEN, "")
        return observe<BaseResponse<List<ErrorRecordModel>>>(
            mScanService.request(token, Constant.FAULT_LIST, map)).map(PayLoad<List<ErrorRecordModel>>())
    }

    interface Service {
        @GET
        fun request(@Header("Authorization") token: String, @Url url: String, @QueryMap bean: Map<String, String>): Observable<BaseResponse<List<ErrorRecordModel>>>
    }
}