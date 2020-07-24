package com.denobili.app.studentInboxPage

import android.content.Context
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter


class StudentInboxEventPresenter(context: Context,type: String): Presenter() {

    private val interactor: StudentInboxEventInteractor

    init {
        interactor = StudentInboxEventInteractor(context, this,type)
    }

    fun setData(displayItem: ArrayList<DisplayItem>,totalPage:Int,totalElements:Int) {
        if (displayItem.isEmpty())
            Emit(StudentInboxEvent(Event.NO_RESULT))
        else Emit(StudentInboxEvent(displayItem,totalPage,totalElements))
    }

    fun setError(error: String) {
        Emit(StudentInboxEvent(Event.ERROR, error))
    }

    fun serverError() {
        Emit(StudentInboxEvent(Event.SERVER_ERROR))
    }

    fun getdata(type: Int,page: Int) {
        interactor.getData(page)
    }

    fun  refreshdata( page:Int) {
          interactor.refreshdata(page)
    }

    fun refreshData(){

    }

}



