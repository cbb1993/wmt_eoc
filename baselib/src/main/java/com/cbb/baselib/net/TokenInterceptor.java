package com.cbb.baselib.net;

import com.cbb.baselib.event.NotifyReLogin;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by 坎坎.
 * Date: 2020/6/4
 * Time: 17:24
 * describe:
 */
public class TokenInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        int code = response.code();
        if (code == 401) {
            EventBus.getDefault().post(new NotifyReLogin());
        }
        return response;
    }
}
