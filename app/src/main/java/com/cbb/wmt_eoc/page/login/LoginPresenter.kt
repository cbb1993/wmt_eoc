package com.cbb.wmt_eoc.page.login

import android.annotation.SuppressLint
import com.cbb.baselib.net.ThrowableUtils

/**
 * Created by 坎坎.
 * Date: 2019/12/24
 * Time: 22:51
 * describe:
 */
class LoginPresenter(var view: LoginContract.View): LoginContract.Presenter{


    @SuppressLint("CheckResult")
    override fun login(account: String?, code: String?) {
        if (account == null||account == "") {
            view.loginFail("请输入账号")
            return
        }
        if (code == null||code == "") {
            view.loginFail("请输入密码")
            return
        }
        val map = HashMap<String, String>()
//        map["mobile"] = account
//        map["code"] = code
        map["name"] = account
        map["password"] = code
        LoginLoader().request(map).subscribe({
            view.loginSuccess(it)
        }, {
            view.loginFail(ThrowableUtils.getThrowableMessage(it))
        })
    }

    @SuppressLint("CheckResult")
    override fun getCode(account: String) {
        if (account.length!=11) {
            view.loginFail("请输入正确手机号")
            return
        }
        val map = HashMap<String, String>()
        map["mobile"] = account
        map["type"] = "1"
        LoginCodeLoader().request(map).subscribe({
            view.getCodeSuccess()
        }, {
            view.getCodeFail(ThrowableUtils.getThrowableMessage(it))
        })
    }

}