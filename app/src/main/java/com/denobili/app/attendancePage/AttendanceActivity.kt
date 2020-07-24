package com.denobili.app.attendancePage

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.denobili.app.R
import com.denobili.app.helper.EventDecorator
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.DisplayItem
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener
import kotlinx.android.synthetic.main.activity_attendence_card.*
import org.jetbrains.anko.imageResource
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AttendanceActivity : LocalizationActivity(), OnMonthChangedListener {

    companion object StudentIDKEy {
        const val studentidkey: String = "com.studentid.key"
    }

    var month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1
    var year: String = Calendar.getInstance().get(Calendar.YEAR).toString()
    var studentid: String? = "1"
    var viewmodel: AttendanceListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendence_card)

        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.title_attendence))

        viewmodel = AttendanceListViewModel(this@AttendanceActivity)

        if (intent.hasExtra(studentidkey)) {
            studentid = intent.getStringExtra(studentidkey)
        }

        getData(month, year)

        calendarView_new.setOnMonthChangedListener(this)


    }

    override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {

        //System.out.println("Data--->"+"onMonthChanged details-->"+studentid+"-"+date!!.getMonth()+"--"+date!!.getYear())
        calendarView_new.removeDecorators()
        getData(date!!.month + 1, date.year.toString())
    }

    /* private val onItemListener=object: AdapterView.OnItemSelectedListener{
         override fun onNothingSelected(parent: AdapterView<*>?) {
         }

         override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
             year=years_spinner.selectedItem.toString()
             getData()
         }
     }

     private val onItemListenermonth=object: AdapterView.OnItemSelectedListener{
         override fun onNothingSelected(parent: AdapterView<*>?) {
         }

         override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
             month=months_spinner.selectedItemPosition+1
             getData()
         }
     }*/


    private fun showError(error: String?) {
        onerror()
        attendance_error_image_new.imageResource = R.drawable.ic_error_black_24dp
        attendance_error_text_new.text = error
    }

    private fun showServerError() {
        onerror()
        attendance_error_image_new.imageResource = R.drawable.ic_error_black_24dp
        attendance_error_text_new.text = getString(R.string.serverError)
    }

    private fun showNoInternet() {
        onerror()
        attendance_error_image_new.imageResource = R.drawable.ic_cloud_off_black_24dp
        attendance_error_text_new.text = getString(R.string.noInternet)
    }

    private fun showLoading() {
        calendarView_new.visibility = View.GONE
        attendance_error_image_new.visibility = View.GONE
        attendance_error_text_new.visibility = View.GONE
        attendance_contentLoading_new.show()
        attendance_contentLoading_new.visibility = View.VISIBLE
    }

    private fun getData(month1: Int, year1: String) {
        if (NetworkHelper.isOnline(this@AttendanceActivity)) {
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
                    list.addAll(t.data!!)
                    showResults(list)
                } else showError(t!!.error_message)
            }
        }

    private fun onerror() {
        calendarView_new.visibility = View.GONE
        attendance_error_image_new.visibility = View.VISIBLE
        attendance_error_text_new.visibility = View.VISIBLE
        attendance_contentLoading_new.hide()
        attendance_contentLoading_new.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    private fun showResults(displayItem: List<DisplayItem>) {
        calendarView_new.visibility = View.VISIBLE
        attendance_error_image_new.visibility = View.GONE
        attendance_error_text_new.visibility = View.GONE
        attendance_contentLoading_new.visibility = View.GONE
        calendarView_new.addDecorator(EventDecorator(ContextCompat.getDrawable(this@AttendanceActivity, R.drawable.circle_green), getCalenderMonth(displayItem)))
        calendarView_new.addDecorator(EventDecorator(ContextCompat.getDrawable(this@AttendanceActivity, R.drawable.circle_red), getCalenderAbsent(displayItem)))
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
