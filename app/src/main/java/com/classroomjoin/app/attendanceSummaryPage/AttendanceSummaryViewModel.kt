package com.classroomjoin.app.attendanceSummaryPage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.saveAll
import org.jetbrains.anko.AnkoLogger
import rx.Observer


class AttendanceSummaryViewModel
internal constructor(context: Context, private val presenter: AttendanceSummaryPresenter)
    : Interactor(context), AnkoLogger {


    fun getData(classid: String, month: String, year: String) {
        if (getDatafromDisk(classid, month, year).isEmpty()) getDatafromNetwork(classid, month, year)
        else {
            val list = ArrayList<DisplayItem>()
            list.addAll(getDatafromDisk(classid, month, year))
            presenter.setData(list)
        }
    }

    private fun getDatafromNetwork(classid: String, month: String, year: String) {
        apiInterface.getAttendanceSummary(accesstoken, account_id, classid, month, year)
                .compose(RxFunctions.applySchedulers())
                .subscribe(Observer(classid, month, year, account_id))

    }

    private fun getDatafromDisk(classid: String, month: String, year: String): List<AttendanceSummaryRow> {
        return AttendanceSummaryRow().query { realmQuery ->
            realmQuery.equalTo("month", month)
                    .equalTo("year", year)
                    .equalTo("class_id", classid.toInt())
                    .equalTo("account_id", account_id.toInt())
        }

    }

    private fun deleteDatafromDisk(classid: String, month: String, year: String) {
        AttendanceSummaryRow().delete { realmQuery ->
            realmQuery.equalTo("month", month)
                    .equalTo("year", year)
                    .equalTo("class_id", classid.toInt())
                    .equalTo("account_id", account_id.toInt())
        }

    }

    private fun Observer(classid: String, month: String, year: String, account_id: String): Observer<AttendanceSummaryResponse> {
        return object : Observer<AttendanceSummaryResponse> {
            override fun onNext(t: AttendanceSummaryResponse?) {
                if (t?.status == "Success") {
                    deleteDatafromDisk(classid, month, year)
                    val list = ArrayList<AttendanceSummaryRow>(t.data.size)
                    t.data.forEachIndexed { index, attendanceSummaryRow ->
                        attendanceSummaryRow.setQueryParams(classid = classid.toInt(), month = month, year = year, accountid = account_id.toInt())
                        list.add(attendanceSummaryRow)
                    }
                    list.saveAll()
                    val result = ArrayList<DisplayItem>()
                    result.addAll(getDatafromDisk(classid, month, year))
                    presenter.setData(result)
                }

            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
                presenter.serverError()
            }

        }
    }

    fun refreshdata(classid: String, month: String, year: String) {
        getDatafromNetwork(classid, month, year)
    }


}
