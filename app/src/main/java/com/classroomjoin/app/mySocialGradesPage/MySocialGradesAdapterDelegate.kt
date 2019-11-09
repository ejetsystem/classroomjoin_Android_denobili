package com.classroomjoin.app.mySocialGradesPage

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.classroomjoin.app.R
import com.classroomjoin.app.addSocialGrade.AddSocialGradePage
import com.classroomjoin.app.helper_utils.DisplayItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_social_grade.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick


class MySocialGradesAdapterDelegate : AdapterDelegate<List<DisplayItem>>() {
    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is MySocialGradesModel
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_social_grade, viewGroup, false)
        return SocialGrade(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as SocialGrade
        val item = displayItems[i] as MySocialGradesModel

        holder.social_grade_name.text = item.name.trim()
        holder.social_grade_mark.text = item.point.toString()
        if (item.flag == 0) {
            holder.social_grade_image.imageResource = R.drawable.ic_thumb_down_green_24dp
            holder.social_grade_mark.setTextColor(Color.parseColor("#ff0000"))
        } else {
            holder.social_grade_mark.setTextColor(Color.parseColor("#008000"))
            holder.social_grade_image.imageResource = R.drawable.ic_thumb_up_green_24dp
        }

        holder.itemView.onClick {
            holder.itemView.context.startActivity(
                    holder.itemView.context.intentFor<AddSocialGradePage>(AddSocialGradePage.SOCIAL_GRADE_ID_KEY to item.id.toString(),
                            AddSocialGradePage.SOCIAL_GRADE_HEADING to item.name.trim(),
                            AddSocialGradePage.SOCIAL_GRADE_MARK to item.point.toString(),
                            AddSocialGradePage.SOCIAL_GRADE_TYPE to item.flag)
            )
        }


    }

    internal class SocialGrade(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val social_grade_name: TextView
        internal val social_grade_mark: TextView
        internal val social_grade_image: ImageView

        init {
            social_grade_name = itemView.socia_grade_name
            social_grade_mark = itemView.social_grade_mark
            social_grade_image = itemView.social_grade_type
        }
    }

}