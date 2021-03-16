package com.cbb.wmt_eoc.page.mine

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cbb.baselib.StringConstant
import com.cbb.baselib.base.BaseFragment
import com.cbb.baselib.net.ThrowableUtils
import com.cbb.baselib.util.PermissionUtil
import com.cbb.baselib.util.SharedPreferencesUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.error_record.ErrorRecordActivity
import com.cbb.wmt_eoc.page.login.LoginActivity
import com.cbb.wmt_eoc.page.login.LoginModel
import com.cbb.wmt_eoc.page.main.MainActivity
import com.cbb.wmt_eoc.page.personal.PersonalActivity
import com.cbb.wmt_eoc.page.schedule.ScheduleActivity
import com.cbb.wmt_eoc.util.FileDownLoadUtil
import com.cbb.wmt_eoc.view.ConfirmPopupWindow
import com.cbb.wmt_eoc.view.ProgressAlertPopupWindow
import kotlinx.android.synthetic.main.frag_mine.*
import java.io.File


/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 14:36
 * describe:
 */
class MineFrag : BaseFragment() {
    override fun attachLayoutRes(): Int = R.layout.frag_mine
    lateinit var token:String
    lateinit var main :MainActivity
    lateinit var user: LoginModel.User
    override fun initView() {
        token = SharedPreferencesUtils.readData(StringConstant.TOKEN, "")
        user =SharedPreferencesUtils.readObjData(StringConstant.USER, LoginModel.User::class.java)
        main = activity as MainActivity
        user?.run {
            tv_name.text = this.name
            tv_account.text = this.mobile
        }

        rl_person.setOnClickListener {
            startActivity(Intent(activity, PersonalActivity::class.java))
        }

        tv_error_record.setOnClickListener {
            startActivity(Intent(activity, ErrorRecordActivity::class.java))
        }

        tv_select_task.setOnClickListener {
            startActivity(Intent(activity, ScheduleActivity::class.java))
        }

        rl_update.setOnClickListener {
            getApkVersion()
        }

        btn_login_out.setOnClickListener {
            SharedPreferencesUtils.addData(StringConstant.TOKEN,"")
            SharedPreferencesUtils.addObjData(StringConstant.USER,"")
            startActivity(Intent(activity, LoginActivity::class.java))
            activity!!.finish()
        }

        tv_version_name.text = "版本号：${getVersionName()}"
    }

    override fun onResume() {
        super.onResume()
        user =SharedPreferencesUtils.readObjData(StringConstant.USER, LoginModel.User::class.java)
        Glide.with(activity!!)
            .load(user.profilePic)
            .placeholder(R.mipmap.head_pic)
            .error(R.mipmap.head_pic)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(head_pic)
    }

    override fun lazyLoad() {
    }

    fun getVersionName(): String? {
        //获取包管理器
        val pm = activity!!.packageManager
        //获取包信息
        try {
            val packageInfo = pm.getPackageInfo(activity!!.packageName, 0)
            //返回版本号
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "error"

    }

    @SuppressLint("CheckResult")
    fun  getApkVersion(){
        val map = HashMap<String, String>()
        ApkVersionLoader().request(map).subscribe({
            val versionName = getVersionName()
            var local = versionName!!.replace(".","")
            var cloud = it.visionName.replace(".","")
            val i_local = java.lang.Integer.parseInt(local)
            val i_cloud = java.lang.Integer.parseInt(cloud)
            if(i_local >= i_cloud){
                main.showToast("您现在已经是最新版本")
            }else {
                ConfirmPopupWindow(main,"发现新版本，是否更新?",object :ConfirmPopupWindow.ConfirmCallback{
                    override fun confirm() {
                        PermissionUtil.requsetStoragePermission(main,object :PermissionUtil.RequestPermissionCallback{
                            override fun callback(success: Boolean) {
                                if(success){
                                    downLoadApk(it.visionName,it.url)
                                }else{
                                    main.showToast("请同意权限申请")
                                }
                            }
                        })
                    }
                }).show(rl_update)
            }
        }, {

            main.showToast(ThrowableUtils.getThrowableMessage(it))
        })
    }

    private fun downLoadApk(vName:String,url: String?) {
        if(url == null ){
            return
        }

        val root = File(Environment.getExternalStorageDirectory(), "wmt_eoc_cache")
        val update = File(root,"update_${vName}.apk")
        val temp = File(root,"update_${vName}.apk.temp")
        if(update.exists()){
            update.delete()
        }
        Log.e("------", "------"+temp.exists())
        if(temp.exists()){
            temp.delete()
        }
        Log.e("------", "------"+temp.exists())
        val progressAlertPopupWindow = ProgressAlertPopupWindow(main,object :ProgressAlertPopupWindow.OnCancelClickListener{
            override fun click() {
                FileDownLoadUtil.cancel()
            }
        })
        progressAlertPopupWindow.show(tv_version_name)
        FileDownLoadUtil.downLoad(url,update.absolutePath,object :FileDownLoadUtil.DownLoadCallback{
            override fun downLoadComplete() {
                progressAlertPopupWindow.dismiss()
                main.showToast("下载完成")
                if(update.exists()){
                    FileDownLoadUtil.installApk(update,main)
                }
            }
            override fun downLoadIng(p: Int) {
                Log.e("------downLoadIng", "------$p")
                progressAlertPopupWindow.updateProgress(p)
            }

            override fun error(error: String?) {
                progressAlertPopupWindow.dismiss()
                error?.run {
                    main.showToast(this)
                }
            }
            override fun downLoadStart() {
                main.showToast("已经开始下载")
            }
        })

    }

}