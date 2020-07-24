package com.denobili.app.studentInboxPage

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
import com.denobili.app.studentallEventsPage.AllStudentEvent
import com.denobili.app.studentallEventsPage.All_events_presenter
import com.denobili.app.studentallEventsPage.StudentEventModel
import com.denobili.app.teacherCommunicationReports.CommunictionMessageAdapter
import org.jetbrains.anko.startActivity
import rx.Observer

class StudentInboxPage : BaseListActivity() {
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = true
    private var adapter: StudentInboxAdapter?=null
    private var adapter1: CommunictionInboxAdapter? = null
    private var presenter: StudentInboxEventPresenter?= null
    private var presenter1: All_events_presenter? = null
    private var progressdialog: ProgressDialog?=null
    private var type:String="3"
    private var pastVisiblesItems = 0
    private val visibleThreshold = 5
    var firstVisibleItem:Int = 0
    var visibleItemCount:Int = 0
    private var totalElement=0
    var totalItemCount:Int = 0
    private var TOTAL_PAGES = 0
    var curentPage=0
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
        adapter1 = CommunictionInboxAdapter(this)
        adapter= StudentInboxAdapter(null,this)



        if (type.equals("6")){
            recyclerView.adapter=adapter
            var linear = LinearLayoutManager(this)
            recyclerView.layoutManager=linear
            recyclerView.isNestedScrollingEnabled = false
            MainBus.getInstance().busObservable.ofType(AllStudentEvent::class.java).subscribe(eventobserver1)
            presenter1 = All_events_presenter(this)

        }else{
            recyclerView.adapter=adapter1
            var linear = LinearLayoutManager(this)
            recyclerView.layoutManager=linear
            recyclerView.isNestedScrollingEnabled = false
            MainBus.getInstance().busObservable.ofType(StudentInboxEvent::class.java).subscribe(eventobserver)
            presenter = StudentInboxEventPresenter(this,type)
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
                            if (curentPage<TOTAL_PAGES){
                                isLoading = false
                                curentPage++
                                Handler().postDelayed({
                                    presenter!!.refreshdata(curentPage)
                                }, 1000)
                            }else{

                            }

                            //Log.v("...", " Reached Last Item")
                        }



                        //  Log.e("ProductFragment", "down")
                    }

                }
            })*/
            //comment
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
                            presenter!!.refreshdata(curentPage)
                        }, 1000)
                    }else{

                    }
                   // Toast.makeText(this@StudentInboxPage,"hello",Toast.LENGTH_LONG).show()
                    // api call for pagination
                }
            })
        }
        progressdialog= DialogUtil.showProgressDialog(this@StudentInboxPage)
       // val adRequest = AdRequest.Builder().build()

        /*val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)*/
    }

    override fun onRefresh() {
        super.onRefresh()
        if(type.equals("6")){
            curentPage==0
            presenter1?.refreshdata(type.toInt(),200)
        }else{
            curentPage==0
            presenter?.refreshdata(curentPage)
        }

    }
    private val eventobserver1 = object : Observer<AllStudentEvent> {
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
    private val eventobserver = object : Observer<StudentInboxEvent> {
        override fun onCompleted() {}
        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: StudentInboxEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            when (event.event) {
                Event.RESULT -> {
                    totalElement=event.totalElements!!
                    TOTAL_PAGES= event.totalPage!!
                    if (TOTAL_PAGES>=1){
                        TOTAL_PAGES=TOTAL_PAGES-1
                    }else{

                    }
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
    private fun loadFirstPage(listing: List<StudentEventModel>?) {
        try {
            var modellist: MutableList<StudentEventModel> = listing as MutableList<StudentEventModel>
            if (modellist.size!=null&&curentPage==0){
                adapter1!!.clear()
                isLoading = true
                adapter1!!.addAll(modellist)
                if (curentPage !== TOTAL_PAGES)
                    adapter1!!.addLoadingFooter()
                else isLastPage = true
            }else{
                isLoading = true
                adapter1!!.addAll(modellist)
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
            if (listing!!.size!=0||listing!=null){
                val modellist: List<StudentEventModel> =listing!!
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
        presenter!!.getdata(3,curentPage)
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
