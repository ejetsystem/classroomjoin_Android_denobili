package com.denobili.app.studentDiaryPage

import android.content.Context
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error


class Student_Diary_Presenter(context: Context): Presenter(),AnkoLogger {

    private val interactor: StudentDiaryInteractor

    init {
        interactor = StudentDiaryInteractor(context, this)
    }

    fun setData(displayItem: ArrayList<DisplayItem>,totalPage:Int,totalElements:Int) {
        if (displayItem.isEmpty())
            Emit(StudentDiaryEvents(Event.NO_RESULT))
        else {
            error{displayItem.size.toString()}
            Emit(StudentDiaryEvents(Event.RESULT, displayItem,totalPage,totalElements))
        }
    }

    fun setError(error: String) {
        Emit(StudentDiaryEvents(Event.ERROR, error))
    }

    fun serverError() {
        Emit(StudentDiaryEvents(Event.SERVER_ERROR))
    }



    fun getdata(type:String,page:Int){
        interactor.getData(type,page)
    }

    fun  refreshdata(type: String,page: Int) {
        interactor.refreshdata(type,page)
    }

}



