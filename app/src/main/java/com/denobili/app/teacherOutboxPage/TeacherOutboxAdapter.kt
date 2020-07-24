package com.denobili.app.teacherOutboxPage


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.denobili.app.helper_utils.BaseRecyclerviewAdapter
import com.denobili.app.helper_utils.DisplayItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import java.util.*


class TeacherOutboxAdapter(var objects: List<DisplayItem>?=ArrayList<DisplayItem>(), outboxSyncListener: OutboxSyncListener, context:Context) : BaseRecyclerviewAdapter<DisplayItem>(objects) {
    private val delegatesManager: AdapterDelegatesManager<List<DisplayItem>>

    init {
        delegatesManager = AdapterDelegatesManager<List<DisplayItem>>()
        delegatesManager.addDelegate(TeacherOutboxAdapterDelegate(outboxSyncListener,context))
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

    override fun additemtolist(position: Int, objects: MutableList<DisplayItem>?) {
        super.additemtolist(position, objects)
    }

    override fun removeall() {
        super.removeall()
    }

    override fun getItems(): MutableList<DisplayItem> {
        return super.getItems()
    }

    override fun clearList() {
        super.clearList()
    }

    override fun add(`object`: DisplayItem?) {
        super.add(`object`)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun getItem(position: Int): DisplayItem {
        return super.getItem(position)
    }



}
