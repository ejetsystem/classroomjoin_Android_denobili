package com.classroomjoin.app.studentInboxPage

import android.content.Context
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter


class StudentInboxEventPresenter(context: Context,type: String): Presenter() {

    private val interactor: StudentInboxEventInteractor

    init {
        interactor = StudentInboxEventInteractor(context, this,type)
    }

    fun setData(displayItem: ArrayList<DisplayItem>) {
        if (displayItem.isEmpty())
            Emit(StudentInboxEvent(Event.NO_RESULT))
        else Emit(StudentInboxEvent(displayItem))
    }

    fun setError(error: String) {
        Emit(StudentInboxEvent(Event.ERROR, error))
    }

    fun serverError() {
        Emit(StudentInboxEvent(Event.SERVER_ERROR))
    }

    fun getdata(type: Int) {
        interactor.getData()
    }

    fun  refreshdata( ) {
          interactor.refreshdata()
    }

    fun refreshData(){

    }

}



