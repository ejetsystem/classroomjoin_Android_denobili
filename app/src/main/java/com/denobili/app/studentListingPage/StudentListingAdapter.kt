package com.denobili.app.studentListingPage


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.denobili.app.helper_utils.BaseRecyclerviewAdapter
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.StudentSelectionListener
import com.denobili.app.mystudentsPage.MyStudentModel
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import org.jetbrains.anko.AnkoLogger
import java.util.*


class StudentListingAdapter(var objects: List<DisplayItem>? = ArrayList<DisplayItem>(), context: Context, string: String?) :
        BaseRecyclerviewAdapter<DisplayItem>(objects),
        StudentSelectionListener, AnkoLogger {
    private val delegatesManager: AdapterDelegatesManager<List<DisplayItem>>

    init {
        delegatesManager = AdapterDelegatesManager<List<DisplayItem>>()
        delegatesManager.addDelegate(StudentSelectionAdapterDelegate(this, context, string))
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

    override fun addItems(clear: Boolean, mObjects: List<DisplayItem>) {
        super.addItems(clear, mObjects)
    }

    override fun selectedStudent(position: Int, isSelected: Boolean) {
        var student: MyStudentModel = getItem(position) as MyStudentModel
        student.isSelected = isSelected
        notifyItemChanged(position, student)
    }

}
