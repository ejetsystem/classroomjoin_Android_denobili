package com.denobili.app.teacherOutboxPage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.denobili.app.R
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.OfflineAttachmentAdapter
import com.denobili.app.helper_utils.OfflineAttachmentDelegate
import com.denobili.app.helper_utils.OfflineAttachmentModel
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_outbox.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick


class TeacherOutboxAdapterDelegate(outboxSyncListener: OutboxSyncListener,private var context:Context) : AdapterDelegate<List<DisplayItem>>(), OfflineAttachmentDelegate.AddAttachmentListener {

    override fun onClicked(url:String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    private val listener:OutboxSyncListener

    init {
        listener=outboxSyncListener
    }


    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is OutboxModel
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(viewGroup.context).inflate(R.layout.row_outbox,viewGroup,false)
        return EventTypeViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as EventTypeViewHolder
        val item =displayItems[i] as OutboxModel

        holder.date.setText(item.shown_date)
        holder.message.setText(item.message)

        holder.sync.onClick {
            listener.onSyncStateChanged(item,i)
        }


        if(item.sync_state==0){
            holder.sync.visibility=View.VISIBLE
            holder.sync_indicator.visibility=View.GONE
        }else{
            holder.sync.visibility=View.GONE
            holder.sync_indicator.visibility=View.VISIBLE
        }

        when(item.feature){
            1->holder.type.imageResource=R.drawable.circle_sms
            2->holder.type.imageResource=R.drawable.circle_mail
            3->{
                when(item.diary_event_id){
                    1->holder.type.imageResource=R.drawable.circle_diary
                    2->holder.type.imageResource=R.drawable.circle_assignment
                    3->holder.type.imageResource=R.drawable.circle_notice
                    4->holder.type.imageResource=R.drawable.circle_events
                }
            }
            4->holder.type.imageResource=R.drawable.circle_grade
            5->holder.type.imageResource=R.drawable.circle_message
            6->holder.type.imageResource=R.drawable.circle_attendance

        }

        if(item.attachment_id!=0)initializeAdapter(holder,item.getAttachments())
    }

    private fun initializeAdapter(holder: EventTypeViewHolder,items:List<OfflineAttachmentModel>){
        val attachmentAdapter= OfflineAttachmentAdapter(items, this)
        holder.recycler_view.adapter=attachmentAdapter
        holder.recycler_view.visibility=View.VISIBLE

    }

    internal class EventTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        internal val date:TextView
        internal val message:TextView
        internal val type:ImageView
        internal val sync:ImageButton
        internal val sync_indicator:ContentLoadingProgressBar
        internal val recycler_view:RecyclerView
        init {

            date=itemView.outbox_date
            message=itemView.outboxmessage
            type=itemView.outbox_type
            sync=itemView.sync_button
            sync_indicator=itemView.contentLoading_sync
            recycler_view=itemView.recycler_attachments
            recycler_view.layoutManager= LinearLayoutManager(itemView.context,LinearLayoutManager.HORIZONTAL,false)
            recycler_view.isNestedScrollingEnabled = false

        }
    }

}