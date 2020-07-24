package com.denobili.app.landing_page

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.denobili.app.R

class ViewPagerAdapter(internal var context: Context, internal var itemList: List<ViewItem>) : PagerAdapter() {

    internal var mLayoutInflater: LayoutInflater
    private var pos = 0

    init {
        mLayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return Integer.MAX_VALUE
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val holder = ViewHolder()
        val itemView = mLayoutInflater.inflate(R.layout.slidingimages_layout, container, false)
        holder.itemImage = itemView.findViewById(R.id.image) as ImageView

        if (pos > this.itemList.size - 1)
            pos = 0

        holder.sliderItem = this.itemList[pos]
        holder.itemImage.setImageDrawable(context.getDrawable(holder.sliderItem.imageDrawable))

        (container as ViewPager).addView(itemView)

        holder.itemImage.scaleType = ImageView.ScaleType.FIT_XY

        pos++
        return itemView
    }

    internal class ViewHolder {
        lateinit var sliderItem: ViewItem
        lateinit var itemImage: ImageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1 as View
    }

    override fun destroyItem(arg0: View, arg1: Int, arg2: Any) {
        (arg0 as ViewPager).removeView(arg2 as View)
    }
}