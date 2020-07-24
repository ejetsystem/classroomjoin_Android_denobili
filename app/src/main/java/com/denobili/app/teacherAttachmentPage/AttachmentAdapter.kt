package com.denobili.app.teacherAttachmentPage


import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.denobili.app.helper_utils.BaseRecyclerviewAdapter
import com.denobili.app.helper_utils.DisplayItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import java.util.*


class AttachmentAdapter(var objects: List<DisplayItem>? = ArrayList<DisplayItem>(),
                        listener: AddAttachmentAdapterDelegate.AddAttachmentListener,
                        attachment_listener: AttachmentAdapterDelegate.AttachmentClickListener) : BaseRecyclerviewAdapter<DisplayItem>(objects) {

    private val delegatesManager: AdapterDelegatesManager<List<DisplayItem>>

    init {
        delegatesManager = AdapterDelegatesManager<List<DisplayItem>>()
        delegatesManager.addDelegate(AttachmentAdapterDelegate(attachment_listener))
        delegatesManager.addDelegate(AddAttachmentAdapterDelegate(listener))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(items, position, holder)
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(items, position)
    }

   /* override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: MutableList<Any?>?) {
        super.onBindViewHolder(holder, position, payloads)
    }*/


    override fun addItems(clear: Boolean, mObjects: List<DisplayItem>) {
        super.addItems(clear, mObjects)
    }

    override fun remove(position: Int) {
        super.remove(position)
    }

    override fun add(`object`: DisplayItem?) {
        super.add(`object`)
    }

    override fun additemat(position: Int, `object`: DisplayItem?) {
        super.additemat(position, `object`)
    }


}
