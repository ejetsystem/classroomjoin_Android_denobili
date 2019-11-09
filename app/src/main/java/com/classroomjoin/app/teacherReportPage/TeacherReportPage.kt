package com.classroomjoin.app.teacherReportPage

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.classroomjoin.app.R
import com.classroomjoin.app.classEditPage.AddClassModel
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper_utils.BaseListActivity
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.MainBus
import com.classroomjoin.app.teacherAddClasses.ClassListEvent
import com.classroomjoin.app.teacherAddClasses.ClassListPresenter
import rx.Observer

class TeacherReportPage : BaseListActivity() {


    private var classmodel: AddClassModel? = AddClassModel()
    private var adapter: TeacherReportClassAdapter? = null
    private var presenter: ClassListPresenter? = null
    private var classname: String? = null
    private var progressdialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)
        initviews()
        adapter = TeacherReportClassAdapter(null)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@TeacherReportPage)
        recyclerView.isNestedScrollingEnabled = false
        MainBus.getInstance().busObservable.ofType(ClassListEvent::class.java).subscribe(eventobserver)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.report))

        progressdialog = DialogUtil.showProgressDialog(this@TeacherReportPage)

        presenter = ClassListPresenter(this)
        getData()
    }

    override fun onRefresh() {
        super.onRefresh()
        swipeContainer.isRefreshing = true
        presenter?.refreshdata()
    }

    private val eventobserver = object : Observer<ClassListEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: ClassListEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            if (progressdialog!!.isShowing) progressdialog!!.hide()
            when (event.event) {
                Event.RESULT -> {
                    adapter!!.addItems(true, event.results)
                    showResults()
                }
                Event.NO_RESULT -> {
                    if (adapter!!.itemCount == 0) showNoResults()
                }
                Event.NO_INTERNET -> {
                    if (adapter!!.itemCount == 0)
                        showNoInternet()
                }
                Event.POST_SUCCESS -> onRefresh()
                Event.SERVER_ERROR -> if (adapter!!.itemCount == 0) showServerError()
                Event.ERROR -> if (adapter!!.itemCount == 0) showError(event.error)
                Event.POST_FAILURE -> showError(event.error)
                else -> {
                }
            }

        }
    }

    private fun showLoading() {
        imageView.visibility = View.GONE
        recyclerView.visibility = View.GONE
        textView.visibility = View.GONE
        contentLoadingProgressBar.visibility = View.VISIBLE
        contentLoadingProgressBar.show()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getData() {
        if (adapter!!.itemCount == 0) showLoading()
        presenter!!.getdata()

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}
