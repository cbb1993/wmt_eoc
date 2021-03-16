package com.cbb.wmt_eoc.page.error_report

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.cbb.baselib.adapter.CommonAdapter
import com.cbb.baselib.adapter.ViewHolder
import com.cbb.baselib.base.BaseActivity
import com.cbb.baselib.camera.CameraUtil
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.baselib.util.PermissionUtil
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.bind.Point
import com.cbb.wmt_eoc.view.BindLocationPopupWindow
import com.cbb.wmt_eoc.view.ImagePagePopupWindow
import com.cbb.wmt_eoc.view.SelectCameraPopupWindow
import kotlinx.android.synthetic.main.activity_report_error.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File


/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 16:39
 * describe:
 */
class ReportErrorActivity : BaseActivity<ReportErrorPresenter>() {
    override fun initPresenter(): ReportErrorPresenter? = null

    override fun getLayoutId(): Int = R.layout.activity_report_error
    private val list = ArrayList<String>()
    private val maxSize = 6
    var cardUid: String? = null
    var logId: String? = null
    var pointId: String? = null

    @SuppressLint("SetTextI18n")
    override fun initView() {
        super.initView()

        cardUid = intent.getStringExtra("cardUid")
        logId = intent.getStringExtra("logId")

        if (logId == null) {
            rl_error_location.visibility = View.VISIBLE
            rl_error_location.setOnClickListener {
                BindLocationPopupWindow(this@ReportErrorActivity,
                    object : BindLocationPopupWindow.SelectCallBack {
                        override fun select(point: Point) {
                            tv_location_name.text = point.pointName
                            pointId = point.id
                        }
                    }).show(rl_error_location)
            }
        }

        et_content.addTextChangedListener {
            it?.run {
                tv_count.text = "${it.length}/120"
            }
        }
        list.add("")

        recycler_photo.layoutManager = GridLayoutManager(this, 3)
        recycler_photo.adapter =
            object : CommonAdapter<String>(this, list, R.layout.item_photo) {
                override fun convert(holder: ViewHolder, t: MutableList<String>) {
                    val iv_ = holder.getView<ImageView>(R.id.iv_)
                    var path = t[holder.realPosition]
                    if (path == "") {
                        iv_.setImageResource(R.mipmap.camera_grey)
                        iv_.setOnClickListener {
                            SelectCameraPopupWindow(this@ReportErrorActivity,
                                object : SelectCameraPopupWindow.SelectCallBack {
                                    override fun select(flag: Int) {
                                        openCamera(flag)
                                    }
                                }).show(recycler_photo)
                        }
                    } else {
                        Glide.with(this@ReportErrorActivity)
                            .load(path)
                            .into(iv_)
                        iv_.setOnClickListener {
                            val photos = ArrayList<String>()
                            list.forEach {
                                if (it != "") {
                                    photos.add(it)
                                }
                            }
                            ImagePagePopupWindow(
                                this@ReportErrorActivity,
                                photos,
                                holder.realPosition
                            ).show(recycler_photo)
                        }
                    }
                }
            }

        CameraUtil.setCallBack(object : CameraUtil.SelectCallBack {
            override fun success(path: String) {
                val temp = list.size - 1
                val cPath = list[temp]
                if (cPath == "") {
                    list.add(temp, path)
                }
                if (list.size > maxSize) {
                    list.removeAt(list.size - 1)
                }
                recycler_photo.adapter?.run {
                    this.notifyDataSetChanged()
                }
            }

            override fun fail() {
                showToast("选取照片失败")
            }
        })

        btn_submit.setOnClickListener {
            if (et_content.length() == 0) {
                showToast("请填写故障内容")
                return@setOnClickListener
            }
            report(et_content.text.toString())
        }
    }

    @SuppressLint("CheckResult")
    fun report(content: String) {
        showLoading()
        val map = HashMap<String, RequestBody>()
        list.forEach {

            if (it != "") {
                val file = File(it)
                // application/otcet-stream
                val requestFile =
                    RequestBody.create(MediaType.parse("image/*"), file)
                map["files\"; filename=\"" + file.name] = requestFile
            }
        }
        map["content"] = toRequestBody(content)
        if (logId == null) {
            map["patrolLogId"] = toRequestBody("0")
            if (pointId != null) {
                map["pointId"] = toRequestBody(pointId!!)
            }
        } else {
            map["cardUid"] = toRequestBody(cardUid!!)
            map["patrolLogId"] = toRequestBody(logId!!)
        }
        ReportErrorLoader().request(map).subscribe({
            dismissLoading()
            showToast("提交成功")
            finish()
        }, {
            dismissLoading()
            showToast(ThrowableUtils.getThrowableMessage(it))
        })
    }

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        CameraUtil.onActivityResult(this@ReportErrorActivity, requestCode, resultCode, data)
    }

    private fun openCamera(flag: Int) {
        PermissionUtil.requsetCameraPermission(this,
            object : PermissionUtil.RequestPermissionCallback {
                override fun callback(success: Boolean) {
                    if (flag == 0) {
                        CameraUtil.openAlbum(this@ReportErrorActivity)
                    } else if (flag == 1) {
                        CameraUtil.openCamera(this@ReportErrorActivity)
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        CameraUtil.destroyCallBack()
    }
}