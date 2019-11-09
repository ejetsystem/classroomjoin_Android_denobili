package com.classroomjoin.app.teacherCommunicationReports

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.classroomjoin.app.R
import com.classroomjoin.app.ReportsMessageDetails
import com.classroomjoin.app.helper.CircularTextView
import com.classroomjoin.app.helper_utils.DisplayItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_communication_report.view.*
import org.jetbrains.anko.image
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import java.text.SimpleDateFormat
import java.util.*

class TeacherCommunicationReportAdapterDelegate(context11: android.content.Context) : AdapterDelegate<List<DisplayItem>>() {

    private val androidColors: IntArray
    var context: Context? = null

    init {
        context = context11
        androidColors = context11.resources.getIntArray(com.classroomjoin.app.R.array.androidcolors)
    }

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean =
            displayItems[i] is ReportModel


    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_communication_report, viewGroup, false)
        return ViewHolder1(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as ViewHolder1
        val item = displayItems[i] as ReportModel

        if (item.student_name != null && !item.student_name.equals(""))
            holder.student_name.text = item.student_name
        else
            holder.student_name.text = context!!.getString(R.string.no_student)


        if (item.message != null)
            holder.message.text = item.message
        else
            holder.message.text = item.message1

        holder.date.text = getDateFormat(item.date)

        holder.circularTextView2_report.text = item.student_name


        val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
        holder.circularTextView2_report.solidColor = randomAndroidColor

        if (item.status != null && item.status.equals("success")) {

            if (item.statusRead != null && item.statusRead == 1) {
                holder.meassage_status.image = ContextCompat.getDrawable(context!!, R.mipmap.read_tick)
            } else if (item.statusRead != null && item.statusRead == 0) {
                holder.meassage_status.image = ContextCompat.getDrawable(context!!, R.mipmap.delivery_tick)
            }
        } else {
            holder.meassage_status.image = ContextCompat.getDrawable(context!!, R.mipmap.unsuuceess)

        }

        holder.lyt_parent_report.onClick {
            context!!.startActivity(context!!.intentFor<ReportsMessageDetails>
            (ReportsMessageDetails.eventdiarydetail.STUDENT_NAME to item.student_name,
                    ReportsMessageDetails.eventdiarydetail.CLASS_NAME to item.className,
                    ReportsMessageDetails.eventdiarydetail.MESSAGE to item.message,
                    ReportsMessageDetails.eventdiarydetail.MESSAGE1 to item.message1,
                    ReportsMessageDetails.eventdiarydetail.STATUS to item.statusRead.toString(),
                    ReportsMessageDetails.eventdiarydetail.DATE to item.date))
        }


    }

    internal class ViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val date: TextView
        internal val message: TextView
        internal val student_name: TextView
        internal val circularTextView2_report: CircularTextView
        internal val meassage_status: ImageView
        internal val lyt_parent_report: LinearLayout


        init {
            date = itemView.event_date_report
            message = itemView.event_message_report
            student_name = itemView.student_name_report
            circularTextView2_report = itemView.circularTextView2_report
            meassage_status = itemView.meassage_status
            lyt_parent_report = itemView.lyt_parent_report

        }
    }

    private fun getDateFormat(date11: String?): String {
        var added_date = date11
        var spf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val newDate = spf.parse(added_date)

        spf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        added_date = spf.format(newDate)
        val date = spf.parse(added_date)
        val startDate = date.time

        return if (isToday(startDate)) {
            spf = SimpleDateFormat("HH:mm", Locale.getDefault())
            spf.format(newDate)
        } else if (isYesterday(startDate)) {
            "Yesterday"
        } else {
            spf = SimpleDateFormat("dd MMM", Locale.getDefault())
            added_date = spf.format(newDate)
            added_date
        }

        // return ""

    }


    private fun isYesterday(date: Long): Boolean {
        val now = Calendar.getInstance()
        val cdate = Calendar.getInstance()
        cdate.timeInMillis = date

        now.add(Calendar.DATE, -1)

        return (now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE))
    }

    private fun isToday(date: Long): Boolean {
        val now = Calendar.getInstance()
        val cdate = Calendar.getInstance()
        cdate.timeInMillis = date

        now.add(Calendar.DATE, 0)

        return (now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE))
    }

}