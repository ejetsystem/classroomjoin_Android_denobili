package com.denobili.app.attendanceSummaryPage

import android.content.Context
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger


class AttendanceSummaryPresenter(context: Context) : Presenter(), AnkoLogger {

    private val interactor: AttendanceSummaryViewModel = AttendanceSummaryViewModel(context, this)

    fun setData(displayItem: ArrayList<DisplayItem>) {
        if (displayItem.isEmpty())
            Emit(AttendanceSummaryEvent(Event.NO_RESULT))
        else Emit(AttendanceSummaryEvent(displayItem))
    }

    fun setError(error: String) {
        Emit(AttendanceSummaryEvent(Event.ERROR, error))
    }

    fun serverError() {
        Emit(AttendanceSummaryEvent(Event.SERVER_ERROR))
    }

    fun getdata(classid: String, month: String, year: String) {
        interactor.getData(classid, month, year)
    }

    fun refershData(classid: String, month: String, year: String) {
        interactor.refreshdata(classid, month, year)
    }


}