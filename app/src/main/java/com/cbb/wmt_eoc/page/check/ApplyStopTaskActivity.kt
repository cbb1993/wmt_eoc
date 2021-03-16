package com.cbb.wmt_eoc.page.check

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.cbb.baselib.base.BaseActivity
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.main.MainActivity
import kotlinx.android.synthetic.main.activity_stop_task.*
import java.util.HashMap

/**
 * Created by 坎坎.
 * Date: 2020/3/10
 * Time: 21:30
 * describe:
 */
class ApplyStopTaskActivity : BaseActivity<Any>() {
    override fun initPresenter(): Any? = null

    override fun getLayoutId(): Int = R.layout.activity_stop_task

    var id: String? = null

    override fun initView() {
        super.initView()

        id = intent.getStringExtra("id")
        if(id==null){
            showToast("数据发生错误")
            finish()
            return
        }

        et_content.addTextChangedListener {
            it?.run {
                tv_count.text = "${it.length}/500"
            }
        }

        btn_submit.setOnClickListener {
            if(et_content.length()==0){
                showToast("请填写申请理由")
                return@setOnClickListener
            }
            request()
        }
    }

    @SuppressLint("CheckResult")
    fun request() {
        val map = HashMap<String, String>()
        map["uptId"] = "" + id
        map["content"] = et_content.text.toString()
        PlanStopLoader().request(map).subscribe({
            showToast("任务终止成功")
            startActivity(Intent(this@ApplyStopTaskActivity,MainActivity::class.java))
        }, {
            showToast(ThrowableUtils.getThrowableMessage(it))
        })
    }
}