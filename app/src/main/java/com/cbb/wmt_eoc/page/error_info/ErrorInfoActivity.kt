package com.cbb.wmt_eoc.page.error_info

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.cbb.baselib.StringConstant
import com.cbb.baselib.adapter.CommonAdapter
import com.cbb.baselib.adapter.ViewHolder
import com.cbb.baselib.base.BaseActivity
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.error_record.ErrorRecordModel
import com.cbb.wmt_eoc.page.error_record.Pic
import com.cbb.wmt_eoc.view.ImagePagePopupWindow
import kotlinx.android.synthetic.main.activity_error_info.*

/**
 * Created by 坎坎.
 * Date: 2019/12/28
 * Time: 15:53
 * describe:
 */
class ErrorInfoActivity : BaseActivity<ErrorInfoPresenter>(), ErrorInfoConstract.View {
    override fun initPresenter(): ErrorInfoPresenter? = ErrorInfoPresenter(this)

    override fun getLayoutId(): Int = R.layout.activity_error_info
    private val list = ArrayList<Pic>()
    override fun initView() {
        super.initView()
        recycler_iv.layoutManager = GridLayoutManager(this, 3)
        recycler_iv.adapter =
            object : CommonAdapter<Pic>(this, list, R.layout.item_photo2) {
                override fun convert(viewHolder: ViewHolder, data: MutableList<Pic>) {
                    val iv_ = viewHolder.getView<ImageView>(R.id.iv_)
                    Glide.with(this@ErrorInfoActivity)
                        .load(list[viewHolder.realPosition].redirect)
                        .into(iv_)
                    iv_.setOnClickListener {
                        val photos = ArrayList<String>()
                        list.forEach {
                            if(it.redirect!=null){
                                photos.add(it.redirect)
                            }
                        }
                        ImagePagePopupWindow(this@ErrorInfoActivity, photos,viewHolder.realPosition).show(recycler_iv)
                    }
                }
            }
    }

    override fun initData() {
        super.initData()
        val id: String? = intent.getStringExtra("id")
        id?.run {
            presenter!!.request(id)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun requestSuccess(result: ErrorRecordModel) {
        list.clear()
        list.addAll(result.pics)
        tv_time.text = result.gmtCreate
        tv_location.text = "${result.buildingName}${result.floorName}${result.pointName}"
        tv_location_remark.text =result.pointRemark
        tv_content.text = result.content
        recycler_iv.adapter!!.notifyDataSetChanged()

        if(result.wokePlanName!=null){
            tv_task_name.text = result.wokePlanName
        }
        if(result.gmtStart!=null){
            tv_time_range.text = "${result.gmtStart}-${result.gmtEnd}"
        }
    }

    override fun requestFail(e: String) {
        showToast(e)
    }
}