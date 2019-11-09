package com.classroomjoin.app.studentallEventsPage


import com.classroomjoin.app.helper_utils.BaseRecyclerviewAdapter
import com.classroomjoin.app.helper_utils.DisplayItem


class StudentEventAdapter(var objects: List<DisplayItem>?= java.util.ArrayList<DisplayItem>(), context: android.content.Context) : BaseRecyclerviewAdapter<DisplayItem>(objects) {
    private val delegatesManager: com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager<List<DisplayItem>>

    init {
        delegatesManager = com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager<List<DisplayItem>>()
        delegatesManager.addDelegate(StudentEventAdapterDelegate(context))
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): android.support.v7.widget.RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: android.support.v7.widget.RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(items, position, holder)
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(items, position)
    }

    override fun addItems(clear: Boolean, mObjects: List<DisplayItem>?) {
        super.addItems(clear, mObjects)
    }

}
