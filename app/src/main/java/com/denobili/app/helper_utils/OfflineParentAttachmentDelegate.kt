package com.denobili.app.helper_utils

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.denobili.app.R
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_attachement_list_grid.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick

class OfflineParentAttachmentDelegate(private var addAttachmentListener: AddAttachmentListener, contect: Context) : AdapterDelegate<List<DisplayItem>>(), AnkoLogger {

    var context: Context? = null
    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is OfflineAttachmentModel
    }

    init {
        context = contect

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_attachement_list_grid, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as ViewHolder
        val item = displayItems[i] as OfflineAttachmentModel


        println("Data--->" + "test--->" + item.att_type + "===" + displayItems.size)

        holder.thumb_type.imageResource = item.getThumbnail(item.att_type!!)

        holder.name.text = item.name

        //Picasso.with(context).load(item.file_url!!).into(holder.thumb_type)

        val picasso = Picasso.Builder(context)
                .listener(object : Picasso.Listener {
                    override fun onImageLoadFailed(picasso: Picasso, uri: Uri, exception: Exception) {
                        //Here your log
                        println("Data--->" + "onImageLoadFailed--->")
                        holder.thumb_type.imageResource = item.getThumbnail(item.att_type!!)


                    }
                })
                .build()
        picasso.load(item.file_url!!)
                .fit()
                .into(holder.thumb_type)

        //Picasso.with(context).load(item.file_url!!).placeholder(item.getThumbnail(item.att_type!!)).error(item.getThumbnail(item.att_type!!)).into(holder.thumb_type);

        /* if (item.att_type == 1  ) {

             println("Data--->" + "test11--->" + item.att_type)

         } else if(item.att_type == 2) {
             println("Data--->" + "test22--->" + item.att_type)

         }else{
             println("Data--->" + "test333--->" + item.att_type)

         }*/
        /*if (item.att_type == 1 || item.att_type == 3) {

            println("Data--->"+"test11--->"+item.att_type)

            holder.thumb_file_type2.visibility = View.GONE
            holder.thumb_file_type1.visibility = View.VISIBLE

            Picasso.with(context).load(item.file_url!!).into(holder.imageview_preview)

        } else {

            holder.thumb_type.imageResource = item.getThumbnail(item.att_type!!)

            holder.name.text = item.name

            println("Data--->"+"test22--->"+item.att_type)

            holder.thumb_file_type2.visibility = View.VISIBLE
            holder.thumb_file_type1.visibility = View.GONE

        }*/

        holder.itemView.onClick {
            addAttachmentListener.onClicked(item.file_url!!)
        }
    }

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         internal val name: TextView
         internal val thumb_type: ImageView
         /*internal val imageview_preview: ImageView
         internal val thumb_file_type2: LinearLayout
         internal val thumb_file_type1: FrameLayout*/


         init {
             name = itemView.id_attachement_name
             thumb_type = itemView.thumb_file_type
            /* imageview_preview = itemView.imageview_preview
             thumb_file_type2 = itemView.thumb_file_type2
             thumb_file_type1 = itemView.thumb_file_type1
*/

         }
     }


   /* class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val name: TextView
        internal val thumb_type: ImageView

        init {
            name = itemView.id_attachement_name
            thumb_type = itemView.thumb_file_type
        }
    }
*/
    interface AddAttachmentListener {
        fun onClicked(url: String)
    }


}