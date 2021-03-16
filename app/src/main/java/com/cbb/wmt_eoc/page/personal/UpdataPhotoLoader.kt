package com.cbb.wmt_eoc.page.personal

import com.cbb.baselib.StringConstant
import com.cbb.baselib.net.BaseResponse
import com.cbb.baselib.net.ObjectLoader
import com.cbb.baselib.net.PayLoad
import com.cbb.baselib.net.RetrofitServiceManager
import com.cbb.baselib.util.SharedPreferencesUtils
import com.cbb.wmt_eoc.Constant
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Created by 坎坎.
 * Date: 2019/12/31
 * Time: 13:43
 * describe:
 */
class UpdataPhotoLoader : ObjectLoader() {
    private var mScanService: Service

    init {
        mScanService = RetrofitServiceManager.getInstance().create<Service>(Service::class.java)
    }

    fun request(@PartMap file: MultipartBody.Part): Observable<HeadPic> {
        val token =SharedPreferencesUtils.readData(StringConstant.TOKEN,"")
        return observe<BaseResponse<HeadPic>>(
            mScanService.request(token, Constant.file_upload, file)
        ).map(PayLoad<HeadPic>())
    }

    interface Service {
        @Multipart
        @POST
        fun request(
            @Header("Authorization") token: String, @Url url: String,
            @Part file: MultipartBody.Part
        ): Observable<BaseResponse<HeadPic>>
    }
}