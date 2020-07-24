package com.denobili.app.studentSocialGradeReportPage

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.BaseListActivity
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.monthPickerLibrary.DateMonthDialogListener
import com.denobili.app.monthPickerLibrary.OnCancelMonthDialogListener
import com.denobili.app.monthPickerLibrary.RackMonthPicker
import kotlinx.android.synthetic.main.activity_student_social_grade_report.*
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*

class StudentSocialGradeReportActivity : BaseListActivity() {

    private var adapter: StudentSocialGradeReportAdapter? = null
    private var presenter: StudentSocialGradePresenter? = null
    private var progressdialog: ProgressDialog? = null

   // var month = "0"
    //var year = "2017"

    open var month = (Calendar.getInstance().get(Calendar.MONTH) + 1)
    open var year = (Calendar.getInstance().get(Calendar.YEAR))
    var rackMonthPicker: RackMonthPicker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_main_ad_social_grade)
        initviews()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.social_grade_report))

        adapter = StudentSocialGradeReportAdapter(null)
        recyclerView.adapter = adapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.layoutManager = LinearLayoutManager(this@StudentSocialGradeReportActivity)
        recyclerView.isNestedScrollingEnabled = false
        MainBus.getInstance().busObservable.ofType(SocialGradeListEvent::class.java).subscribe(eventobserver)
        presenter = StudentSocialGradePresenter(this)
        progressdialog = DialogUtil.showProgressDialog(this@StudentSocialGradeReportActivity)


        //months_spinner.setSelection(Calendar.getInstance().get(Calendar.MONTH))

        //years_spinner.onItemSelectedListener = onItemListener

        //months_spinner.onItemSelectedListener = onItemListenermonth


       /* val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)*/

        rackMonthPicker = RackMonthPicker(this)
                .setLocale(Locale.getDefault())
                .setSelectedMonth(month - 1)
                .setSelectedYear(year)
                .setColorTheme(R.color.darkgreen)
                .setPositiveButton(DateMonthDialogListener { month11, startDate, endDate, yearqq, monthLabel ->
                    println(month11)
                    println(startDate)
                    println(endDate)
                    println(yearqq)
                    println(monthLabel)
                    month = month11
                    year = yearqq
                    months_spinner.text = getString(R.string.currently_showing).replace("@", monthLabel)
                    getData()
                })
                .setNegativeButton(OnCancelMonthDialogListener { dialog ->
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



    }

    /*private val onItemListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            year = years_spinner.selectedItem.toString()
            getData()
        }
    }

    private val onItemListenermonth = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            month = (months_spinner.selectedItemPosition + 1).toString()
            getData()
        }
    }
*/
    override fun onRefresh() {
        super.onRefresh()
        getData()
    }

    private val eventobserver = object : Observer<SocialGradeListEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: SocialGradeListEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            when (event.event) {
                Event.RESULT -> {
                    adapter!!.addItems(true, event.results)
                    showResults()
                }
                Event.NO_RESULT ->
                    showNoResults(getString(R.string.no_social_grades))
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
        if (NetworkHelper.isOnline(this)) {
            showLoading()
            val text = (if (month < 10) "0" else "") + month
            presenter!!.getdata(text, year.toString())
        } else Emit(SocialGradeListEvent(getString(R.string.noInternet)))
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

}
