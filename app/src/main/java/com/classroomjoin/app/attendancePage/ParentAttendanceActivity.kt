package com.classroomjoin.app.attendancePage

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.EventDecorator
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.DisplayItem
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener
import kotlinx.android.synthetic.main.activity_attendence_card_parent.*
import org.jetbrains.anko.imageResource
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ParentAttendanceActivity : LocalizationActivity(), OnMonthChangedListener {


    companion object StudentIDKEy {
        const val studentidkey: String = "com.studentid.key"
    }

    var month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1
    var year: String = Calendar.getInstance().get(Calendar.YEAR).toString()
    var studentid: String? = "1"
    private var viewmodel: AttendanceListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendence_card_parent)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.title_attendence))

        // calendarView.topbarVisible=false
        // calendarView.isPagingEnabled=false

        calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_NONE

        viewmodel = AttendanceListViewModel(this@ParentAttendanceActivity)

        if (intent.hasExtra(studentidkey)) {
            studentid = intent.getStringExtra(studentidkey)
        }

        // months_spinner.setSelection(Calendar.getInstance().get(Calendar.MONTH))

        //years_spinner.onItemSelectedListener=onItemListener

        // months_spinner.onItemSelectedListener=onItemListenermonth

        getData(month, year)

        calendarView.setOnMonthChangedListener(this)

        /*val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)*/

    }

    override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {

        //System.out.println("Data--->"+"onMonthChanged details-->"+studentid+"-"+date!!.getMonth()+"--"+date!!.getYear())
        calendarView.removeDecorators()
        getData(date!!.month + 1, date.year.toString())
    }

    /*private val onItemListener=object: AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            year=years_spinner.selectedItem.toString()
            //getData()
        }
    }

    private val onItemListenermonth=object: AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            month=months_spinner.selectedItemPosition+1
           // getData()
        }
    }*/

    private fun showError(error: String?) {
        onerror()
        attendance_error_image.imageResource = R.drawable.ic_error_black_24dp
        attendance_error_text.text = error
    }

    private fun showServerError() {
        onerror()
        attendance_error_image.imageResource = R.drawable.ic_error_black_24dp
        attendance_error_text.text = getString(R.string.serverError)
    }

    private fun showNoInternet() {
        onerror()
        attendance_error_image.imageResource = R.drawable.ic_cloud_off_black_24dp
        attendance_error_text.text = getString(R.string.noInternet)
    }

    private fun showLoading() {
        calendarView.visibility = View.GONE
        attendance_error_image.visibility = View.GONE
        attendance_error_text.visibility = View.GONE
        attendance_contentLoading.show()
        attendance_contentLoading.visibility = View.VISIBLE
    }

    private fun getData(month1: Int, year1: String) {
        if (NetworkHelper.isOnline(this@ParentAttendanceActivity)) {
            showLoading()
            viewmodel?.getData(studentid!!, month1, year1)!!.subscribe(Observer)
            // System.out.println("Data--->"+"getData details-->"+studentid+"-"+month1+"--"+year1)

        } else
            showNoInternet()
    }


    private val Observer: Observer<AttendanceApiResponse>
        get() = object : Observer<AttendanceApiResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                showServerError()
                e?.printStackTrace()
            }

            override fun onNext(t: AttendanceApiResponse?) {
                if (t?.status == "Success") {
                    val list = java.util.ArrayList<DisplayItem>()

                    //if(t.data!!.size>0) {
                        calendarView.visibility=View.VISIBLE
                        list.addAll(t.data!!)
                        showResults(list)
                  /*  }else{
                        calendarView.visibility=View.GONE
                        showError(getString(R.string.no_attendance_added))}*/
                } else showError(t!!.error_message)
            }
        }

    private fun onerror() {
        calendarView.visibility = View.GONE
        attendance_error_image.visibility = View.VISIBLE
        attendance_error_text.visibility = View.VISIBLE
        attendance_contentLoading.hide()
        attendance_contentLoading.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    private fun showResults(displayItem: List<DisplayItem>) {
        calendarView.visibility = View.VISIBLE
        attendance_error_image.visibility = View.GONE
        attendance_error_text.visibility = View.GONE
        attendance_contentLoading.visibility = View.GONE
        calendarView.addDecorator(EventDecorator(ContextCompat.getDrawable(this@ParentAttendanceActivity, R.drawable.circle_green), getCalenderMonth(displayItem)))
        calendarView.addDecorator(EventDecorator(ContextCompat.getDrawable(this@ParentAttendanceActivity, R.drawable.circle_red), getCalenderAbsent(displayItem)))
    }

    private fun getCalenderMonth(displayItem: List<DisplayItem>): ArrayList<CalendarDay> {
        val daysList: ArrayList<CalendarDay>? = ArrayList()
        displayItem.forEachIndexed { index, displayItem ->
            val attendencedate: AttendanceData = displayItem as AttendanceData
            if (attendencedate.attendance == 1) {
                val string = attendencedate.date
                val pattern = "yyyy-MM-dd"
                val date = SimpleDateFormat(pattern, Locale.US).parse(string)
                daysList?.add(CalendarDay.from(date))
            }
        }
        return daysList!!
    }

    private fun getCalenderAbsent(displayItem: List<DisplayItem>): ArrayList<CalendarDay> {
        val daysList: ArrayList<CalendarDay>? = ArrayList()
        displayItem.forEachIndexed { index, displayItem ->
            val attendencedate: AttendanceData = displayItem as AttendanceData
            if (attendencedate.attendance == 0) {
                val string = attendencedate.date
                val pattern = "yyyy-MM-dd"
                val date = SimpleDateFormat(pattern, Locale.US).parse(string)
                daysList?.add(CalendarDay.from(date))
            }
        }
        return daysList!!
    }


}
