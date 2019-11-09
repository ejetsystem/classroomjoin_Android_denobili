package com.classroomjoin.app.studentListingPage

import android.content.Context
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger


class StudentListingPresenter(private val context: Context) : Presenter(), AnkoLogger {

    private val interactor: StudentListingInteractor

    init {
        interactor = StudentListingInteractor(context, this)

    }

    fun setData(displayItem: ArrayList<DisplayItem>) {
        if (displayItem.isEmpty())
            Emit(StudentListingEvent(Event.NO_RESULT))
        else {
            Emit(StudentListingEvent(displayItem))
        }
    }

    fun setError(error: String) {
        Emit(StudentListingEvent(Event.ERROR, error))
    }

    fun serverError() {
        Emit(StudentListingEvent(Event.SERVER_ERROR))
    }

    fun getdata(classid: String) {
        interactor.getData(classid)
    }

    fun isAdmin() = interactor.isAdmin

    fun refreshdata(classid: String) {
        if (NetworkHelper.isOnline(context.applicationContext))
            interactor.refresh(classid)
        else Emit(StudentListingEvent(Event.NO_INTERNET))
    }


    fun postsuccess() {
        Emit(StudentListingEvent(Event.POST_SUCCESS))
    }

    fun postfailure(message: String) {
        Emit(StudentListingEvent(Event.POST_FAILURE, message))
    }

}