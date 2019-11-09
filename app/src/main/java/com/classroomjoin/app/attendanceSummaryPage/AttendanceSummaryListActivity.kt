package com.classroomjoin.app.attendanceSummaryPage

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.BaseListActivity
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.MainBus
import com.classroomjoin.app.monthPickerLibrary.RackMonthPicker
import kotlinx.android.synthetic.main.activity_student_social_grade_report.*
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class AttendanceSummaryListActivity : BaseListActivity() {

    private var adapter: AttendanceSummaryAdapter? = null
    private var presenter: AttendanceSummaryPresenter? = null

    companion object {
        var CLASSIDKEY: String = "com.classroom.id"
    }

    private var classid: String = "0"
    private var progressdialog: ProgressDialog? = null

    open var month = (Calendar.getInstance().get(Calendar.MONTH) + 1)
    open var year = (Calendar.getInstance().get(Calendar.YEAR))
    private var rackMonthPicker: RackMonthPicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_social_grade_report)
        initviews()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.title_attendence))

        MainBus.getInstance().busObservable.ofType(AttendanceSummaryEvent::class.java).subscribe(eventobserver)
        presenter = AttendanceSummaryPresenter(this)

        classid = intent.getStringExtra(CLASSIDKEY)
        adapter = AttendanceSummaryAdapter(null, classid)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        progressdialog = DialogUtil.showProgressDialog(this@AttendanceSummaryListActivity)

        rackMonthPicker = RackMonthPicker(this)
                .setLocale(Locale.ENGLISH)
                .setSelectedMonth(month - 1)
                .setSelectedYear(year)
                .setColorTheme(R.color.darkgreen)
                .setPositiveButton({ month11, startDate, endDate, yearqq, monthLabel ->
                   /* println(month11)
                    println(startDate)
                    println(endDate)
                    println(yearqq)
                    println(monthLabel)*/
                    month = month11
                    year = yearqq
                    months_spinner.text = getString(R.string.currently_showing).replace("@", monthLabel)
                    getData()
                })
                .setNegativeButton({ dialog ->
                    dialog.dismiss()
                })


        val cal = Calendar.getInstance()
        val month_date = SimpleDateFormat("MMM", Locale.getDefault())
        //val monthnum = 5
        cal.set(Calendar.MONTH, month - 1)
        val month_name = month_date.format(cal.time)

        Log.e("", "" + month_name)
        //  months_spinner.text=month_name+", "+year
        months_spinner.text = getString(R.string.currently_showing).replace("@", month_name + ", " + year)

        recyclerView.setOnClickListener({

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.attendance_reports, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.menu_item_atten) {
            rackMonthPicker!!.show()

        }
        return true
    }


    /* private val onItemListener=object: AdapterView.OnItemSelectedListener{
         override fun onNothingSelected(parent: AdapterView<*>?) {
         }

         override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
             year=years_spinner.selectedItem.toString()
             getData()
         }
     }*/

    /*private val onItemListenermonth=object: AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            month=(months_spinner.selectedItemPosition+1).toString()
            getData()
        }
    }*/

    override fun onRefresh() {
        super.onRefresh()
        if (NetworkHelper.isOnline(this@AttendanceSummaryListActivity)) {
            if (adapter!!.itemCount == 0) showLoading()

            val text = (if (month < 10) "0" else "") + month

            presenter!!.refershData(classid, text, year.toString())
        } else
            Emit(AttendanceSummaryEvent(Event.NO_INTERNET))
    }

    private val eventobserver = object : Observer<AttendanceSummaryEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: AttendanceSummaryEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            if (progressdialog!!.isShowing) progressdialog?.dismiss()
            when (event.event) {
                Event.RESULT -> {
                    adapter!!.addItems(true, event.results)
                    showResults()
                }
                Event.NO_RESULT -> {
                    showNoResults()
                }
                Event.NO_INTERNET -> {
                    showNoInternet()
                }
                Event.SERVER_ERROR -> showServerError()
                Event.ERROR -> showError(event.error)
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

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        showLoading()
        presenter!!.getdata(classid, month.toString(), year.toString())

    }


}
