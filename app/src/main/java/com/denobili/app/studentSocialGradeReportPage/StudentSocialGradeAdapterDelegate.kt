package com.denobili.app.studentSocialGradeReportPage

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.denobili.app.R
import com.denobili.app.helper_utils.DisplayItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_social_grade_report_student.view.*
import org.jetbrains.anko.AnkoLogger


class StudentSocialGradeAdapterDelegate : AdapterDelegate<List<DisplayItem>>(), AnkoLogger {

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is SocialGradeStudentReport
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_social_grade_report_student, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as ViewHolder
        val item = displayItems[i] as SocialGradeStudentReport

        holder.name.text = item.name
        holder.teacher_name.text = item.teachername
        holder.mark.text = item.grade
        holder.date.text = item.date

        if (item.flag.equals("1")) {
            holder.mark.setBackgroundColor(Color.parseColor("#55A300"))
        } else {
            holder.mark.setBackgroundColor(Color.parseColor("#9B1300"))

        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val name: TextView
        internal val mark: TextView
        internal val teacher_name: TextView
        internal val date: TextView

        init {
            name = itemView.social_grade_name_report
            mark = itemView.social_grade_mark_report
            teacher_name = itemView.social_grade_teacher_name
            date = itemView.id_social_grage_date

        }


    }


}