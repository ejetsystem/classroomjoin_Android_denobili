package com.denobili.app.attendancePage

import android.content.Context
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.Interactor
import rx.Observable
import java.text.DecimalFormat


class AttendanceListViewModel
internal constructor(context: Context)
    : Interactor(context) {

    fun getData(studentid: String, month: Int, year: String): Observable<AttendanceApiResponse> {
        val formatter = DecimalFormat("00")
        val month_format = formatter.format(month)
        return getObservable(studentid, month_format, year)
    }

    private fun getObservable(studentid: String, month: String, year: String): Observable<AttendanceApiResponse> =
            apiInterface.getAttendance(accesstoken, studentid, month, year).compose(RxFunctions.applySchedulers())


}
