package com.denobili.app.teacherAddClasses

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.denobili.app.R
import com.denobili.app.helper.CircularTextView
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.teacherHomePage.TeacherHomePage
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_class_linear.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import java.util.*

class ClassListAdapterDelegate(val touchcallback: TouchcallbackListener, context:Context, val isadmin:Boolean) : AdapterDelegate<List<DisplayItem>>() {
    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is ClassListModel
    }


    internal val androidColors:IntArray
    init {
        androidColors = context.resources.getIntArray(R.array.androidcolors)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(viewGroup.context).inflate(R.layout.row_class_linear,viewGroup,false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentViewHolder
        val item =displayItems[i] as ClassListModel

        holder.name_short.text=item.name
        holder.no_of_students.text=item.getNoofStudents().toString()+ " no. of student(s)"
        holder.student_name_short.setText(item.name)
        val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
        holder.student_name_short.setSolidColor(randomAndroidColor)
        holder.itemView.onClick {
            holder.itemView.context
                    .startActivity(holder.itemView.context
                            .intentFor<TeacherHomePage>
                            (TeacherHomePage.CLASSIDKEY to item.id.toString()))
        }

        /*val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]

        holder.card_background.backgroundColor=randomAndroidColor*/

        /*if (i % 2 != 0) {
            holder.card_background.setBackgroundColor(Color.parseColor("#B0BEC5"))

        } else {
            holder.card_background.setBackgroundColor(Color.parseColor("#CFD8DC"))

        }*/

        /*if(!isadmin) {
            holder.edit_button.visibility = View.VISIBLE
        }else{
            holder.edit_button.visibility = View.INVISIBLE

        }*/


        holder.edit_button.onClick {
           touchcallback.editItem(item)
        }

    }

    internal class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        internal val name_short:TextView
        internal val edit_button:ImageView
        //internal val card_background:ConstraintLayout
        internal val no_of_students:TextView
        internal val student_name_short: CircularTextView

        init {
            name_short =itemView.class_name
            edit_button=itemView.class_edit
            //card_background=itemView.card_background
            no_of_students=itemView.no_of_students
            student_name_short = itemView.text_first_class

        }
    }

    interface TouchcallbackListener {
       fun editItem(myclass: ClassListModel)
    }
}