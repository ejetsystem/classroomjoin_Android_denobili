package com.denobili.app.socialGradeSelectionPage

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.ItemClickSupport
import com.denobili.app.helper_utils.BaseListActivity
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.mySocialGradesPage.MySocialGradesModel
import com.denobili.app.teacherMessageSendPage.TeacherCommunicationPage
import com.denobili.app.teacherOutboxPage.Outboxpage
import kotlinx.android.synthetic.main.activity_student_selection.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*

class SocialGradeSelectionPage : BaseListActivity(), AnkoLogger, SocialListner {


    companion object SocialGradeSelectionPage {
        var STUDENTSKEY: String = "com.classroomandroid.socialgradeselection"
        val CLASSIDKEY: String = "com.students.class"

    }

    private var adapter: SocialGradesSelectionAdapter? = null
    private var presenter: SocialGradesSelectionPresenter? = null
    private var displaylist: ArrayList<DisplayItem>? = ArrayList<DisplayItem>()
    private var referencelist: List<DisplayItem>? = ArrayList<DisplayItem>()
    private var progressdialog: ProgressDialog? = null
    var student_list: ArrayList<String>? = ArrayList<String>()
    private var select_all: MenuItem? = null
    private var deSelectall: MenuItem? = null
    private var classid: String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_selection)
        initviews()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        attendace_list_tabview.addTab(attendace_list_tabview.newTab().setText(R.string.positive_social_grade).setIcon(R.drawable.ic_thumb_up_green_24dp))
        attendace_list_tabview.addTab(attendace_list_tabview.newTab().setText(R.string.negative_social_grade).setIcon(R.drawable.ic_thumb_down_green_24dp))
        MainBus.getInstance().busObservable.ofType(SocialGradeSelectionEvent::class.java).subscribe(eventobserver)
        presenter = SocialGradesSelectionPresenter(this)
        getData()
        attendace_list_tabview.addOnTabSelectedListener(listener)
        attendace_list_tabview.setTabTextColors(Color.parseColor("#BDBDBD"), Color.parseColor("#ffffff"))

        adapter = SocialGradesSelectionAdapter(this, null)
        recyclerView.adapter = adapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.title_send_social_grade))

        recyclerView.layoutManager = LinearLayoutManager(this@SocialGradeSelectionPage)
        recyclerView.isNestedScrollingEnabled = false


        progressdialog = DialogUtil.showProgressDialog(this@SocialGradeSelectionPage)

        if (intent.hasExtra(STUDENTSKEY)) {
            student_list = intent.getStringArrayListExtra(STUDENTSKEY)
        }
        if (intent.hasExtra(TeacherCommunicationPage.CLASSIDKEY)) {
            classid = intent.getStringExtra(TeacherCommunicationPage.CLASSIDKEY)
        }

        counter_fab.imageResource = R.drawable.ic_send_white_24dp

        counter_fab.onClick {
            if (getSelecteditems().isEmpty()) showSnackbar(getString(R.string.select_social_grade))
            else sendSocialgrades()
        }

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener { _, position, _ ->
            var socialGradesModel: MySocialGradesModel = adapter?.getItem(position) as MySocialGradesModel
            socialGradesModel.isselected = !socialGradesModel.isselected
            adapter?.notifyItemChanged(position, socialGradesModel)
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


    private fun sendSocialgrades() {
        progressdialog!!.show()
        presenter?.postdata(getModel())
    }

    private fun getModel(): SocialGradeSendModel {
        return SocialGradeSendModel(null, null, getSelecteditems(), student_list!!, classid, getCurrentDate())
    }

    private fun getSelecteditems(): List<String> {
        val list: ArrayList<String>? = ArrayList<String>()
        adapter?.items?.forEachWithIndex { i, displayItem ->
            val grade: MySocialGradesModel = displayItem as MySocialGradesModel
            if (grade.isselected) {
                list?.add(grade.id.toString())
            }
        }
        return list!!
    }

    override fun selected(position: Int, isSelected: Boolean) {
        var student: MySocialGradesModel = adapter!!.getItem(position) as MySocialGradesModel
        student.isselected = isSelected
        adapter?.notifyItemChanged(position, student)
    }


    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

    override fun onRefresh() {
        swipeContainer.isRefreshing = false
        super.onRefresh()
    }

    private val eventobserver = object : Observer<SocialGradeSelectionEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: SocialGradeSelectionEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.RESULT -> {
                    attendace_list_tabview.visibility = View.VISIBLE
                    attendace_list_tabview.getTabAt(0)!!.select()
                    referencelist = event.results
                    adapter!!.addItems(true, referencelist!!)
                    showResults()
                    filter(true)
                }
                Event.NO_RESULT -> {
                    // showNoResults()
                    showError(getString(R.string.pls_add_social_grade))
                }
                Event.NO_INTERNET -> {
                    if (adapter!!.itemCount == 0)
                        showNoInternet()
                }
                Event.SERVER_ERROR -> if (adapter!!.itemCount == 0)
                    showServerError()
                Event.ERROR -> if (adapter!!.itemCount == 0) showError(event.error)
                Event.POST_FAILURE -> showerrorDialog(event.error)
                Event.POST_SUCCESS -> showSuccessDialog()
                Event.SAVED_TO_OUTBOX -> {
                    Snackbar.make(recyclerView, R.string.saved_to_outbox, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_outbox, snack_listener)
                            .show()
                }
                else -> {
                }
            }
        }
    }

    private val snack_listener = object : View.OnClickListener {
        override fun onClick(p0: View?) {
            startActivity<Outboxpage>()
        }

    }


    private fun showerrorDialog(message: String?) {
        var alert = AlertDialog.Builder(this@SocialGradeSelectionPage)
        alert.setTitle(R.string.error)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_retry) { dialog, whichButton ->
            sendSocialgrades()
            dialog.dismiss()
        }
        alert.setNegativeButton(R.string.btn_cancel) { dialog, whichButton ->
            dialog.dismiss()
        }
        alert.show()
    }

    private fun showSuccessDialog() {
        var alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.success)
        alert.setMessage(R.string.success_send_social_grade)
        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->
            dialog.dismiss()
            finish()
        }
        alert.show()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }

        return true
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
