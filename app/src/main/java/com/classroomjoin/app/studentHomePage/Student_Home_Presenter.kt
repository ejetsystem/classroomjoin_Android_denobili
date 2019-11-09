package com.classroomjoin.app.studentHomePage

import android.content.Context
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import com.classroomjoin.app.studentallEventsPage.StudentEvents


class Student_Home_Presenter (context: Context): Presenter() {

    private val interactor: StudentEventInteractor

    init {
        interactor = StudentEventInteractor(context, this)
    }

    fun setData(displayItem: ArrayList<DisplayItem>) {
        if (displayItem.isEmpty())
            Emit(StudentEvents(Event.NO_RESULT))
        else Emit(StudentEvents(Event.RESULT, displayItem))
    }

    fun setError(error: String) {
        Emit(StudentEvents(Event.ERROR, error))
    }

    fun serverError() {
        Emit(StudentEvents(Event.SERVER_ERROR))
    }

    fun getdata(studentid: Int,eventid:Int) {
        interactor.getData(studentid,eventid)
    }

}



