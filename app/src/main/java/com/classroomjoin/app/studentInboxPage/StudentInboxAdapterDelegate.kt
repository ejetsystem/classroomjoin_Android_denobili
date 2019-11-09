package com.classroomjoin.app.studentInboxPage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.CircularTextView
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.OfflineAttachmentModel
import com.classroomjoin.app.helper_utils.OfflineParentAttachmentAdapter
import com.classroomjoin.app.helper_utils.OfflineParentAttachmentDelegate
import com.classroomjoin.app.studentEventDetailPage.EventDetailActivity
import com.classroomjoin.app.studentallEventsPage.StudentEventModel
import kotlinx.android.synthetic.main.row_inbox.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*


class StudentInboxAdapterDelegate(private var context11: android.content.Context) : com.hannesdorfmann.adapterdelegates3.AdapterDelegate<List<DisplayItem>>(), OfflineParentAttachmentDelegate.AddAttachmentListener {
    override fun onClicked(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context!!.startActivity(browserIntent)
    }

    var context: Context? = null


    init {
        context = context11

    }

    private val androidColors: IntArray = context!!.resources.getIntArray(com.classroomjoin.app.R.array.androidcolors)

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean =
            displayItems[i] is StudentEventModel

    override fun onCreateViewHolder(viewGroup: android.view.ViewGroup): android.support.v7.widget.RecyclerView.ViewHolder {
        val view = android.view.LayoutInflater.from(viewGroup.context).inflate(com.classroomjoin.app.R.layout.row_inbox, viewGroup, false)
        return InboxViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: android.support.v7.widget.RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as InboxViewHolder
        val item = displayItems[i] as StudentEventModel

        holder.teacher_name.text = item.teacher_name
        holder.event_date.text = getDateFormat(item.date)
        holder.event_message.text = item.message
        holder.event_subject.text = item.subject
        holder.teacher_short.text = item.teacher_name


        val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
        holder.teacher_short.solidColor = randomAndroidColor


        holder.lyt_parentww.onClick {
            holder.lyt_parentww.context.startActivity(holder.lyt_parentww.context.intentFor<EventDetailActivity>
            (EventDetailActivity.Companion.EVENT_ID_KEY to item.id, EventDetailActivity.Companion.EVENT_TYPE_ID_KEY to "3"))
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

    private fun getDateFormat(date11: String?): String {
        var added_date = date11
        var spf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val newDate = spf.parse(added_date)

        spf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        added_date = spf.format(newDate)
        val date = spf.parse(added_date)
        val startDate = date.time

        when {
            isToday(startDate) -> {
                spf = SimpleDateFormat("HH:mm", Locale.getDefault())
                return spf.format(newDate)
            }
            isYesterday(startDate) -> return "Yesterday"
            else -> {
                spf = SimpleDateFormat("dd MMM", Locale.getDefault())
                added_date = spf.format(newDate)
                return added_date
            }
        }

        return ""

    }


    private fun isYesterday(date: Long): Boolean {
        val now = Calendar.getInstance()
        val cdate = Calendar.getInstance()
        cdate.timeInMillis = date

        now.add(Calendar.DATE, -1)

        return (now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE))
    }

    private fun isToday(date: Long): Boolean {
        val now = Calendar.getInstance()
        val cdate = Calendar.getInstance()
        cdate.timeInMillis = date

        now.add(Calendar.DATE, 0)

        return (now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE))
    }


    private fun initializeAdapter(holder: InboxViewHolder, items: List<OfflineAttachmentModel>) {
        val attachmentAdapter = OfflineParentAttachmentAdapter(items, this, context!!)
        holder.recycler_view.adapter = attachmentAdapter
        holder.recycler_view.visibility = View.VISIBLE

    }

    internal class InboxViewHolder(itemView: android.view.View) : android.support.v7.widget.RecyclerView.ViewHolder(itemView) {
        internal val teacher_name: android.widget.TextView
        internal val event_subject: android.widget.TextView
        internal val event_date: android.widget.TextView
        internal val event_message: android.widget.TextView
        internal val teacher_short: CircularTextView
        internal val recycler_view: RecyclerView
        internal val lyt_parentww: LinearLayout

        init {
            teacher_name = itemView.teacher_name
            event_date = itemView.event_date
            event_subject = itemView.event_teacher_name
            event_message = itemView.event_message
            teacher_short = itemView.circularTextView2
            lyt_parentww = itemView.lyt_parentww
            recycler_view = itemView.recycler_attachments
            recycler_view.layoutManager = LinearLayoutManager(itemView.context)
            recycler_view.isNestedScrollingEnabled = false


        }
    }

}