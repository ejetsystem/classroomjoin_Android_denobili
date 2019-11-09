package com.classroomjoin.app.postAttendancePage

import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import com.classroomjoin.app.teacherOutboxPage.OutboxModel
import com.classroomjoin.app.teacherOutboxPage.StudentIdClass
import com.vicpin.krealmextensions.save


class AttendenceSelectionPresenter(private var context: Context) : Presenter() {

    private val interactor: AttendenceSelectionInteractor

    init {
        interactor = AttendenceSelectionInteractor(context, this)
    }

    fun setData(displayItem: ArrayList<DisplayItem>) {
        if (displayItem.isEmpty())
            Emit(AttendenceSelectionEvent(Event.NO_RESULT))
        else Emit(AttendenceSelectionEvent(displayItem))
    }

    fun setError(error: String) {
        Emit(AttendenceSelectionEvent(Event.ERROR, error))
    }

    fun serverError() {
        Emit(AttendenceSelectionEvent(Event.SERVER_ERROR))
    }

    fun getdata(queryid: String) {
        interactor.getData(queryid)
    }

    fun postdata(postAttendence: PostAttendence) {
        if (NetworkHelper.isOnline(context))
            interactor.postdata(postAttendence)
        else {
            var model = OutboxModel(interactor.account_id, interactor.realm)
            model.feature = 6
            postAttendence.roll_no_list!!.forEachIndexed { index, s ->
                model.pre_list?.add(index, StudentIdClass(Integer.parseInt(s)))
            }
            postAttendence.roll_no_list_absent!!.forEachIndexed { index, s ->
                model.abs_list?.add(index, StudentIdClass(Integer.parseInt(s)))
            }
            model.class_id = postAttendence.class_id
            model.attendance_date = postAttendence.date
            model.message = context.getString(R.string.class_attendance) + postAttendence.date
            model.save()
            postOutbox()

        }
    }

    private fun postOutbox() {
        Emit(AttendenceSelectionEvent(Event.SAVED_TO_OUTBOX))
    }

    fun postfailure(error: String) {
        Emit(AttendenceSelectionEvent(Event.POST_FAILURE, error))
    }

    fun postSuccess() {
        Emit(AttendenceSelectionEvent(Event.POST_SUCCESS))
    }

    fun refreshdata(classid: String) {
        if (NetworkHelper.isOnline(context))
            interactor.refresh(classid)
        else Emit(AttendenceSelectionEvent(Event.NO_INTERNET))
    }

}