package com.cbb.baselib.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cbb.baselib.StringConstant
import com.cbb.baselib.NetworkChangeEvent
import com.cbb.baselib.util.NetWorkUtil
import com.cbb.baselib.util.PreferenceUtil
import org.greenrobot.eventbus.EventBus

/**
 * 网络状态接受广播，监听网络状态
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    /**
     * 缓存上一次的网络状态
     */
    private var hasNetwork: Boolean by PreferenceUtil(StringConstant.HAS_NETWORK_KEY, true)

    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetWorkUtil.isNetworkConnected(context)
        if (isConnected) {
            EventBus.getDefault().post(NetworkChangeEvent(isConnected))
        } else {
            EventBus.getDefault().post(NetworkChangeEvent(isConnected))
        }
    }

}