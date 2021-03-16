package com.cbb.baselib.base

import android.app.Activity
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cbb.baselib.NetworkChangeEvent
import com.cbb.baselib.R
import com.cbb.baselib.receiver.NetworkChangeReceiver
import com.cbb.baselib.util.ScreenUtil
import com.cbb.baselib.view.LoadingDialog
import com.cbb.baselib.view.TipView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by 坎坎.
 * Date: 2019/12/24
 * Time: 19:26
 * describe:
 */
abstract class BaseActivity<T> : AppCompatActivity() {
    // 网络状态广播
    private lateinit var mNetworkChangeReceiver: NetworkChangeReceiver
    // 网络提示View
    private lateinit var tipView: TipView
    // presenter
    var presenter: T? = null

    lateinit var loading :LoadingDialog

    fun showLoading(){loading.show()}
    fun dismissLoading(){loading.dismiss()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivity(this)
        // 设置状态栏颜色
//        ScreenUtil.initStatusBar(this, getStatusBarColor())
        // 注册EventBus
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }

        // 网络提示View
        tipView = TipView(this)

        //设置contentView
        setContentView(getLayoutId())

        // 初始化presenter
        presenter = initPresenter()

        loading = LoadingDialog(this)

        initView()
        initListener()
        initData()

    }

    /**
     * Network Change
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChangeEvent(event: NetworkChangeEvent) {
        tipView.checkNetwork(event.isConnected)
    }

    // 状态栏默认颜色
    open fun getStatusBarColor(): Int = R.color.lib_transparent

    // 是否使用 EventBus
    open fun useEventBus(): Boolean = true

    // 是否使用 EventBus
    abstract fun initPresenter(): T?
    // 引入布局
    abstract fun getLayoutId(): Int

    override fun onDestroy() {
        super.onDestroy()
        removeActivity(this)
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    open fun initView() {
    }

    open fun initListener() {
    }

    open fun initData() {
    }

    override fun onResume() {
        // 动态注册网络变化广播
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        mNetworkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(mNetworkChangeReceiver, filter)
        super.onResume()
    }

    override fun onPause() {
        unregisterReceiver(mNetworkChangeReceiver)
        super.onPause()
    }

    fun showToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

  companion object {
      private val activities :ArrayList<Activity> = ArrayList<Activity>()
      var exit: Boolean = false
  }


    fun addActivity(activity:Activity){
        var exsit = false
        activities.forEach {
            if(it == activity){
                exsit = true
            }
        }
       if(!exsit){
           activities.add(activity)
       }
    }

    fun removeActivity(activity:Activity){
        if(!exit){
            var exsit = false
            activities.forEach {
                if(it == activity){
                    exsit = true
                }
            }
            if(exsit){
                activities.remove(activity)
            }
        }

    }
    fun getActivitySize():Int{
        return activities.size
    }
    fun exitPro(){
        exit =true
        activities.forEach {
            if(!it.isDestroyed){
                it.finish()
            }
        }
        activities.clear()
    }
}