package com.cbb.wmt_eoc.page.personal

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cbb.baselib.StringConstant
import com.cbb.baselib.base.BaseActivity
import com.cbb.baselib.camera.CameraUtil
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.baselib.util.PermissionUtil
import com.cbb.baselib.util.SharedPreferencesUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.login.LoginModel
import com.cbb.wmt_eoc.view.SelectCameraPopupWindow
import kotlinx.android.synthetic.main.activity_personal.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


/**
 * Created by 坎坎.
 * Date: 2019/12/27
 * Time: 14:10
 * describe:
 */
class PersonalActivity : BaseActivity<Any>() {
    private lateinit var user:LoginModel.User

    override fun initPresenter(): Any? = null
    override fun getLayoutId(): Int = R.layout.activity_personal
    override fun initView() {
        super.initView()
        user = SharedPreferencesUtils.readObjData(StringConstant.USER,LoginModel.User::class.java)

        user?.run {
            tv_name.text = this.name
            tv_account.text = this.account

        }
        Glide.with(this@PersonalActivity)
            .load(user.profilePic)
            .placeholder(R.mipmap.head_pic)
            .error(R.mipmap.head_pic)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(iv_photo)

        CameraUtil.setCallBack(object : CameraUtil.SelectCallBack {
            override fun success(path: String) {
                upload(File(path))
            }

            override fun fail() {
                showToast("选取照片失败")
            }
        })

        rl_photo.setOnClickListener {
            SelectCameraPopupWindow(this, object : SelectCameraPopupWindow.SelectCallBack {
                override fun select(flag: Int) {
                    openCamera(flag)
                }
            }).show(rl_photo)
        }
    }


    private fun openCamera(flag: Int) {
        PermissionUtil.requsetCameraPermission(this,
            object : PermissionUtil.RequestPermissionCallback {
                override fun callback(success: Boolean) {
                    if (flag == 0) {
                        CameraUtil.openAlbum(this@PersonalActivity)
                    } else if (flag == 1) {
                        CameraUtil.openCamera(this@PersonalActivity)
                    }
                }
            })
    }

    @SuppressLint("CheckResult")
    fun upload(file: File) {
        showLoading()
        val filePart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            RequestBody.create(MediaType.parse("image/*"), file)
        )
        UpdataPhotoLoader().request(filePart).subscribe({
            user.profilePic = it.fileUrl
            SharedPreferencesUtils.addObjData(StringConstant.USER,user)
            Glide.with(this@PersonalActivity)
                .load(it.fileUrl)
                .placeholder(R.mipmap.head_pic)
                .error(R.mipmap.head_pic)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(iv_photo)
            dismissLoading()
            showToast("上传成功")
        }, {
            dismissLoading()
            Log.e("--------","------"+it.message)
            showToast(ThrowableUtils.getThrowableMessage(it))
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        CameraUtil.onActivityResult(this@PersonalActivity, requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        CameraUtil.destroyCallBack()
    }
}