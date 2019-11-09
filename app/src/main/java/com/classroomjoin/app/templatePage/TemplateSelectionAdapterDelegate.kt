package com.classroomjoin.app.templatePage


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.classroomjoin.app.R
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.teacherHomePage.TemplateSelectionListener
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_template_selction.view.*
import org.jetbrains.anko.onClick


class TemplateSelectionAdapterDelegate(listener: TemplateSelectionListener) : AdapterDelegate<List<DisplayItem>>() {

    private var listener: TemplateSelectionListener

    init {
        this.listener = listener
    }

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is Template
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_template_selction, viewGroup, false)
        return EventTypeViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as EventTypeViewHolder
        val item = displayItems[i] as Template

        holder.template_message.text = item.message
        holder.template_title.text = item.subject

        holder.itemView.onClick {
            listener.onTemplateSelected(item.message, item.id, item.type_id)
        }

    }

    internal class EventTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val template_title: TextView
        internal val template_message: TextView

        init {

            template_title = itemView.template_title
            template_message = itemView.template_text
        }
    }

}