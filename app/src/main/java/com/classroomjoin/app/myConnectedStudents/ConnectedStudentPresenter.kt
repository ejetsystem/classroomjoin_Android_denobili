package com.classroomjoin.app.myConnectedStudents

import android.content.Context
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter


class ConnectedStudentPresenter(context: Context) : Presenter() {

    private val interactor: ConnectedStudentInteractor

    init {
        interactor = ConnectedStudentInteractor(context, this)
    }

    fun setData(displayItem: ArrayList<DisplayItem>) {
        if (displayItem.isEmpty())
            Emit(ConnectedStudentEvent(Event.NO_RESULT))
        else Emit(ConnectedStudentEvent(displayItem))
    }

    fun setError(error: String) {
        Emit(ConnectedStudentEvent(Event.ERROR, error))
    }

    fun serverError() {
        Emit(ConnectedStudentEvent(Event.SERVER_ERROR))
    }

    fun getdata() {
        interactor.getData1()
    }

    fun deleteitem(studentid: String) {
        interactor.deleteStudent(studentid)
    }

    fun postDeleteditem() {
        Emit(ConnectedStudentEvent(Event.DELETED_ITEM))
    }

    fun postDeletionFailure(error_message: String) {
        Emit(ConnectedStudentEvent(Event.DELETION_FAILURE, error_message))

    }

    fun register(model: MyConnectedStudentModel) {
        interactor.register(model)
    }


}