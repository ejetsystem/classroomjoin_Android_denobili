package com.denobili.app.studentDiaryPage

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper_utils.BaseListActivity
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.helper_utils.TouchDetectableScrollView
import com.denobili.app.studentSearchPage.StudentSearchPage
import com.denobili.app.studentallEventsPage.StudentEventModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import rx.Observer

class StudentDiaryActivity : BaseListActivity(), AnkoLogger {
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = true
    private var adapter: StudentDiaryAdapter? = null
    private var adapter1: StudentDiary1Adapter? = null
    private var presenter: Student_Diary_Presenter? = null
    private var progressdialog: ProgressDialog? = null
    private var type: String = "1"
    var modellist: MutableList<StudentDiaryEventModel>?=null
    var listing: List<StudentDiaryEventModel>?=null
    private var pastVisiblesItems = 0
    private var totalElement=0
    private val visibleThreshold = 5
    var firstVisibleItem:Int = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    private var TOTAL_PAGES = 0
    var curentPage=0


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
        adapter1 = StudentDiary1Adapter(this,type)
        recyclerView.adapter = adapter1
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var linear = LinearLayoutManager(this)
        recyclerView.layoutManager=linear
        recyclerView.isNestedScrollingEnabled = false
        MainBus.getInstance().busObservable.ofType(StudentDiaryEvents::class.java).subscribe(eventobserver)
        presenter = Student_Diary_Presenter(this)
        progressdialog = DialogUtil.showProgressDialog(this@StudentDiaryActivity)

        if (intent.hasExtra(TEMPLATE_NAME_KEY)) supportActionBar?.title = intent.getStringExtra(TEMPLATE_NAME_KEY)
        /*nestedScrollView.setOnScrollChangeListener(object: NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(nestedScrollView: NestedScrollView, scrollX:Int, scrollY:Int, oldScrollX:Int, oldScrollY:Int) {
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
                                presenter!!.refreshdata(type,curentPage)
                            }, 1000)
                        }

                        //Log.v("...", " Reached Last Item")
                    }



                    //  Log.e("ProductFragment", "down")
                }

            }
        }) // val adRequest = AdRequest.Builder().build()
*/
        nestedScrollView.setMyScrollChangeListener(object: TouchDetectableScrollView.OnMyScrollChangeListener{
            override fun onScrollUp() {
            }
            override fun onScrollDown() {
            }
            override fun onBottomReached() {
                if (curentPage<TOTAL_PAGES){
                    isLoading = false
                    curentPage++
                    Handler().postDelayed({
                        presenter!!.refreshdata(type,curentPage)
                    }, 1000)
                }else{

                }
               // Toast.makeText(this,"hello", Toast.LENGTH_LONG).show()
                // api call for pagination
            }
        })

       /* val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)*/

        //For Refreshing the data here we are calling this method
       // onRefresh()

    }


    override fun onRefresh() {
        super.onRefresh()
        presenter?.refreshdata(type,curentPage)
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
                    TOTAL_PAGES= event.totalPage1!!
                    totalElement=event.totalElement1!!
                    if (TOTAL_PAGES>=1){
                        TOTAL_PAGES=TOTAL_PAGES-1
                    }else{

                    }
                     listing= event.resul as List<StudentDiaryEventModel>
                    if (listing!!.size != 0 || listing != null) {
                        if (curentPage == 0) {
                            loadFirstPage(listing);
                        } else {
                            loadNextPage(listing);
                        }
                        showResults()
                        // adapter!!.addItems(true, event.resul!!)
                        //showResults()
                    }
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

    private fun loadFirstPage(listing: List<StudentDiaryEventModel>?) {
        try {
            modellist=null
            modellist = listing as MutableList<StudentDiaryEventModel>
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
    private fun loadNextPage(listing: List<StudentDiaryEventModel>?) {
        try {
            isLoading=true
            if (listing!!.size!=0||listing!=null){
                 modellist = listing as MutableList<StudentDiaryEventModel>
                adapter1!!.removeLoadingFooter();
                if (modellist!!.size<=totalElement-1){
                    adapter1!!.addAll(modellist!!)

                    if (curentPage < TOTAL_PAGES) adapter1!!.addLoadingFooter() else isLastPage = true
                }
                else{
                    adapter1!!.removeLoadingFooter()
                }

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
        onRefresh()
    }


    private fun getData() {
        showLoading()
        presenter!!.getdata(type,0)
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
