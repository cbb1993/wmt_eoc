package com.cbb.wmt_eoc.page.error_record

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cbb.baselib.StringConstant
import com.cbb.baselib.adapter.CommonAdapter
import com.cbb.baselib.adapter.ViewHolder
import com.cbb.baselib.base.BaseActivity
import com.cbb.wmt_eoc.R
import com.cbb.wmt_eoc.page.error_info.ErrorInfoActivity
import kotlinx.android.synthetic.main.activity_report_record.*

/**
 * Created by 坎坎.
 * Date: 2019/12/27
 * Time: 11:19
 * describe:
 */
class ErrorRecordActivity : BaseActivity<ErrorRecordPresenter>(), ErrorRecordConstract.View {


    override fun initPresenter(): ErrorRecordPresenter? = ErrorRecordPresenter(this)

    override fun getLayoutId(): Int = R.layout.activity_report_record

    private val list = ArrayList<ErrorRecordModel>()
//    private val list2= ArrayList<ErrorRecordModel>()

    override fun initView() {
        super.initView()
        recycler_record.layoutManager = LinearLayoutManager(this)
        recycler_record.adapter =
            object : CommonAdapter<ErrorRecordModel>(this, list, R.layout.item_record) {
                @SuppressLint("SetTextI18n")
                override fun convert(holder: ViewHolder, t: MutableList<ErrorRecordModel>) {
                    val root = holder.getView<View>(R.id.root)
                    val tv_time = holder.getView<TextView>(R.id.tv_time)
                    val tv_location = holder.getView<TextView>(R.id.tv_location)
                    val tv_content = holder.getView<TextView>(R.id.tv_content)
                    t[holder.realPosition].run {
                        tv_time.text = "故障时间：${this.gmtCreate}"
                        tv_location.text =
                            "故障地点：${this.buildingName}${this.floorName}${this.pointName}"
                        tv_content.text = this.content
                    }

                    root.setOnClickListener {
                        startActivity(
                            Intent(this@ErrorRecordActivity, ErrorInfoActivity::class.java)
                                .putExtra("id", t[holder.realPosition].id)
                        )
                    }
                    val recycler_iv = holder.getView<RecyclerView>(R.id.recycler_iv)
                    recycler_iv.layoutManager = GridLayoutManager(this@ErrorRecordActivity, 3)
                    recycler_iv.adapter =
                        object : CommonAdapter<Pic>(
                            this@ErrorRecordActivity,
                            t[holder.realPosition].pics,
                            R.layout.item_photo2
                        ) {
                            override fun convert(viewHolder: ViewHolder, data: MutableList<Pic>) {
                                val iv_ = viewHolder.getView<ImageView>(R.id.iv_)
                                Glide.with(this@ErrorRecordActivity)
                                    .load(data[viewHolder.realPosition].redirect)
                                    .into(iv_)
                            }
                        }
                }
            }

        smart_.setOnRefreshListener {
            current = 1
            presenter!!.request(current)
        }
        smart_.setOnLoadMoreListener {
            current ++
            presenter!!.request(current)
        }
    }

    var current = 1
    fun finishLoad() {
        smart_.finishRefresh()
        smart_.finishLoadMore()
    }

    override fun initData() {
        super.initData()
        presenter!!.request(current)
    }

    override fun requestSuccess(result: List<ErrorRecordModel>) {
        if(current == 1){
            list.clear()
        }
        list.addAll(result)
        recycler_record.adapter!!.notifyDataSetChanged()
        finishLoad()
    }

    override fun requestFail(e: String) {
        showToast(e)
        finishLoad()
    }

}