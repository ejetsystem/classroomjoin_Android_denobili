package com.classroomjoin.app.teacherCommunicationReports

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.classroomjoin.app.R
import com.classroomjoin.app.classEditPage.AddClassModel
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.BaseListActivity
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.MainBus
import com.jakewharton.rxbinding.widget.RxSearchView
import org.jetbrains.anko.AnkoLogger
import rx.Observer
import java.util.concurrent.TimeUnit


class TeacherCommunicationReportPage : BaseListActivity(), AnkoLogger {


    private var classmodel: AddClassModel? = AddClassModel()
    private var adapter: TeacherCommunicationReportAdapter? = null
    private var presenter: CommunicationReportPresenter? = null
    private var classname: String? = null
    private var progressdialog: ProgressDialog? = null
    private var type = "2"
    private var classid = ""

    companion object TYPE {
        val communication_type_key = "com.setting.Communication_type"
        val communication_type_name = "com.setting.communication_name"
        val classidkey = "com.setting.classid"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)
        initviews()
        adapter = TeacherCommunicationReportAdapter(null,this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@TeacherCommunicationReportPage)
        recyclerView.isNestedScrollingEnabled = false
        progressdialog = DialogUtil.showProgressDialog(this@TeacherCommunicationReportPage)

        MainBus.getInstance().busObservable.ofType(CommunicationReportEvent::class.java).subscribe(eventobserver)
        presenter = CommunicationReportPresenter(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.home))

        if (intent.hasExtra(communication_type_key))
            type = intent.getStringExtra(communication_type_key)

        if (intent.hasExtra(communication_type_name))
            supportActionBar?.title = intent.getStringExtra(communication_type_name)

        if (intent.hasExtra(classidkey)) {
            classid = intent.getStringExtra(classidkey)
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        swipeContainer.isRefreshing = true
        refreshdata()
    }

    private val eventobserver = object : Observer<CommunicationReportEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: CommunicationReportEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
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
        showLoading()
        presenter?.getdata(type, classid)
    }

    private fun refreshdata() {
        if (NetworkHelper.isOnline(this)) {
            if (adapter!!.itemCount == 0) showLoading()
            presenter!!.refreshData(type, classid)
        } else
            Emit(CommunicationReportEvent(Event.NO_INTERNET))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu_reports, menu)
        val searchView = menu!!.findItem(R.id.action_bar_search).actionView as android.widget.SearchView
        RxSearchView.queryTextChanges(searchView)
                .debounce(200, TimeUnit.MILLISECONDS)
                .skip(1)
                .filter { t: CharSequence? -> t?.trim()!!.length > 1 }
                .subscribe(searchObserver)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.action_bar_search) {
        }
        return true
    }


    private val searchObserver = object : Observer<CharSequence> {
        override fun onCompleted() {}
        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(t: CharSequence?) {
            getData(t.toString())
        }
    }

    private fun getData(toString: String) {
        presenter!!.getdata(type, classid, toString)
    }


}
