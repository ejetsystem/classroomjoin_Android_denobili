package com.classroomjoin.app.studentDiaryPage

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
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
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import rx.Observer

class StudentDiaryActivity : BaseListActivity(), AnkoLogger {

    private var adapter: StudentDiaryAdapter? = null
    private var presenter: Student_Diary_Presenter? = null
    private var progressdialog: ProgressDialog? = null
    private var type: String = "1"


    companion object TemplateSelectionPage {
        var TEMPLATE_TYPE_KEY: String = "com.classroom.templateselection"
        var TEMPLATE_NAME_KEY: String = "com.classroom.template"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_main_ad)
        initviews()

        if (intent.hasExtra(TEMPLATE_TYPE_KEY)) type = intent.getStringExtra(TEMPLATE_TYPE_KEY)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.home))

        adapter = StudentDiaryAdapter(null, this,type)
        recyclerView.adapter = adapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.layoutManager = LinearLayoutManager(this@StudentDiaryActivity)
        recyclerView.isNestedScrollingEnabled = false
        MainBus.getInstance().busObservable.ofType(StudentDiaryEvents::class.java).subscribe(eventobserver)
        presenter = Student_Diary_Presenter(this)
        progressdialog = DialogUtil.showProgressDialog(this@StudentDiaryActivity)

        if (intent.hasExtra(TEMPLATE_NAME_KEY)) supportActionBar?.title = intent.getStringExtra(TEMPLATE_NAME_KEY)


       /* val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)*/

        //For Refreshing the data here we are calling this method
       // onRefresh()

    }


    override fun onRefresh() {
        super.onRefresh()
        presenter?.refreshdata(type)
    }

    private val eventobserver = object : Observer<StudentDiaryEvents> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: StudentDiaryEvents) {
            error { event.toString() }

            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            when (event.event) {
                Event.RESULT -> {
                    System.out.println("Data--->" + "constructor--->" + event.resul!!.size)

                    adapter!!.addItems(true, event.resul!!)
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
        onRefresh()
    }


    private fun getData() {
        showLoading()
        presenter!!.getdata(type)
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
