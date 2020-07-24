package com.denobili.app.studentDiaryPage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.denobili.app.R
import com.denobili.app.helper.CircularTextView
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.OfflineAttachmentModel
import com.denobili.app.helper_utils.OfflineParentAttachmentAdapter
import com.denobili.app.helper_utils.OfflineParentAttachmentDelegate
import com.denobili.app.studentDiaryEventDetailPage.DiaryEventDetailActivity
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_inbox.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*


class StudentDiaryAdapterDelegate(private var context11: Context,event_type11:String) : AdapterDelegate<List<DisplayItem>>(), OfflineParentAttachmentDelegate.AddAttachmentListener {

    var context:Context?=null

    override fun onClicked(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context11.startActivity(browserIntent)
    }

    internal val androidColors: IntArray
    var event_type:String?=null

    init {
        context=context11
        androidColors = context11.resources.getIntArray(R.array.androidcolors)
        event_type=event_type11
    }

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is StudentDiaryEventModel
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_inbox, viewGroup, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentViewHolder
        val item = displayItems[i] as StudentDiaryEventModel

        holder.teacher_name.text = item.teacher_name
        holder.event_date.text = getDateFormat(item.date)
        holder.event_message.text = item.message
        holder.event_subject.text = item.subject
        holder.teacher_short.text = item.teacher_name


        val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
        holder.teacher_short.solidColor = randomAndroidColor

        holder.lyt_parent.onClick {
            context!!.startActivity(context!!.intentFor<DiaryEventDetailActivity>
            (DiaryEventDetailActivity.eventdiarydetail.EVENT_ID_KEY to item.id,DiaryEventDetailActivity.eventdiarydetail.EVENT_TYPE_ID_KEY to event_type))
        }

        if (item.attachments != null) if (item.attachments!!.isNotEmpty())
            initializeAdapter(holder, item.attachments!!,context!!)


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

    private fun initializeAdapter(holder: StudentViewHolder, items: List<OfflineAttachmentModel>,context11: Context) {
        val attachmentAdapter = OfflineParentAttachmentAdapter(items, this,context11)
        holder.recycler_view.adapter = attachmentAdapter
        holder.recycler_view.visibility = View.VISIBLE
    }

    internal class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val teacher_name: TextView
        internal val event_subject: TextView
        internal val event_date: TextView
        internal val event_message: TextView
        internal val teacher_short: CircularTextView
        internal val recycler_view: RecyclerView
        internal val lyt_parent: LinearLayout

        init {
            teacher_name = itemView.teacher_name
            event_date = itemView.event_date
            event_subject = itemView.event_teacher_name
            event_message = itemView.event_message
            teacher_short = itemView.circularTextView2
            lyt_parent = itemView.lyt_parentww
            recycler_view = itemView.recycler_attachments
            recycler_view.layoutManager = LinearLayoutManager(itemView.context)
            recycler_view.isNestedScrollingEnabled = false
        }
    }

}