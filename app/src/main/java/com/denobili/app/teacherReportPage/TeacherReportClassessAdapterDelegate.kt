package com.denobili.app.teacherReportPage

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.denobili.app.R
import com.denobili.app.attendanceSummaryPage.AttendanceSummaryListActivity
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.teacherAddClasses.ClassListModel
import com.denobili.app.teacherCommunicationReports.TeacherCommunicationReportPage
import com.denobili.app.teacherSocialGradeReport.SocialGradeClassReportPage
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_teacher_report_class.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick

class TeacherReportClassessAdapterDelegate : AdapterDelegate<List<DisplayItem>>() {
    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is ClassListModel
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_teacher_report_class, viewGroup, false)
        return ViewHolder1(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as ViewHolder1
        val item = displayItems[i] as ClassListModel

        holder.class_name.text = item.name

        holder.sms.onClick {
            holder.sms.context.startActivity(
                    holder.sms.context
                            .intentFor<TeacherCommunicationReportPage>
                            (TeacherCommunicationReportPage.communication_type_key to "2",
                                    TeacherCommunicationReportPage.communication_type_name to "SMS Reports",
                                    TeacherCommunicationReportPage.classidkey to item.id.toString()))
        }
        holder.email.onClick {
            holder.email.context.startActivity(
                    holder.email.context
                            .intentFor<TeacherCommunicationReportPage>
                            (TeacherCommunicationReportPage.communication_type_key to "1",
                                    TeacherCommunicationReportPage.communication_type_name to "Email Reports",
                                    TeacherCommunicationReportPage.classidkey to item.id.toString()))
        }
        holder.message.onClick {
            holder.message.context.startActivity(
                    holder.message.context
                            .intentFor<TeacherCommunicationReportPage>
                            (TeacherCommunicationReportPage.communication_type_key to "3",
                                    TeacherCommunicationReportPage.communication_type_name to "Message Reports",
                                    TeacherCommunicationReportPage.classidkey to item.id.toString()))

        }
        holder.attendance.onClick {
            holder.attendance.context.startActivity(
                    holder.attendance.context
                            .intentFor<AttendanceSummaryListActivity>
                            (AttendanceSummaryListActivity.CLASSIDKEY to item.id.toString()))
        }
        holder.grade.onClick {
            holder.grade.context.startActivity(
                    holder.grade.context
                            .intentFor<SocialGradeClassReportPage>
                            (SocialGradeClassReportPage.classidKey to item.id.toString()))
        }


    }

    internal class ViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val class_name: TextView
        internal val sms: ImageView
        internal val email: ImageView
        internal val attendance: ImageView
        internal val grade: ImageView
        internal val message: ImageView

        init {
            class_name = itemView.class_name_report
            sms = itemView.sms_featue_report
            email = itemView.mail_featue_report
            attendance = itemView.attendace_featue_report
            grade = itemView.grade_featue_report
            message = itemView.message_featue_report
        }
    }

    interface TouchcallbackListener {
        fun editItem(myclass: ClassListModel)
    }
}