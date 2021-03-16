package com.cbb.wmt_eoc.page.bind

import android.annotation.SuppressLint
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.util.Log
import com.cbb.baselib.base.BaseActivity
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.util.ByteArrayTohexHepler.ByteArrayToHexString
import com.cbb.wmt_eoc.util.NfcUtils
import com.cbb.wmt_eoc.view.BindLocationPopupWindow
import kotlinx.android.synthetic.main.activity_bind_location.*


/**
 * Created by 坎坎.
 * Date: 2019/12/27
 * Time: 13:21
 * describe:
 */
class BindLocationActivity : BaseActivity<Any>() {
    override fun initPresenter(): Any? = null

    override fun getLayoutId(): Int = R.layout.activity_bind_location

    private var cardId = ""

    override fun initView() {
        super.initView()
        btn_bind.setOnClickListener {
            if (cardId == "") {
                showToast("绑定前请将手机靠近NFC卡读取信息")
                return@setOnClickListener
            }
            BindLocationPopupWindow(this@BindLocationActivity,
                object : BindLocationPopupWindow.SelectCallBack {
                    override fun select(point: Point) {
                        request(point.id)
                    }
                }).show(btn_bind)
        }
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        readMsg(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
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
        try {
            //获取到卡对象
            val tagFromIntent : Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            //获取卡id这里即uid
            val aa = tagFromIntent.id
            val uid = ByteArrayToHexString(aa)
            cardId = uid
            tv_nfc_id.text = uid
        }catch (e:Exception){

        }

    }

    @SuppressLint("CheckResult")
    fun request(pointId:String) {
        val map = HashMap<String, String>()
        map["pointId"] = pointId
        map["cardUid"] = cardId
        map["cardNo"] = "卡1"
        BindLocationLoader().request(map).subscribe({
            showToast("绑定成功")
            finish()
        }, {
            showToast(ThrowableUtils.getThrowableMessage(it))
        })
    }
}

