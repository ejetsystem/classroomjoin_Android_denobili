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
import com.denobili.app.helper_utils.OfflineAttachmentModel
import com.denobili.app.helper_utils.OfflineParentAttachmentAdapter
import com.denobili.app.helper_utils.OfflineParentAttachmentDelegate
import com.denobili.app.studentDiaryEventDetailPage.DiaryEventDetailActivity
import kotlinx.android.synthetic.main.row_inbox.view.*
import kotlinx.android.synthetic.main.row_student_events.view.event_date
import kotlinx.android.synthetic.main.row_student_events.view.event_message
import kotlinx.android.synthetic.main.row_student_events.view.event_teacher_name
import kotlinx.android.synthetic.main.row_student_events.view.recycler_attachments
import kotlinx.android.synthetic.main.row_student_events.view.teacher_name
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*

class StudentDiary1Adapter(private val context11: Context,event_type11:String) : RecyclerView.Adapter<RecyclerView.ViewHolder?>(), OfflineParentAttachmentDelegate.AddAttachmentListener  {
    private var product_list: MutableList<StudentDiaryEventModel>?
    private var isLoadingAdded = false
    internal val androidColors: IntArray
    var event_type:String?=null
    var context:Context?=null
    init {
        context=context11
        androidColors = context11.resources.getIntArray(R.array.androidcolors)
        event_type=event_type11
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> viewHolder = getViewHolder(parent, inflater)
            LOADING -> {
                val v2 = inflater.inflate(R.layout.item_loading, parent, false)
                viewHolder = Loading(v2)
            }
        }
        return viewHolder!!
    }

    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1 = inflater.inflate(R.layout.row_inbox, parent, false)
        viewHolder = CustomViewHolder(v1)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = product_list!![position]
        when (getItemViewType(position)) {
            ITEM ->{var custViewHolder= holder as CustomViewHolder?
                custViewHolder!!.teacher_name.text = item.teacher_name
                custViewHolder.event_date.text = getDateFormat(item.date)
                custViewHolder.event_message.text = item.message
                custViewHolder.event_subject.text = item.subject
                custViewHolder.teacher_short.text = item.teacher_name


                val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
                custViewHolder.teacher_short.solidColor = randomAndroidColor

                custViewHolder.lyt_parent.onClick {
                    context!!.startActivity(context!!.intentFor<DiaryEventDetailActivity>
                    (DiaryEventDetailActivity.eventdiarydetail.EVENT_ID_KEY to item.id, DiaryEventDetailActivity.eventdiarydetail.EVENT_TYPE_ID_KEY to event_type))
                }

                if (item.attachments != null) if (item.attachments!!.isNotEmpty())
                    initializeAdapter(custViewHolder, item.attachments!!,context!!)


                if (item.statusRead != null && item.statusRead == 0) {

                    custViewHolder.teacher_name.textColor = context!!.resources.getColor(R.color.titles_color)
                    custViewHolder.event_date.textColor = context!!.resources.getColor(R.color.material_gre)
                    custViewHolder.event_message.textColor = context!!.resources.getColor(R.color.material_gre)
                    custViewHolder.event_subject.textColor = context!!.resources.getColor(R.color.material_gre)
                    custViewHolder.teacher_short.alpha = 1f

                } else if (item.statusRead != null && item.statusRead == 1) {

                    custViewHolder.teacher_name.textColor = context!!.resources.getColor(R.color.grey_color)
                    custViewHolder.event_date.textColor = context!!.resources.getColor(R.color.grey_color)
                    custViewHolder.event_message.textColor = context!!.resources.getColor(R.color.grey_color)
                    custViewHolder.event_subject.textColor = context!!.resources.getColor(R.color.grey_color)
                    custViewHolder.teacher_short.alpha = 0.5f
                }


            }

            LOADING -> {

            }
        }
    }
    /* private fun initializeAdapter(holder: StudentEventAdapterDelegate.EventViewHolder, items: List<OfflineAttachmentModel>) {
         val attachmentAdapter = OfflineParentAttachmentAdapter(items, this,context!!)
         holder.recycler_view.adapter = attachmentAdapter
         holder.recycler_view.visibility = View.VISIBLE
 
     }*/
    private fun initializeAdapter(custViewHolder: CustomViewHolder, items: List<OfflineAttachmentModel>, context11: Context) {
        val attachmentAdapter = OfflineParentAttachmentAdapter(items, this,context11)
        custViewHolder.recycler_view.adapter = attachmentAdapter
        custViewHolder.recycler_view.visibility = View.VISIBLE }

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


    override fun getItemCount(): Int {
        return if (product_list == null) 0 else product_list!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == product_list!!.size - 1 && isLoadingAdded)
            LOADING else ITEM
    }

    fun add(mc: StudentDiaryEventModel) {
        product_list!!.add(mc)
        notifyItemInserted(product_list!!.size - 1)
    }

    fun addAll(mcList: List<StudentDiaryEventModel>) {
        for (mc in mcList) {
            add(mc)
        }
    }

    fun remove(city: StudentDiaryEventModel?) {
        val position = product_list!!.indexOf(city)
        if (position > -1) {
            product_list!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    val isEmpty: Boolean
        get() = itemCount == 0

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(StudentDiaryEventModel())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = product_list!!.size - 1
        val item = getItem(position)
        if (item != null) {
            product_list!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): StudentDiaryEventModel {
        return product_list!![position]
    }

    /*
   View Holders
   _________________________________________________________________________________________________
    */
    /**
     * Main list's content ViewHolder
     */
    protected inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

    protected inner class Loading(itemView: View?) : RecyclerView.ViewHolder(itemView)
    companion object {
        private const val ITEM = 0
        private const val LOADING = 1
    }

    init {
        product_list = ArrayList()
    }

    override fun onClicked(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context!!.startActivity(browserIntent)
    }


}