package com.cbb.baselib.util

import android.Manifest
import android.content.Context
import android.os.Build
import android.util.Log
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import java.util.*

class PermissionUtil {
    companion object {
        fun requestPermission(
            context: Context,
            ps: List<String>,
            callBack: RequestPermissionCallback
        ) {
            val array = Array(ps.size) {
                ps[it]
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AndPermission.with(context)
                    .runtime()
                    .permission(array)
                    // 用户给权限了
                    .onGranted(Action<List<String>> {
                        callBack.callback(true)
                    })
                    .onDenied(Action<List<String>> {
                        Log.e("-------","------111111")
                        callBack.callback(false)
                    }
                    ).start()
            } else {
                callBack.callback(true)
            }
        }

        fun requsetCameraPermission(context: Context, callBack: RequestPermissionCallback) {
            val p = listOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            PermissionUtil.requestPermission(
                context,
                p,
                callBack
            )
        }

        fun requsetStoragePermission(context: Context, callBack: RequestPermissionCallback) {
            val p = listOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            PermissionUtil.requestPermission(
                context,
                p,
                callBack
            )
        }
    }

    interface RequestPermissionCallback {
        fun callback(success: Boolean)
    }
}