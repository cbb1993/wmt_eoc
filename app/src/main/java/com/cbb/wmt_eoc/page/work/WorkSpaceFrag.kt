package com.cbb.wmt_eoc.page.work

import android.content.Intent
import android.widget.Toast
import com.cbb.baselib.StringConstant
import com.cbb.baselib.base.BaseFragment
import com.cbb.baselib.util.SharedPreferencesUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.bind.BindLocationActivity
import com.cbb.wmt_eoc.page.check.StartCheckActivity
import com.cbb.wmt_eoc.page.error_report.ReportErrorActivity
import com.cbb.wmt_eoc.page.login.LoginModel
import com.cbb.wmt_eoc.page.remind.LeftRemindActivity
import kotlinx.android.synthetic.main.frag_work_space.*

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 14:36
 * describe:
 */
class WorkSpaceFrag : BaseFragment() {
    override fun attachLayoutRes(): Int = R.layout.frag_work_space
    private lateinit var user: LoginModel.User
    override fun initView() {
        user = SharedPreferencesUtils.readObjData(StringConstant.USER, LoginModel.User::class.java)

        user?.run {
            tv_account.text = "账号：${this.name}"
        }

        tv_start_check.setOnClickListener {
            startActivity(Intent(activity, StartCheckActivity::class.java))
        }
        tv_error_report.setOnClickListener {
            startActivity(Intent(activity, ReportErrorActivity::class.java))
        }
        tv_left_remind.setOnClickListener {
            startActivity(Intent(activity, LeftRemindActivity::class.java))
        }

        tv_bind_location.setOnClickListener {
            if (user.roleId == 2||user.roleId == 1) {
                startActivity(Intent(activity, BindLocationActivity::class.java))
            } else {
                Toast.makeText(activity, "您没有权限进行操作", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
//        if(!hidden){
//            ScreenUtil.initStatusBar(activity!!, R.color.main)
//        }
    }

    override fun lazyLoad() {
    }
}