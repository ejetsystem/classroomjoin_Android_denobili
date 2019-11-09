package com.classroomjoin.app.manageAccountsPackage

import android.content.Context
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import com.classroomjoin.app.loginPage.Userdata


class ManageAccountPresenter(context: Context) : Presenter() {

    private val interactor: ManageAccountsInteractor

    init {
        interactor = ManageAccountsInteractor(context, this)
    }

    fun setData(displayItem: ArrayList<DisplayItem>) {
        if (displayItem.isEmpty())
            Emit(ManageAccountsEvent(Event.NO_RESULT))
        else Emit(ManageAccountsEvent(displayItem))
    }

    fun setError(error: String) {
        Emit(ManageAccountsEvent(Event.ERROR, error))
    }

    fun serverError() {
        Emit(ManageAccountsEvent(Event.SERVER_ERROR))
    }

    fun getdata() {
        interactor.getData()
    }

    fun deleteitem(studentid: String) {
    }

    fun postDeleteditem() {
        Emit(ManageAccountsEvent(Event.DELETED_ITEM))
    }

    fun postDeletionFailure(error_message: String) {
        Emit(ManageAccountsEvent(Event.DELETION_FAILURE, error_message))

    }

    fun setUser(user: Userdata) {
        interactor.setUser(user)
    }


}