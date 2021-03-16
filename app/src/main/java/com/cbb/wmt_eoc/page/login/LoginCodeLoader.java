package com.cbb.wmt_eoc.page.login;



import com.cbb.baselib.net.BaseResponse;
import com.cbb.baselib.net.ObjectLoader;
import com.cbb.baselib.net.PayLoad;
import com.cbb.baselib.net.RetrofitServiceManager;
import com.cbb.wmt_eoc.Constant;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


/**
 * Created by dhy
 * Date: 2019/5/9
 * Time: 11:18
 * describe:
 */
public class LoginCodeLoader extends ObjectLoader {
    private LoginService mScanService;

    public LoginCodeLoader() {
        mScanService = RetrofitServiceManager.getInstance().create(LoginService.class);
    }

    public Observable<Object> request(Map<String,String> map) {
        return observe(mScanService.request(Constant.SIGN_CODE, map)).map(new PayLoad<Object>());
    }

    public interface LoginService {
        @GET
        Observable<BaseResponse<Object>> request(@Url String url, @QueryMap Map<String, String> bean);
    }
}
