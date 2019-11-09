package com.classroomjoin.app.myConnectedStudents

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.CircularTextView
import com.classroomjoin.app.helper_utils.DisplayItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_student_linear.view.*
import java.util.*


class ConnectedStudentAdapterDelegate(context: Context) : AdapterDelegate<List<DisplayItem>>() {

    internal val androidColors: IntArray

    init {
        androidColors = context.resources.getIntArray(R.array.androidcolors)
    }

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is MyConnectedStudentModel
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_student_linear, viewGroup, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentViewHolder
        val item = displayItems[i] as MyConnectedStudentModel

        var user_name: String? = null
        if (!item.firstName.isNullOrBlank())
            user_name = item.firstName

        if (!item.middleName.isNullOrBlank())
            user_name = user_name + " " + item.middleName

        if (!item.lastName.isNullOrBlank())
            user_name = user_name + " " + item.lastName


        holder.student_name.text = user_name
        holder.student_classname.text = item.classname
        holder.student_name_short.text = user_name

        val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
        holder.student_name_short.solidColor = randomAndroidColor

    }

    internal class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val student_name: TextView
        internal val student_classname: TextView
        internal val student_name_short: CircularTextView

        init {
            student_classname = itemView.row_connected_student_classname
            student_name = itemView.row_connected_student_name
            student_name_short = itemView.row_connected_studentname_short
        }

    }

}