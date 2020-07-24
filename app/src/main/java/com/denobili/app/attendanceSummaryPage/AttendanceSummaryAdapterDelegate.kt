package com.denobili.app.attendanceSummaryPage

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.denobili.app.R
import com.denobili.app.helper_utils.DisplayItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_student_attendance_summary_list_linear.view.*


class AttendanceSummaryAdapterDelegate(val classid: String) : AdapterDelegate<List<DisplayItem>>() {


    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean =
            displayItems[i] is AttendanceSummaryRow

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_student_attendance_summary_list_linear, viewGroup, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentViewHolder
        val item = displayItems[i] as AttendanceSummaryRow

        holder.student_name.text = item.name!!.trim()
        holder.student_number.text = item.id.toString()
        holder.summary.text = (" " + item.pre_days + " out of " + item.no_days + " days Present").trim()
        holder.percentage.text = item.getPercentage()

        /*holder.itemView.onClick {
            holder.itemView.context.startActivity(
                    holder.itemView.context.intentFor<AttendanceActivity>(AttendanceActivity.studentidkey to item.id.toString(),AttendanceActivity.studentidkey to)
            )
        }
*/

    }

    internal class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val student_name: TextView = itemView.student_name_summary
        internal val student_number: TextView = itemView.roll_no_text_summary
        internal val summary: TextView = itemView.status_no
        internal val percentage: TextView = itemView.percentage
    }

}