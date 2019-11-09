package com.classroomjoin.app.studentallEventsPage

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper_utils.BaseListActivity
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.MainBus
import com.classroomjoin.app.studentSearchPage.StudentSearchPage
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import rx.Observer

class AlleventsActivity : BaseListActivity(), AnkoLogger {

    private var adapter: StudentEventAdapter? = null
    private var presenter: All_events_presenter? = null
    private var progressdialog: ProgressDialog? = null
    private var type: String = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_main_ad)
        initviews()

        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver,
                IntentFilter("fcm_refresh"))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.allevents))

        adapter = StudentEventAdapter(null, this)
        recyclerView.adapter = adapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.layoutManager = LinearLayoutManager(this@AlleventsActivity)
        recyclerView.isNestedScrollingEnabled = false
        MainBus.getInstance().busObservable.ofType(AllStudentEvent::class.java).subscribe(eventobserver)
        presenter = All_events_presenter(this)
        progressdialog = DialogUtil.showProgressDialog(this@AlleventsActivity)

        // val adRequest = AdRequest.Builder().build()
        // adView.loadAd(adRequest)
    }

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            onRefresh()
        }
    }


    override fun onRefresh() {
        super.onRefresh()
        presenter?.refreshdata()
    }

    private val eventobserver = object : Observer<AllStudentEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: AllStudentEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.RESULT -> {
                    adapter!!.clear()
                    adapter!!.addItems(true, event.results)
                    showResults()
                }
                Event.NO_RESULT ->
                    showNoResults(getString(R.string.no_messages_parent))
                Event.NO_INTERNET -> {
                    if (adapter!!.itemCount == 0)
                        showNoInternet()
                }
                Event.SERVER_ERROR -> if (adapter!!.itemCount == 0) showServerError()
                Event.ERROR -> if (adapter!!.itemCount == 0) showError(event.error)
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
        Snackbar.make(recyclerView, R.string.noInternet, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // getData()
        onRefresh()
    }

    private fun getData() {
        showLoading()
        presenter!!.getdata(0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.action_bar_search) {
            startActivity<StudentSearchPage>()
        }
        return true
    }
}
