package com.denobili.app.teacherCommunicationReports

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
import com.denobili.app.R
import com.denobili.app.ReportsMessageDetails
import com.denobili.app.helper.CircularTextView
import com.denobili.app.helper_utils.OfflineAttachmentModel
import com.denobili.app.helper_utils.OfflineParentAttachmentAdapter
import com.denobili.app.helper_utils.OfflineParentAttachmentDelegate
import kotlinx.android.synthetic.main.row_communication_report.view.*
import kotlinx.android.synthetic.main.row_student_events.view.*
import org.jetbrains.anko.image
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*

class CommunictionMessageAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder?>(), OfflineParentAttachmentDelegate.AddAttachmentListener  {
    private var product_list: MutableList<ReportModel>?
    private var isLoadingAdded = false
    val array_list: List<ReportModel>?
        get() = product_list
    private val androidColors: IntArray
    var context1: Context? = null

    init {
        context1 = context
        androidColors = context.resources.getIntArray(com.denobili.app.R.array.androidcolors)
    }
    fun setArray_list(array_list: MutableList<ReportModel>?) {
        product_list = array_list
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
        val v1 = inflater.inflate(R.layout.row_communication_report, parent, false)
        viewHolder = CustomViewHolder(v1)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = product_list!![position]
        when (getItemViewType(position)) {
            ITEM ->{var custViewHolder= holder as CustomViewHolder?
                if (item.student_name != null && !item.student_name.equals(""))
                    holder.student_name.text = item.student_name
                else
                    holder.student_name.text = context!!.getString(R.string.no_student)


                if (item.message != null)
                    holder.message.text = item.message
                else
                    holder.message.text = item.message1

                holder.date.text = getDateFormat(item.date)

                holder.circularTextView2_report.text = item.student_name


                val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
                holder.circularTextView2_report.solidColor = randomAndroidColor

                if (item.status != null && item.status.equals("success")) {

                    if (item.statusRead != null && item.statusRead == 1) {
                        holder.meassage_status.image = ContextCompat.getDrawable(context!!, R.mipmap.read_tick)
                    } else if (item.statusRead != null && item.statusRead == 0) {
                        holder.meassage_status.image = ContextCompat.getDrawable(context!!, R.mipmap.delivery_tick)
                    }
                } else {
                    holder.meassage_status.image = ContextCompat.getDrawable(context!!, R.mipmap.unsuuceess)

                }

                holder.lyt_parent_report.onClick {
                    context!!.startActivity(context!!.intentFor<ReportsMessageDetails>
                    (ReportsMessageDetails.eventdiarydetail.STUDENT_NAME to item.student_name,
                            ReportsMessageDetails.eventdiarydetail.CLASS_NAME to item.className,
                            ReportsMessageDetails.eventdiarydetail.MESSAGE to item.message,
                            ReportsMessageDetails.eventdiarydetail.MESSAGE1 to item.message1,
                            ReportsMessageDetails.eventdiarydetail.STATUS to item.statusRead.toString(),
                            ReportsMessageDetails.eventdiarydetail.DATE to item.date))
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
    private fun getDateFormat(date11: String?): String {
        var added_date = date11
        var spf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val newDate = spf.parse(added_date)

        spf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        added_date = spf.format(newDate)
        val date = spf.parse(added_date)
        val startDate = date.time

        return if (isToday(startDate)) {
            spf = SimpleDateFormat("HH:mm", Locale.getDefault())
            spf.format(newDate)
        } else if (isYesterday(startDate)) {
            "Yesterday"
        } else {
            spf = SimpleDateFormat("dd MMM", Locale.getDefault())
            added_date = spf.format(newDate)
            added_date
        }

        // return ""

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


    override fun getItemCount(): Int {
        return if (product_list == null) 0 else product_list!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == product_list!!.size - 1 && isLoadingAdded)
            LOADING else ITEM
    }

    fun add(mc: ReportModel) {
        product_list!!.add(mc)
        notifyItemInserted(product_list!!.size - 1)
    }

    fun addAll(mcList: List<ReportModel>) {
        for (mc in mcList) {
            add(mc)
        }
    }

    fun remove(city: ReportModel?) {
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
        add(ReportModel())
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

    fun getItem(position: Int): ReportModel {
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
        internal val date: TextView
        internal val message: TextView
        internal val student_name: TextView
        internal val circularTextView2_report: CircularTextView
        internal val meassage_status: ImageView
        internal val lyt_parent_report: LinearLayout


        init {
            date = itemView.event_date_report
            message = itemView.event_message_report
            student_name = itemView.student_name_report
            circularTextView2_report = itemView.circularTextView2_report
            meassage_status = itemView.meassage_status
            lyt_parent_report = itemView.lyt_parent_report

        }
       /* fun setDrawable(context: Context, resid: Int) {
            teacher_short.image = ContextCompat.getDrawable(context, resid)

        }*/

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