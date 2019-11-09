package com.classroomjoin.app.studentListingPage

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.CircularTextView
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.StudentSelectionListener
import com.classroomjoin.app.mystudentsPage.MyStudentModel
import com.classroomjoin.app.studentDetailPage.StudentDetailPage

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_student_selection.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import java.util.*


class StudentSelectionAdapterDelegate(listener: StudentSelectionListener, context11: Context, classid: String?) : AdapterDelegate<List<DisplayItem>>(), AnkoLogger {


    internal var listener: StudentSelectionListener

    internal val androidColors: IntArray

    var class_id: String? = ""

    var context: Context? = null
    var view: View? = null

    init {
        this.class_id = classid
        this.listener = listener
        androidColors = context11.getResources().getIntArray(R.array.androidcolors)
        this.context = context11
    }

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is MyStudentModel
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_student_selection, viewGroup, false)
        return StudentViewHolder(view!!)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentViewHolder
        val item = displayItems[i] as MyStudentModel

        var user_name: String? = ""

        if (!item.fname.isNullOrBlank())
            user_name = item.fname

        if (!item.mname.isNullOrBlank())
            user_name = user_name + " " + item.mname

        if (!item.lname.isNullOrBlank())
            user_name = user_name + " " + item.lname

        holder.student_name.setText(user_name)
        holder.student_number.setText(item.admission_id)
        holder.student_name_short.setText(user_name)
        holder.select.isChecked = item.isSelected!!
        holder.roll_no.text = item.roll_no.toString()

        val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
        holder.student_name_short.setSolidColor(randomAndroidColor)

        holder.select.onClick {

            listener.selectedStudent(i, !item.isSelected!!)


            /*println("Data--->" + item.parentMobile)
            if (item.parentMobile != null && !item.parentMobile.equals(""))
                listener.selectedStudent(i, !item.isSelected!!)
            else {
                Snackbar.make(view!!, context!!.getString(R.string.parent_mobile_not_exist), Snackbar.LENGTH_SHORT).show()
                holder.select.isChecked=false
            }*/
        }

        holder.itemView.onClick {
            holder.itemView.context.startActivity(holder.itemView.context.intentFor<StudentDetailPage>(
                    StudentDetailPage.STUDENT_ID_KEY to item.id.toString(),
                    StudentDetailPage.Student_Name_Key to user_name,
                    StudentDetailPage.CLASS_ID_KEY to class_id))
        }

    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val student_name_short: CircularTextView
        internal val student_name: TextView
        internal val student_number: TextView
        internal val select: RadioButton
        internal val roll_no: TextView

        init {
            student_name = itemView.student_name_selection
            student_number = itemView.student_roll_no_selection
            select = itemView.social_grade_selected
            roll_no = itemView.roll_no_text_summary
            student_name_short = itemView.text_first
        }


    }


}