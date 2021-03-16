package com.cbb.baselib.base

import android.app.Application
import okhttp3.OkHttpClient
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * Created by 坎坎.
 * Date: 2019/12/24
 * Time: 19:42
 * describe:
 */
open class BaseApplication : Application() {
     companion object {
         lateinit var instance: BaseApplication
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}