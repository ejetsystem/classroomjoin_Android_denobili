package com.classroomjoin.app.templatePage

import android.content.Context
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger


class TemplateSelectionPresenter(private var context: Context) : Presenter(), AnkoLogger {

    private val interactor: TemplateSelectionInteractor

    init {
        interactor = TemplateSelectionInteractor(context, this)
    }

    fun setData(displayItem: ArrayList<DisplayItem>) {
        if (displayItem.isEmpty()) {
            Emit(TemplateSelectionEvent(Event.NO_RESULT))
        } else Emit(TemplateSelectionEvent(displayItem))
    }

    fun setError(error: String) {
        Emit(TemplateSelectionEvent(Event.ERROR, error))
    }

    fun serverError() {
        Emit(TemplateSelectionEvent(Event.SERVER_ERROR))
    }

    fun getdata(typeid: Int) {
        interactor.getData(typeid)
    }

    fun refreshdata(typeid: Int) {
        if (NetworkHelper.isOnline(context))
            interactor.refresh(typeid)
        else Emit(TemplateSelectionEvent(Event.NO_INTERNET))
    }

}