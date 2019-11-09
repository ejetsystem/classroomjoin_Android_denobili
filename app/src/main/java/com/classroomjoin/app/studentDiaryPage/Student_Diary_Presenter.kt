package com.classroomjoin.app.studentDiaryPage

import android.content.Context
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error


class Student_Diary_Presenter(context: Context): Presenter(),AnkoLogger {

    private val interactor: StudentDiaryInteractor

    init {
        interactor = StudentDiaryInteractor(context, this)
    }

    fun setData(displayItem: ArrayList<DisplayItem>) {
        if (displayItem.isEmpty())
            Emit(StudentDiaryEvents(Event.NO_RESULT))
        else {
            error{displayItem.size.toString()}
            Emit(StudentDiaryEvents(Event.RESULT, displayItem))
        }
    }

    fun setError(error: String) {
        Emit(StudentDiaryEvents(Event.ERROR, error))
    }

    fun serverError() {
        Emit(StudentDiaryEvents(Event.SERVER_ERROR))
    }



    fun getdata(type:String){
        interactor.getData(type)
    }

    fun  refreshdata(type: String) {
        interactor.refreshdata(type)
    }

}



