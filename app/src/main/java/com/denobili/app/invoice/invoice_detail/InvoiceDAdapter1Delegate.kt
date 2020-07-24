package com.denobili.app.invoice.invoice_detail

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.denobili.app.R
import com.denobili.app.helper_utils.DisplayItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.particular.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class InvoiceDAdapter1Delegate (context: Context) : AdapterDelegate<List<DisplayItem>>() {
    var mContext: Context?=null

    internal val androidColors: IntArray

    init {
        androidColors = context.resources.getIntArray(R.array.androidcolors)
        mContext=context
    }

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is ParticularAnnualModel
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.particular, viewGroup, false)

        return StudentViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentViewHolder

        val item = displayItems[i] as ParticularAnnualModel
        holder.name.text=item.name
        holder.amount.text="Amount-"+" "+ item.amount
        //holder.desc.text=item.description
        //holder.unit.text=item.amount






        //val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
        //holder.student_name_short.solidColor = randomAndroidColor



    }
    @Throws(ParseException::class)
    private fun getMonth(date:String):String {
        val d = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)
        val cal = Calendar.getInstance()
        cal.setTime(d)
        val monthName = SimpleDateFormat("MMMM").format(cal.getTime())
        return monthName
    }
    internal class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val name: TextView
        internal val amount: TextView
        //internal val unit: TextView
        internal val desc: TextView




        init {
            name = itemView.name
            amount = itemView.amount
            // unit = itemView.unit
            desc = itemView.desc


        }

    }

}