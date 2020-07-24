package com.denobili.app.studentallEventsPage

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper_utils.*
import com.denobili.app.studentSearchPage.StudentSearchPage
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import rx.Observer


class AlleventsActivity : BaseListActivity(), AnkoLogger {
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = true
    private var adapter: StudentEventAdapter? = null
    private var adapter1: ProductLevel1Adapter? = null
    private var presenter: All_events_presenter? = null
    private var progressdialog: ProgressDialog? = null
    private var type: String = "1"
    var modellist: List<StudentEventModel>?=null
    private var pastVisiblesItems = 0
    private val visibleThreshold = 5
    var firstVisibleItem:Int = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    private val TOTAL_PAGES = 2
    var curentPage=0

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
        adapter1=ProductLevel1Adapter(this)
        adapter = StudentEventAdapter(null, this)
        recyclerView.adapter = adapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var linear = LinearLayoutManager(this@AlleventsActivity)
        recyclerView.layoutManager=linear
        recyclerView.isNestedScrollingEnabled = false

        MainBus.getInstance().busObservable.ofType(AllStudentEvent::class.java).subscribe(eventobserver)
        presenter = All_events_presenter(this)
        progressdialog = DialogUtil.showProgressDialog(this@AlleventsActivity)

/*
        nestedScrollView.setOnScrollChangeListener(object: NestedScrollView.OnScrollChangeListener {
           override fun onScrollChange(nestedScrollView:NestedScrollView, scrollX:Int, scrollY:Int, oldScrollX:Int, oldScrollY:Int) {
               // Log.e("ProductFragment", "position button " + topPositionButton + " scrollY " + scrollY)
                if (scrollY > 0)
                {
                    visibleItemCount = linear.getChildCount()
                    totalItemCount = linear.getItemCount()
                    pastVisiblesItems = linear.findFirstVisibleItemPosition()
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                    {
                        if (curentPage==TOTAL_PAGES){

                        }else{
                            isLoading = false
                            curentPage++
                            Handler().postDelayed({
                                presenter!!.refreshdata(0,curentPage)
                            }, 1000)
                        }

                        //Log.v("...", " Reached Last Item")
                    }



                  //  Log.e("ProductFragment", "down")
                }

            }
        }) // val adRequest = AdRequest.Builder().build()
*/
        // adView.loadAd(adRequest)
    }

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            onRefresh()
        }
    }


    override fun onRefresh() {
        super.onRefresh()
        curentPage=0
        presenter?.refreshdata(0,curentPage)
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
                    /*if(event.results!!.size==0||event!!.results!!.size==null){

                    }else{
                        val listing:List<StudentEventModel>?=event.results as List<StudentEventModel>
                        if (listing!!.size!=0||listing!=null){
                            if(curentPage==0) {
                                loadFirstPage(listing);
                            }else{
                                loadNextPage(listing);
                            }
                            showResults()
                        }else{

                        }

                    }*/
                    adapter!!.clear()
                    adapter!!.addItems(true, event.results)
                    //adapter!!.notifyDataSetChanged()
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
    private fun loadFirstPage(listing: List<StudentEventModel>?) {
        try {
            modellist=null
            modellist = listing as MutableList<StudentEventModel>
            if (modellist!!.size!=null&&curentPage==0){
                adapter1!!.clear()
                isLoading = true
                adapter1!!.addAll(modellist!!)
                if (curentPage !== TOTAL_PAGES)
                    adapter1!!.addLoadingFooter()
                else isLastPage = true
            }else{
                isLoading = true
                adapter1!!.addAll(modellist!!)
                if (curentPage !== TOTAL_PAGES)
                    adapter1!!.addLoadingFooter()
                else isLastPage = true
            }



        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    private fun loadNextPage(listing: List<StudentEventModel>?) {
        try {
            isLoading=true
            modellist=null
            if (listing!!.size!=0||listing!=null){
                modellist =listing!!
                adapter1!!.removeLoadingFooter();
                adapter1!!.addAll(modellist!!)
                adapter1!!.notifyDataSetChanged();
                if (curentPage < TOTAL_PAGES) adapter1!!.addLoadingFooter() else isLastPage = true
            }
            else{

            }

        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
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
        presenter!!.getdata(0,0)
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
