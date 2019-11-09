package com.classroomjoin.app.studentallEventsPage

import android.content.Context
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter



class All_events_presenter(context: Context): Presenter() {

    private val interactor: AllEventInteractor

    init {
        interactor = AllEventInteractor(context, this)
    }

    fun setData(displayItem: ArrayList<DisplayItem>) {
        if (displayItem.isEmpty())
            Emit(AllStudentEvent(Event.NO_RESULT))
        else Emit(AllStudentEvent(displayItem))
    }

    fun setError(error: String) {
        Emit(AllStudentEvent(Event.ERROR, error))
    }

    fun serverError() {
        Emit(AllStudentEvent(Event.SERVER_ERROR))
    }

    fun getdata(type: Int) {
        interactor.getData()
    }



    fun  refreshdata() {
           interactor.refreshdata()
    }



}



