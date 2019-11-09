package com.classroomjoin.app.helper_utils

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.classroomjoin.app.R
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_attachement_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick


class OfflineAttachmentDelegate(private var addAttachmentListener: AddAttachmentListener) : AdapterDelegate<List<DisplayItem>>(),AnkoLogger{


    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is OfflineAttachmentModel
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(viewGroup.context).inflate(R.layout.row_attachement_list,viewGroup,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as ViewHolder
        val item =displayItems[i] as OfflineAttachmentModel



        holder.thumb_type.imageResource=item.getThumbnail(item.att_type!!)



        holder.name.text=item.name

        holder.itemView.onClick {
            addAttachmentListener.onClicked(item.file_url!!)
        }
    }

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         internal val name:TextView
         internal val thumb_type:ImageView

         init {
            name=itemView.id_attachement_name
             thumb_type=itemView.thumb_file_type
        }
    }

    interface AddAttachmentListener{
        fun onClicked(url:String)
    }




}