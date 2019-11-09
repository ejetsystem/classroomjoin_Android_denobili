package com.classroomjoin.app.studentDiaryPage

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.classroomjoin.app.R
import com.classroomjoin.app.helper_utils.DisplayItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_student_diary_date.view.*
import java.text.SimpleDateFormat
import java.util.*


class StudentDiaryDateAdapterDelegate : AdapterDelegate<List<DisplayItem>>() {

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is StudentDiaryDateModel
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_student_diary_date, viewGroup, false)
        return StudentDiaryDateViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentDiaryDateViewHolder
        val item = displayItems[i] as StudentDiaryDateModel
        holder.event_date.text = getDateFormat(item.date)
    }

    fun getDateFormat(date11: String?): String {
        var added_date = date11
        var spf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val newDate = spf.parse(added_date)

        spf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        added_date = spf.format(newDate)
        val date = spf.parse(added_date)
        val startDate = date.time

        if (isToday(startDate)) {
            spf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return spf.format(newDate)
        } else if (isYesterday(startDate)) {
            return "Yesterday"
        } else {
            return added_date
        }

        return ""

    }


    fun isYesterday(date: Long): Boolean {
        val now = Calendar.getInstance()
        val cdate = Calendar.getInstance()
        cdate.timeInMillis = date

        now.add(Calendar.DATE, -1)

        return (now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE))
    }

    fun isToday(date: Long): Boolean {
        val now = Calendar.getInstance()
        val cdate = Calendar.getInstance()
        cdate.timeInMillis = date

        now.add(Calendar.DATE, 0)

        return (now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE))
    }

    internal class StudentDiaryDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val event_date: TextView

        init {
            event_date = itemView.diary_month

        }
    }

}