package com.cbb.wmt_eoc.page.login

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 9:30
 * describe:
 */
interface LoginContract {

    interface View {
        fun loginSuccess(result: LoginModel)
        fun loginFail(e: String)
        fun getCodeSuccess()
        fun getCodeFail(e: String)
    }

    interface Presenter {
        fun login(account: String?, code: String?)
        fun getCode(account: String)
    }
}