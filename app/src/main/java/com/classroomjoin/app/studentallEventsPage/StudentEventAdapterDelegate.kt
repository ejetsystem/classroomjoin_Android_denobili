package com.classroomjoin.app.studentallEventsPage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.classroomjoin.app.R
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.OfflineAttachmentModel
import com.classroomjoin.app.helper_utils.OfflineParentAttachmentAdapter
import com.classroomjoin.app.helper_utils.OfflineParentAttachmentDelegate
import com.classroomjoin.app.studentEventDetailPage.EventDetailActivity
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_student_events.view.*
import org.jetbrains.anko.image
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*


class StudentEventAdapterDelegate(private var context11: android.content.Context) : AdapterDelegate<List<DisplayItem>>(), OfflineParentAttachmentDelegate.AddAttachmentListener {
    override fun onClicked(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context!!.startActivity(browserIntent)
    }

    internal val androidColors: IntArray
    var context: Context? = null

    init {
        androidColors = context11.resources.getIntArray(R.array.androidcolors)
        context = context11
    }

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is StudentEventModel
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_student_events, viewGroup, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(displayItems:List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as EventViewHolder
        val item = displayItems[i] as StudentEventModel

        holder.teacher_name.text = item.teacher_name
        holder.event_date.text = getDateFormat(item.date)
        holder.event_message.text = item.message
        holder.event_subject.text = item.subject
        holder.social_grade_mark.text = item.grade

        if(item.grade!=null&&!item.grade.equals("")){

            val number = item.grade!!.toInt()
            if (number < 0) {
                // negative
                holder.event_subject.text = context!!.getString(R.string.negative_social_grade)

            } else {
                // it's a positive
                holder.event_subject.text = context!!.getString(R.string.positive_social_grade)

            }

        }

        when (item.event_type) {
            1 -> holder.setDrawable(context!!, R.drawable.social_parent)
            2 -> {
                when (item.status_flag) {
                    1 -> holder.setDrawable(context!!, R.drawable.diary_parent)
                    2 -> holder.setDrawable(context!!, R.drawable.assignment_parent)
                    3 -> holder.setDrawable(context!!, R.drawable.notic_parent)
                    4 -> holder.setDrawable(context!!, R.drawable.events_parent)
                }
            }
            3 -> holder.setDrawable(context!!, R.drawable.inbox_parent)
            4 -> holder.setDrawable(context!!, R.drawable.attendance_parent)

        }
        holder.lyt_parent.onClick {
            context!!.startActivity(context!!.intentFor<EventDetailActivity>
            (EventDetailActivity.Companion.EVENT_ID_KEY to item.id,
                    EventDetailActivity.Companion.EVENT_TYPE_ID_KEY to item.event_type.toString()))
        }


        if (item.attachments != null) if (item.attachments!!.isNotEmpty()) initializeAdapter(holder, item.attachments!!)


        if (item.statusRead != null && item.statusRead == 0) {

            holder.teacher_name.textColor = context!!.resources.getColor(R.color.titles_color)
            holder.event_date.textColor = context!!.resources.getColor(R.color.material_gre)
            holder.event_message.textColor = context!!.resources.getColor(R.color.material_gre)
            holder.event_subject.textColor = context!!.resources.getColor(R.color.material_gre)
            holder.teacher_short.alpha = 1f

        } else if (item.statusRead != null && item.statusRead == 1) {

            holder.teacher_name.textColor = context!!.resources.getColor(R.color.grey_color)
            holder.event_date.textColor = context!!.resources.getColor(R.color.grey_color)
            holder.event_message.textColor = context!!.resources.getColor(R.color.grey_color)
            holder.event_subject.textColor = context!!.resources.getColor(R.color.grey_color)
            holder.teacher_short.alpha = 0.5f
        }


    }

    fun getDateFormat(date11: String?): String {
        var added_date = date11
        System.out.println("Data--->" + "getDateFormat hh-->" + added_date)

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

            spf = SimpleDateFormat("dd MMM", Locale.getDefault())
            added_date = spf.format(newDate)
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

    private fun initializeAdapter(holder: EventViewHolder, items: List<OfflineAttachmentModel>) {
        val attachmentAdapter = OfflineParentAttachmentAdapter(items, this,context!!)
        holder.recycler_view.adapter = attachmentAdapter
        holder.recycler_view.visibility = View.VISIBLE

    }

    internal class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val teacher_name: TextView
        internal val event_subject: TextView
        internal val event_date: TextView
        internal val event_message: TextView
        internal val teacher_short: ImageView
        internal val social_grade_mark: TextView
        internal val recycler_view: RecyclerView
        internal val lyt_parent: LinearLayout

        init {
            teacher_name = itemView.teacher_name
            event_date = itemView.event_date
            event_subject = itemView.event_teacher_name
            event_message = itemView.event_message
            teacher_short = itemView.text_first
            social_grade_mark = itemView.social_grade_mark
            lyt_parent = itemView.lyt_parent
            recycler_view = itemView.recycler_attachments
            recycler_view.layoutManager = LinearLayoutManager(itemView.context)
            recycler_view.isNestedScrollingEnabled = false

        }

        fun setDrawable(context: Context, resid: Int) {
            teacher_short.image = ContextCompat.getDrawable(context, resid)

        }

    }

}