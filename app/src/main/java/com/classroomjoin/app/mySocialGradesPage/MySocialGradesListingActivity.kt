package com.classroomjoin.app.mySocialGradesPage

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.classroomjoin.app.R
import com.classroomjoin.app.addSocialGrade.AddSocialGradePage
import com.classroomjoin.app.helper_utils.BaseListActivity
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.MainBus
import kotlinx.android.synthetic.main.activity_student_selection.*
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import rx.Observer


class MySocialGradesListingActivity : BaseListActivity() {

    private var adapter: MySocialGradesAdapter? = null
    private var presenter: MySocialGradesPresenter? = null
    private var referencelist: List<DisplayItem>? = ArrayList<DisplayItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_selection)
        initviews()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        attendace_list_tabview.addTab(attendace_list_tabview.newTab().setText(R.string.positive_social_grade).setIcon(R.drawable.ic_thumb_up_green_24dp))
        attendace_list_tabview.addTab(attendace_list_tabview.newTab().setText(R.string.negative_social_grade).setIcon(R.drawable.ic_thumb_down_green_24dp))
        attendace_list_tabview.addOnTabSelectedListener(listener)
        attendace_list_tabview.setTabTextColors(Color.parseColor("#BDBDBD"), Color.parseColor("#ffffff"))

        adapter = MySocialGradesAdapter(null)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        MainBus.getInstance().busObservable.ofType(MySocialGradesEvent::class.java).subscribe(eventobserver)
        presenter = MySocialGradesPresenter(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.social_grade_listing))

        counter_fab.imageResource = R.drawable.ic_add_white_24dp

        counter_fab.onClick {
            startActivity(intentFor<AddSocialGradePage>(AddSocialGradePage.IS_IN_EDIT_MODEKEY to true))
        }

    }

    private val listener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            adapter?.items?.forEachWithIndex { i, displayItem ->
                val grade: MySocialGradesModel = displayItem as MySocialGradesModel
                grade.isselected = false
                adapter?.notifyItemChanged(i, grade)
            }
            when (tab!!.position) {
                0 -> filter(true)
                1 -> filter(false)
            }
        }
    }

    private fun filter(ispositive: Boolean) {
        var type = 0
        if (ispositive) type = 1
        var list = ArrayList<DisplayItem>()
        adapter!!.addItems(true, referencelist!!)
        adapter?.items?.forEachWithIndex { i, displayItem ->
            val grade: MySocialGradesModel = displayItem as MySocialGradesModel
            if (grade.flag == type) {
                list.add(grade)
            }
        }
        adapter!!.addItems(true, list)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }


    override fun onRefresh() {
        swipeContainer.isRefreshing = false
        super.onRefresh()
        presenter?.refreshdata()
    }

    private val eventobserver = object : Observer<MySocialGradesEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: MySocialGradesEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            when (event.event) {
                Event.RESULT -> {
                    attendace_list_tabview.visibility = View.VISIBLE
                    referencelist = event.results
                    adapter!!.addItems(true, event.results)
                    if (attendace_list_tabview.getTabAt(0)!!.isSelected) filter(true)
                    else filter(false)
                    showResults()
                }
                Event.NO_RESULT -> {
                    if (adapter!!.itemCount == 0)
                        showNoResults(getString(R.string.no_social_added))
                }

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
        getData()
    }

    private fun getData() {
        showLoading()
        presenter!!.getdata()
    }

}
