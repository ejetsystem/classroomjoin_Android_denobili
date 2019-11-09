package com.classroomjoin.app.socialGradeSelectionPage

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.classroomjoin.app.R
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.mySocialGradesPage.MySocialGradesModel
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_social_grade_selection.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick


class SocialGradeSelectionAdapterDelegate(listener: SocialListner) : AdapterDelegate<List<DisplayItem>>() {

    internal var listener: SocialListner

    init {
        this.listener = listener
    }
    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is MySocialGradesModel
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(viewGroup.context).inflate(R.layout.row_social_grade_selection,viewGroup,false)
        return SocialGrade(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as SocialGrade
        val item =displayItems[i] as MySocialGradesModel

        holder.social_grade_name.text = item.name.trim()
        holder.social_grade_mark.text = item.point.toString()
        holder.social_grade_isselected.isChecked=item.isselected

        holder.social_grade_isselected.onClick {
            listener.selected(i, !item.isselected)
        }

        if(item.flag==0){
            holder.social_grade_image.imageResource=R.drawable.ic_thumb_down_green_24dp
        }


    }

    internal class SocialGrade(itemView: View) : RecyclerView.ViewHolder(itemView){
        internal val social_grade_name: TextView
        internal val social_grade_mark:TextView
        internal val social_grade_image: ImageView
        internal val social_grade_isselected:CheckBox
        init {
            social_grade_name =itemView.socia_grade_name
            social_grade_mark =itemView.social_grade_mark
            social_grade_image=itemView.social_grade_type
            social_grade_isselected=itemView.social_grade_selected
        }
    }

}