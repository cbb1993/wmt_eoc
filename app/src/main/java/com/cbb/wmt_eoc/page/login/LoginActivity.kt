package com.cbb.wmt_eoc.page.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.view.View
import com.cbb.baselib.StringConstant
import com.cbb.baselib.base.BaseActivity
import com.cbb.baselib.util.PermissionUtil
import com.cbb.baselib.util.SharedPreferencesUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.main.MainActivity
import com.cbb.wmt_eoc.view.BindHostPopupWindow
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.system.exitProcess

/**
 * Created by 坎坎.
 * Date: 2019/12/24
 * Time: 23:03
 * describe:
 */
class LoginActivity : BaseActivity<LoginPresenter>(), LoginContract.View {
    override fun initPresenter(): LoginPresenter = LoginPresenter(this)
//18501753371
    override fun getLayoutId(): Int = R.layout.activity_login
    private var time = 60
    var handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (time in 1..60) {
                time--
                tv_countdown.text = "${time}秒"
                sendEmptyMessageDelayed(1, 1000)
            } else {
                time = 60
                tv_countdown.text = "获取验证码"
            }
        }
    }


    override fun initView() {
        super.initView()
        val p = listOf(
            Manifest.permission.NFC
        )
        PermissionUtil.requestPermission(
            this,
            p,
            object : PermissionUtil.RequestPermissionCallback {
                override fun callback(success: Boolean) {
                    if (!success) {
                        showToast("请同意申请NFC权限")
                    }
                }
            })
       var token =  SharedPreferencesUtils.readData(StringConstant.TOKEN)
        if ("" != token) {
            start()
        }

        tv_host.setOnClickListener {
            BindHostPopupWindow(this).show(tv_host)
        }
    }

    override fun loginSuccess(result: LoginModel) {
        dismissLoading()
        SharedPreferencesUtils.addData(StringConstant.TOKEN,"Bearer ${result.token}")
        SharedPreferencesUtils.addObjData(StringConstant.USER,result.user)
        start()
    }

    fun start() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun loginFail(e: String) {
        dismissLoading()
        showToast(e)
    }


    override fun getCodeSuccess() {
        showToast("验证码已发送")
    }

    override fun getCodeFail(e: String) {
        showToast(e)
    }

    fun login(view: View) {
        showLoading()
        presenter!!.login(et_account.text.toString(),et_code.text.toString())
    }

    fun countdown(view: View) {
        if (et_account.text == null) {
            return
        }
        if (time == 60) {
            handler.sendEmptyMessage(1)
            presenter!!.getCode(et_account.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}