package com.cbb.wmt_eoc.page.check

import android.annotation.SuppressLint
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbb.baselib.adapter.CommonAdapter
import com.cbb.baselib.adapter.ViewHolder
import com.cbb.baselib.base.BaseActivity
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.util.ByteArrayTohexHepler
import com.cbb.wmt_eoc.util.NfcUtils
import com.cbb.wmt_eoc.view.AlertPopupWindow
import com.cbb.wmt_eoc.view.CheckSelectPopupWindow
import com.cbb.wmt_eoc.view.ConfirmPopupWindow
import kotlinx.android.synthetic.main.activity_start_check.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 16:39
 * describe:
 */
class StartCheckActivity : BaseActivity<Any>() {
    override fun initPresenter(): StartCheckPresenter? = null

    override fun getLayoutId(): Int = R.layout.activity_start_check
    private var points = ArrayList<CurrentPointModel>()
    private var task: MyTask? = null

    //    private var
//    val handler = @SuppressLint("HandlerLeak")
//    object : Handler() {
//        override fun handleMessage(msg: Message?) {
//            super.handleMessage(msg)
//            val format = SimpleDateFormat("HH:mm").format(Date())
//            tv_time.text = format
//            sendEmptyMessageDelayed(1, 1000)
//        }
//    }
    private fun getTime(time: Long) {
        val s = time % 60
        val m = time / 60
        val h = m / 60
    }

    override fun initView() {
        super.initView()
        iv_check.setOnClickListener {
            if (tv_start.text.toString() == "开始") {
                requestStart()
            }
//            CheckSelectPopupWindow(
//                this@StartCheckActivity, pointCheckId,"0472069AAB6484",
//                task!!.workPlanId, task!!.planTimeId
//            ).show(iv_check)
        }


        title_view.setOnRightTextClickListener {
            if (task == null) {
                showToast("请先选择班次")
                return@setOnRightTextClickListener
            }
            ConfirmPopupWindow(this, "是否申请终止任务？", object : ConfirmPopupWindow.ConfirmCallback {
                override fun confirm() {
                    startActivity(
                        Intent(this@StartCheckActivity, ApplyStopTaskActivity::class.java)
                            .putExtra("id", "" + task!!.id)
                    )
                }
            }).show(title_view)
        }

        recycler_task.layoutManager = LinearLayoutManager(this)
        recycler_task.adapter =
            object : CommonAdapter<CurrentPointModel>(this, points, R.layout.item_task) {
                override fun convert(holder: ViewHolder, t: MutableList<CurrentPointModel>) {
                    // 1-已巡且正常 2-已巡且异常报警  3-漏巡
                    val iv_state = holder.getView<ImageView>(R.id.iv_state)
                    val tv_state = holder.getView<TextView>(R.id.tv_state)
                    val tv_name = holder.getView<TextView>(R.id.tv_name)
                    val tv_point_name = holder.getView<TextView>(R.id.tv_point_name)
                    val tv_remark = holder.getView<TextView>(R.id.tv_remark)
                    val rl_root = holder.getView<View>(R.id.rl_root)

                    tv_name.text = "${holder.realPosition + 1}"
//                iv_state
                    t[holder.realPosition].run {
                        tv_state.text = this.state
                        tv_point_name.text = this.pointName
                        tv_remark.text = this.remark
                        when (this.state) {
                            "未巡" -> {
                            }
                            "已巡" -> {
                                iv_state.setImageResource(R.mipmap.icon_checked)
                            }
                            "报警" -> {
                                iv_state.setImageResource(R.mipmap.iocn_checked_error)
                            }
                            "漏巡" -> {
                                iv_state.setImageResource(R.mipmap.icon_checked_left)
                            }
                        }

                    }
                }
            }
//        handler.sendEmptyMessage(1)

        rl_task.setOnClickListener {
            startActivity(Intent(this@StartCheckActivity, SelectTaskActivity::class.java))
        }
        requestTasks()
    }


    override fun onResume() {
        super.onResume()
        if (NfcUtils.mNfcAdapter == null) {
            NfcUtils(this)
        }
        if (NfcUtils.mNfcAdapter != null) {
            NfcUtils.mNfcAdapter.enableForegroundDispatch(
                this,
                NfcUtils.mPendingIntent,
                null,
                null
            )
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getTaskEvent(event: MyTask) {
        task = event
        if (task == null) {
            return
        }
        tv_select_task.text = "${event.planName} ${event.gmtStart}-${event.gmtEnd}"
        if (task!!.state == 1) {
            tv_start.text = "进行中"
        }
        requestTasks()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun taskChanged(event: TaskStateChanged) {
        requestTasks()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        readMsg(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
//        handler.removeCallbacksAndMessages(null)
        NfcUtils.mNfcAdapter = null
    }

    override fun onPause() {
        super.onPause()
        if (NfcUtils.mNfcAdapter != null) {
            NfcUtils.mNfcAdapter.disableForegroundDispatch(this)
        }
    }

    //  这块的processIntent() 就是处理卡中数据的方法
    fun readMsg(intent: Intent) {
        // 如果没有点开始  就不能刷卡
        if (tv_start.text.toString() == "开始") {
            AlertPopupWindow(this, "请先点击开始按钮！").show(tv_start)
            return
        }
        //获取到卡对象
        val tagFromIntent: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        //获取卡id这里即uid
        val aa = tagFromIntent.id
        val uid = ByteArrayTohexHepler.ByteArrayToHexString(aa)

        if (task != null) {
            CheckSelectPopupWindow(
                this@StartCheckActivity, pointCheckId,uid,
                task!!.workPlanId, task!!.planTimeId
            ).show(iv_check)
        } else {
            showToast("请先选择班次")
        }
    }


    @SuppressLint("CheckResult")
    fun requestTasks() {
        if (task == null) {
            return
        }
        val map = HashMap<String, String>()
        map["planId"] = task!!.workPlanId
        map["planTimeId"] = task!!.planTimeId
        PointsLoader().request(map).subscribe({
            points.clear()
            points.addAll(it)
            recycler_task.adapter!!.notifyDataSetChanged()
            if (it.isEmpty()) {
                showToast("这里没有数据")
            }
        }, {
            showToast(ThrowableUtils.getThrowableMessage(it))
        })
    }
    private var pointCheckId = 0
    // 开始
    @SuppressLint("CheckResult")
    fun requestStart() {
        if (task == null) {
            showToast("请先选择任务")
            return
        }
        val map = HashMap<String, String>()
        map["uptId"] = "" + task!!.id
        PlanStartLoader().request(map).subscribe({
            pointCheckId = it
            tv_start.text = "进行中"
        }, {
            showToast(ThrowableUtils.getThrowableMessage(it))
        })
    }
}