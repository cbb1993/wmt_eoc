package com.cbb.wmt_eoc.page.error_report

import com.cbb.baselib.StringConstant
import com.cbb.baselib.net.BaseResponse
import com.cbb.baselib.net.ObjectLoader
import com.cbb.baselib.net.PayLoad
import com.cbb.baselib.net.RetrofitServiceManager
import com.cbb.baselib.util.SharedPreferencesUtils
import com.cbb.wmt_eoc.Constant
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by 坎坎.
 * Date: 2019/12/31
 * Time: 13:43
 * describe:
 */
class ReportErrorLoader : ObjectLoader() {
    private var mScanService: Service

    init {
        mScanService = RetrofitServiceManager.getInstance().create<Service>(Service::class.java)
    }

    fun request(@PartMap files: Map<String, RequestBody>): Observable<Any> {
        val token =  SharedPreferencesUtils.readData(StringConstant.TOKEN, "")

        return observe<BaseResponse<Any>>(
            mScanService.request(token, Constant.fault_add, files)
        ).map(PayLoad<Any>())
    }

    interface Service {
        @Multipart
        @POST
        fun request(
            @Header("Authorization") token: String, @Url url: String,
            @PartMap files: Map<String, @JvmSuppressWildcards RequestBody>
        ): Observable<BaseResponse<Any>>
    }
}