package com.denobili.app.postAttendancePage

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.denobili.app.R
import com.denobili.app.helper.CheckableImageView
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.mystudentsPage.MyStudentModel
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_student_attendance_list_row.view.*


class AttendenceSelectionListAdapterDelegate : AdapterDelegate<List<DisplayItem>>() {

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is MyStudentModel && (displayItems[i] as MyStudentModel).isListView!!
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(viewGroup.context).inflate(R.layout.row_student_attendance_list_row,viewGroup,false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentViewHolder
        val item =displayItems[i] as MyStudentModel

        var user_name: String? = ""

        if (!item.fname.isNullOrBlank())
            user_name = item.fname

        if (!item.mname.isNullOrBlank())
            user_name = user_name + " " + item.mname

        if (!item.lname.isNullOrBlank())
            user_name = user_name + " " + item.lname
        holder.name.text=user_name
        holder.roll_no.text=item.roll_no.toString()
        holder.select.isChecked=item.isSelected!!
    }

    internal class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        internal val select:CheckableImageView
        internal val name:TextView
        internal val roll_no:TextView
        init {
            select=itemView.checkableImageView
            name=itemView.row_attendance_name
            roll_no=itemView.roll_no_text_summary
        }
    }

}