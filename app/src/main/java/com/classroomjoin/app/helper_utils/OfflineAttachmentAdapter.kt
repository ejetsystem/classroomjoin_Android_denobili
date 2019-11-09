package com.classroomjoin.app.helper_utils


import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import java.util.*


class OfflineAttachmentAdapter(var objects: List<DisplayItem>?=ArrayList<DisplayItem>(),
                               listener: OfflineAttachmentDelegate.AddAttachmentListener) : BaseRecyclerviewAdapter<DisplayItem>(objects) {

    private val delegatesManager: AdapterDelegatesManager<List<DisplayItem>>

    init {
        delegatesManager = AdapterDelegatesManager<List<DisplayItem>>()
        delegatesManager.addDelegate(OfflineAttachmentDelegate(listener))

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

    /*override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: MutableList<Any?>?) {
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
