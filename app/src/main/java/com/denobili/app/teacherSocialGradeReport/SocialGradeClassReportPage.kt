package com.denobili.app.teacherSocialGradeReport


import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.BaseListActivity
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.teacherAddClasses.ClassListEvent
import org.jetbrains.anko.AnkoLogger
import rx.Observer

class SocialGradeClassReportPage : BaseListActivity(), AnkoLogger {

    private var classid: String = ""

    private var adapter: ClassSocialGradeReportAdapter? = null
    private var presenter: ClassSocialGradePresenter? = null
    private var classname: String? = null
    private var progressdialog: ProgressDialog? = null


    companion object CLASSKEY {
        val classidKey = "com.classid.clasroom.join"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)
        initviews()
        adapter = ClassSocialGradeReportAdapter(null)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@SocialGradeClassReportPage)
        recyclerView.isNestedScrollingEnabled = false

        MainBus.getInstance().busObservable.ofType(ClassSocialGradeSummaryEvent::class.java).subscribe(eventobserver)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.class_report))

        if (intent.hasExtra(classidKey)) {
            classid = intent.getStringExtra(classidKey)
        }

        progressdialog = DialogUtil.showProgressDialog(this@SocialGradeClassReportPage)

        presenter = ClassSocialGradePresenter(this)
        getData()
    }

    override fun onRefresh() {
        super.onRefresh()
        swipeContainer.isRefreshing = true
        getData()
    }

    private val eventobserver = object : Observer<ClassSocialGradeSummaryEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event11: ClassSocialGradeSummaryEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event11.event) {
                Event.RESULT -> {
                    adapter!!.addItems(true, event11.results)
                    showResults()
                }
                Event.ERROR, Event.SERVER_ERROR, Event.NO_INTERNET -> {
                    if (event11.error != null) showError(event11.error)
                    else showError(getString(R.string.some_error))
                }
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
        if (NetworkHelper.isOnline(this)) {
            if (adapter!!.itemCount == 0) showLoading()
            presenter!!.getdata(classid)
        } else
            Emit(ClassListEvent(Event.NO_INTERNET))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}
