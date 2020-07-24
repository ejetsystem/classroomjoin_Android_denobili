package com.denobili.app.teacherAttachmentPage

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.denobili.app.R
import com.denobili.app.helper_utils.DisplayItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_attachement.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.onClick


class AttachmentAdapterDelegate(private val attachmentClickListener: AttachmentClickListener) : AdapterDelegate<List<DisplayItem>>(), AnkoLogger {

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is Attachment
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_attachement, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as ViewHolder
        val item = displayItems[i] as Attachment

        holder.name.text = item.name

        holder.remove.onClick {
            attachmentClickListener.onItemRemove(i)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val name: TextView
        internal val remove: ImageButton

        init {
            name = itemView.id_attachement_name
            remove = itemView.id_attachement_remove
        }
    }

    interface AttachmentClickListener {
        fun onItemRemove(position: Int)
    }


}