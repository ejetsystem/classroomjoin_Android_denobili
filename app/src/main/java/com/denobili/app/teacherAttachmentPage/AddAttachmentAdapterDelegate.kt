package com.denobili.app.teacherAttachmentPage

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.denobili.app.R
import com.denobili.app.helper_utils.DisplayItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_add_attachment.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.onClick


class AddAttachmentAdapterDelegate(private var addAttachmentListener: AddAttachmentListener) : AdapterDelegate<List<DisplayItem>>(), AnkoLogger {


    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is AddAttachment
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_add_attachment, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as ViewHolder
        val item = displayItems[i] as AddAttachment


        holder.add.onClick {
            error { "clickeddd" }
            addAttachmentListener.onClicked()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val add: ImageView

        init {
            add = itemView.add_attachment
        }
    }

    interface AddAttachmentListener {
        fun onClicked()
    }


}