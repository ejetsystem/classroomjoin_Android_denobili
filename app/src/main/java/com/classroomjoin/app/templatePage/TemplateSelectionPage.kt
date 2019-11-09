package com.classroomjoin.app.templatePage

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.classroomjoin.app.R
import com.classroomjoin.app.addTemplatePage.AddTemplatePage
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper_utils.BaseListActivity
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.MainBus
import com.classroomjoin.app.helper_utils.ViewExtension.invisible
import com.classroomjoin.app.helper_utils.ViewExtension.visible
import com.classroomjoin.app.teacherHomePage.TemplateSelectionListener
import kotlinx.android.synthetic.main.activity_student_selection.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.image
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import rx.Observer

class TemplateSelectionPage : BaseListActivity(), AnkoLogger, TemplateSelectionListener {
    override fun onTemplateSelected(message: String, id: Int, type_id: Int) {
        startActivity(intentFor<AddTemplatePage>(AddTemplatePage.template_type_key to type_id,
                AddTemplatePage.template_edit_id to id))
    }

    private var adapter: TemplateSelectionAdapter? = null
    private var presenter: TemplateSelectionPresenter? = null
    private var progressdialog: ProgressDialog? = null
    private var type: Int = 2

    companion object TemplateSelectionPage {
        var TEMPLATE_TYPE_KEY: String = "com.classroom.templateselection"
    }

    private val tabIcons = intArrayOf(
            R.drawable.sms_tab_white,
            R.drawable.message_tab_white,
            R.drawable.mail_2_white,
            R.drawable.attendance_tab_icon
    )
    private val tabTexts = arrayOf(
            R.string.sms,
            R.string.message,
            R.string.mail,
            R.string.title_attendence)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_selection)
        initviews()

        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver,
                IntentFilter("temp_refresh"))

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setTitle(getString(R.string.choose_template))

        adapter = TemplateSelectionAdapter(null, this)
        recyclerView.adapter = adapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.layoutManager = LinearLayoutManager(this@TemplateSelectionPage)
        recyclerView.isNestedScrollingEnabled = false
        MainBus.getInstance().busObservable.ofType(TemplateSelectionEvent::class.java).subscribe(eventobserver)
        presenter = TemplateSelectionPresenter(this)
        progressdialog = DialogUtil.showProgressDialog(this@TemplateSelectionPage)

        if (intent.hasExtra(TEMPLATE_TYPE_KEY)) type = intent.getStringExtra(TEMPLATE_TYPE_KEY).toInt()
        counter_fab.visibility = View.VISIBLE
        counter_fab.image = AppCompatResources.getDrawable(this, R.drawable.ic_add_white_24dp)
        counter_fab.onClick {
            startActivity(intentFor<AddTemplatePage>(AddTemplatePage.template_type_key to type))
        }

        if (attendace_list_tabview.visibility == View.GONE) attendace_list_tabview.visibility = View.VISIBLE
        setupTabLayout()
    }

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            onRefresh()
        }
    }

    private fun setupTabLayout() {
        for (i in 0..3) {
            attendace_list_tabview.addTab(attendace_list_tabview.newTab().setText(tabTexts[i]).setIcon(tabIcons[i]))
        }
        attendace_list_tabview.addOnTabSelectedListener(tablistener)
        attendace_list_tabview.setTabTextColors(Color.parseColor("#BDBDBD"), Color.parseColor("#ffffff"))
    }

    private val tablistener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            when (tab!!.position) {
                0 -> {
                    type = 2
                    counter_fab.visible()
                }
                1 -> {
                    type = 3
                    counter_fab.visible()
                }
                2 -> {
                    type = 1
                    counter_fab.visible()
                }
                3 -> {
                    type = 4
                    counter_fab.invisible()
                }
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            when (tab!!.position) {
                0 -> {
                    type = 2
                    counter_fab.visible()
                }
                1 -> {
                    type = 3
                    counter_fab.visible()
                }
                2 -> {
                    type = 1
                    counter_fab.visible()
                }
                3 -> {
                    type = 4
                    counter_fab.invisible()
                }
            }
            getData()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        presenter?.refreshdata(type)
    }

    private val eventobserver = object : Observer<TemplateSelectionEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: TemplateSelectionEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            when (event.event) {
                Event.RESULT -> {
                    adapter!!.addItems(true, event.results)
                    showResults()
                }
                Event.NO_RESULT ->
                    showNoResults(getString(R.string.no_templates_added))
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
        presenter!!.getdata(type)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}
