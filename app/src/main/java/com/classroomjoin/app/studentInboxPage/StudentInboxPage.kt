package com.classroomjoin.app.studentInboxPage

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
import org.jetbrains.anko.startActivity
import rx.Observer

class StudentInboxPage : BaseListActivity() {

    private var adapter: StudentInboxAdapter?=null
    private var presenter: StudentInboxEventPresenter?= null
    private var progressdialog: ProgressDialog?=null
    private var type:String="3"

    companion object TemplateSelectionPage {
        var TEMPLATE_TYPE_KEY: String = "com.classroom.type"
        var TEMPLATE_NAME_KEY: String = "com.classroom.type_name"


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_main_ad)
        initviews()

        if (intent.hasExtra(StudentInboxPage.TEMPLATE_TYPE_KEY)) type = intent.getStringExtra(StudentInboxPage.TEMPLATE_TYPE_KEY)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
       // supportActionBar?.setTitle(getString(R.string.inbox))
        if (intent.hasExtra(StudentInboxPage.TEMPLATE_NAME_KEY)) supportActionBar?.title = intent.getStringExtra(StudentInboxPage.TEMPLATE_NAME_KEY)

        adapter= StudentInboxAdapter(null,this)
        recyclerView.adapter=adapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.layoutManager= LinearLayoutManager(this@StudentInboxPage)
        recyclerView.isNestedScrollingEnabled = false
        MainBus.getInstance().busObservable.ofType(StudentInboxEvent::class.java).subscribe(eventobserver)
        presenter = StudentInboxEventPresenter(this,type)
        progressdialog= DialogUtil.showProgressDialog(this@StudentInboxPage)

        /*val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)*/
    }

    override fun onRefresh() {
        super.onRefresh()
        presenter?.refreshdata()
    }

    private val eventobserver = object : Observer<StudentInboxEvent> {
        override fun onCompleted() {}
        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: StudentInboxEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            when (event.event) {
                Event.RESULT -> {
                    adapter!!.addItems(true, event.results)
                    showResults()
                }
                Event.NO_RESULT->
                    showNoResults(getString(R.string.no_messages_parent))
                Event.NO_INTERNET -> {
                    if (adapter!!.itemCount == 0)
                        showNoInternet()
                }
                Event.SERVER_ERROR -> if (adapter!!.itemCount == 0) showServerError()
                Event.ERROR -> if (adapter!!.itemCount == 0) showError(event.error)
                else ->{
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
        //getData()

        onRefresh()
    }

    private fun getData() {
        showLoading()
        presenter!!.getdata(3)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId==android.R.id.home){
            finish()
        }else if(item.itemId==R.id.action_bar_search){
            startActivity<StudentSearchPage>()
        }
        return true
    }
}
