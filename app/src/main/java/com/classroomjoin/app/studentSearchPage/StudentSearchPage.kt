package com.classroomjoin.app.studentSearchPage

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper_utils.BaseListActivity
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.studentInboxPage.StudentInboxAdapter
import com.classroomjoin.app.studentallEventsPage.StudentEventModel
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.app_bar_search_page.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import rx.Observer
import java.util.concurrent.TimeUnit

class StudentSearchPage : BaseListActivity(), AnkoLogger {

    private var adapter: StudentInboxAdapter? = null
    private var progressdialog: ProgressDialog? = null
    private var type: String = "1"
    var viewmodel: SearchViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_search_page)
        initviews()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.home))

        adapter = StudentInboxAdapter(null, this)
        recyclerView.adapter = adapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.layoutManager = LinearLayoutManager(this@StudentSearchPage)
        recyclerView.isNestedScrollingEnabled = false
        progressdialog = DialogUtil.showProgressDialog(this@StudentSearchPage)

        /*val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)*/


        viewmodel = SearchViewModel(this)
        RxTextView.textChanges(search_edit_text)
                .debounce(200, TimeUnit.MILLISECONDS)
                .skip(1)
                .filter { t: CharSequence? -> t?.trim()!!.length > 2 }
                .subscribe(searchObserver)
    }

    private val eventobserver = object : Observer<List<StudentEventModel>> {
        override fun onCompleted() {}
        override fun onError(e: Throwable) {
            error { "thrown error" }
            e.printStackTrace()
        }

        override fun onNext(t: List<StudentEventModel>?) {
            error { "this is just to check the response " + t?.size }
            if (t!!.isEmpty()) showNoResults()
            else {
                val list = ArrayList<DisplayItem>()
                list.addAll(t)
                adapter!!.addItems(true, list)
                showResults()
            }
        }
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

    private fun getData(query: String) {
        error { "this is just a check query= " + query }
        viewmodel!!.getData(query).subscribe(eventobserver)
    }


    override fun onRefresh() {
        super.onRefresh()
        swipeContainer.isRefreshing = false
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}