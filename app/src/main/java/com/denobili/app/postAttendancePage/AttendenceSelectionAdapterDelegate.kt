package com.denobili.app.postAttendancePage

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denobili.app.R
import com.denobili.app.helper.CircularCheckableTextView
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.mystudentsPage.MyStudentModel
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_student_selection_grid.view.*


class AttendenceSelectionAdapterDelegate : AdapterDelegate<List<DisplayItem>>() {

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is MyStudentModel && !(displayItems[i] as MyStudentModel).isListView!!
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_student_selection_grid, viewGroup, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentViewHolder
        val item = displayItems[i] as MyStudentModel
        holder.select.text = item.roll_no.toString()
        holder.select.isChecked = item.isSelected!!

    }

    internal class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val select: CircularCheckableTextView

        init {
            select = itemView.student_roll_no_selection
        }
    }

}