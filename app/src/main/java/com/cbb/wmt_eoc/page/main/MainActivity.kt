package com.cbb.wmt_eoc.page.main

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import com.cbb.baselib.StringConstant
import com.cbb.baselib.base.BaseActivity
import com.cbb.baselib.event.NotifyReLogin
import com.cbb.baselib.util.SharedPreferencesUtils
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.login.LoginActivity
import com.cbb.wmt_eoc.page.login.RefreshTokenLoader
import com.cbb.wmt_eoc.page.mine.MineFrag
import com.cbb.wmt_eoc.page.work.WorkSpaceFrag
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 11:09
 * describe:
 */
class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {
    override fun initPresenter(): MainPresenter = MainPresenter(this)
    override fun getLayoutId(): Int = R.layout.activity_main
    override fun initView() {
        super.initView()
        mFragments.clear()
        mFragments.add(WorkSpaceFrag())
        mFragments.add(MineFrag())
        nav_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_work -> {
                    setIndexSelected(0)
                }
                R.id.navigation_mine -> {
                    setIndexSelected(1)
                }
            }
            true
        }
        setIndexSelected(0)
        refresh()
    }

    @SuppressLint("CheckResult")
    fun refresh() {
        val map = HashMap<String, String>()
        RefreshTokenLoader().request(map).subscribe({
            SharedPreferencesUtils.addData(StringConstant.TOKEN, "Bearer ${it.toString()}")
        }, {
            SharedPreferencesUtils.addData(StringConstant.TOKEN, "")
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        })
    }


    var mIndex = -1
    private var mFragments = ArrayList<Fragment>()
    private fun setIndexSelected(index: Int) {
        val ft = supportFragmentManager.beginTransaction()
        if (mIndex > -1) {
            ft.hide(mFragments[mIndex])
        }
        if (!mFragments[index].isAdded) {
            ft.add(R.id.fragment_main_content, mFragments[index]).show(mFragments[index])
        } else {
            ft.show(mFragments[index])
        }
        ft.commit()
        mIndex = index
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun reLogin(event: NotifyReLogin) {
        SharedPreferencesUtils.addData(StringConstant.TOKEN, "")
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onBackPressed() {
        Log.e("---size", "-----" + getActivitySize())
        exitPro()
    }

}