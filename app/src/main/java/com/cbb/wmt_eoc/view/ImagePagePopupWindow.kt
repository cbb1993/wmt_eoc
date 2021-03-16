package com.cbb.wmt_eoc.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.cbb.wmt_eoc.R
import com.github.chrisbanes.photoview.PhotoView

/**
 * Created by 坎坎.
 * Date: 2019/12/25
 * Time: 21:19
 * describe:
 */
class ImagePagePopupWindow(context: Context,urls :List<String>,p:Int) : PopupWindow(
    null, ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT
) {
    private  var viewpager_:ViewPager

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_image_page, null)
        isOutsideTouchable = false
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        animationStyle = R.style.anim_image_page
        viewpager_ = contentView.findViewById(R.id.viewpager_)
        viewpager_.adapter = PageAdapter(context,urls,this)
        viewpager_.currentItem = p
    }

    class PageAdapter(val context: Context,val urls :List<String>,val popupWindow:PopupWindow): PagerAdapter(){
        private var inflate :LayoutInflater = LayoutInflater.from(context)
        override fun isViewFromObject(view: View, o: Any): Boolean = view == o
        override fun getCount(): Int = urls.size
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val root = inflate.inflate(R.layout.item_image_page, null)
            val photo_iv = root.findViewById<PhotoView>(R.id.photo_iv)
            Glide.with(context).load(urls[position]).into(photo_iv)
            photo_iv.setOnClickListener {
                popupWindow.dismiss()
            }
            root.setOnClickListener {
                popupWindow.dismiss()
            }
            container.addView(root)
            return root
        }
    }

    fun show(parent: View) {
        showAtLocation(parent, Gravity.CENTER, 0, 0)
    }

}