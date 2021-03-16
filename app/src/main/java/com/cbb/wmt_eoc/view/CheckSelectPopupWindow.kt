package com.cbb.wmt_eoc.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.check.PointStateLoader
import com.cbb.wmt_eoc.page.check.TaskStateChanged
import com.cbb.wmt_eoc.page.error_report.ReportErrorActivity
import org.greenrobot.eventbus.EventBus

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 21:19
 * describe:
 */
class CheckSelectPopupWindow(context: Activity,uplId:Int,cardUid:String,planId:String,planTimeId:String) :PopupWindow(null, ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT){
    val context = context
    val cardUid = cardUid
    val planId = planId
    val planTimeId = planTimeId
    val uplId = uplId

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_check_select,null)
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        contentView.findViewById<View>(R.id.tv_normal).setOnClickListener {
            request("1")
        }
        contentView.findViewById<View>(R.id.tv_abnormal).setOnClickListener {
            request("2")

        }
        contentView.setOnClickListener {
            dismiss()
        }


    }
    fun show(parent: View){
        showAtLocation(parent,Gravity.CENTER,0,0)
    }

    @SuppressLint("CheckResult")
    fun request(state:String) {
          val map = HashMap<String, String>()
          map["cardUid"] = cardUid
          map["state"] = state
          map["planId"] = planId
          map["uplId"] = ""+uplId
          map["planTimeId"] = planTimeId
          PointStateLoader().request(map).subscribe({
              if(state == "1"){
                  Toast.makeText(context, "打卡成功",Toast.LENGTH_SHORT).show()
              }else{
                  context.startActivity(Intent(context,ReportErrorActivity::class.java)
                      .putExtra("logId",it.toString())
                      .putExtra("cardUid",cardUid))
              }
              EventBus.getDefault().post(TaskStateChanged())
              dismiss()
          }, {
              Toast.makeText(context, ThrowableUtils.getThrowableMessage(it),Toast.LENGTH_SHORT).show()
          })
      }
}