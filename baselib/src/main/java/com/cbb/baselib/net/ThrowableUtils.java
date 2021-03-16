package com.cbb.baselib.net;

import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * author dhy
 * Created by test on 2018/7/2.
 */

public class ThrowableUtils {
    public static String getThrowableMessage( Throwable throwable) {
        //出现错误
       if (throwable instanceof SocketTimeoutException) {
            return "网络请求超时，请稍后重试";
        } else if (throwable instanceof ConnectException) {
            return "无网络，请求失败";
        } else if (throwable instanceof Fault) {
            return ((Fault) throwable).message;
        } else if (throwable instanceof NullPointerException) {
            return "请求数据为空";
        } else if (throwable instanceof UnknownHostException) {
            return "无网络连接，请求失败";
        } else {
            return throwable.getMessage();
        }
    }
}
