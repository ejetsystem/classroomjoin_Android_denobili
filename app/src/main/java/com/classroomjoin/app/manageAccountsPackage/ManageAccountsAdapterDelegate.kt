package com.classroomjoin.app.manageAccountsPackage

import agency.tango.android.avatarview.loader.PicassoLoader
import agency.tango.android.avatarview.views.AvatarView
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.classroomjoin.app.R
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.loginPage.Userdata
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.row_userdata.view.*


class ManageAccountsAdapterDelegate(context: Context) : AdapterDelegate<List<DisplayItem>>() {


    internal val androidColors: IntArray

    init {
        androidColors = context.getResources().getIntArray(R.array.androidcolors)
    }

    override fun isForViewType(displayItems: List<DisplayItem>, i: Int): Boolean {
        return displayItems[i] is Userdata
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_userdata, viewGroup, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(displayItems: List<DisplayItem>, i: Int, viewHolder: RecyclerView.ViewHolder, list: List<Any>) {
        val holder = viewHolder as StudentViewHolder
        val item = displayItems[i] as Userdata


        var user_Name:String?=null

        if (!item.firstname.isNullOrBlank())
            user_Name = item.firstname

        if (!item.middlename.isNullOrBlank())
            user_Name = user_Name + " " + item.middlename

        if (!item.lastname.isNullOrBlank())
            user_Name = user_Name + " " + item.lastname

        holder.username.text = user_Name

        if (!item.email.isNullOrEmpty())
            holder.user_mail.text = item.email
        else
            holder.user_mail.visibility = View.GONE

        PicassoLoader().loadImage(holder.user_image, item.profile_url, item.firstname)

        holder.user_type.text = item.getUserType()

    }

    internal class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val username: TextView
        internal val user_mail: TextView
        internal val user_image: AvatarView
        internal val user_type: TextView

        init {
            user_mail = itemView.row_userdata_user_email
            username = itemView.row_connected_student_name
            user_image = itemView.profile_image_new
            user_type = itemView.row_user_type
        }

    }

}