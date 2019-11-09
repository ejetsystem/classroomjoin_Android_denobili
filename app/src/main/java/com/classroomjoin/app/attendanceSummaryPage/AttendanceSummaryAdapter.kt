package com.classroomjoin.app.attendanceSummaryPage


import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.classroomjoin.app.helper_utils.BaseRecyclerviewAdapter
import com.classroomjoin.app.helper_utils.DisplayItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import java.util.*


class AttendanceSummaryAdapter(var objects: List<DisplayItem>? = ArrayList(), val classid: String) : BaseRecyclerviewAdapter<DisplayItem>(objects) {
    private val delegatesManager: AdapterDelegatesManager<List<DisplayItem>> = AdapterDelegatesManager()

    init {
        delegatesManager.addDelegate(AttendanceSummaryAdapterDelegate(classid))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegatesManager.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(items, position, holder)
    }

    override fun getItemViewType(position: Int): Int =
            delegatesManager.getItemViewType(items, position)

}
