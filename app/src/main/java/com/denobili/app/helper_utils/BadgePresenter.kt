package com.denobili.app.helper_utils

import android.content.Context
import com.denobili.app.R


class BadgePresenter (private val context: Context): Presenter() {

    private val interactor: BadgeInteractor

    init {
        interactor= BadgeInteractor(context,this)
    }
    fun serverError() {
        Emit(BadgeFetch(Event.SERVER_ERROR, context.getString(R.string.some_error)))
    }

    fun postsuccess() {
        Emit(BadgeFetch(Event.POST_SUCCESS))
    }



    fun  getdata() {
        interactor.getdata()
    }

    fun postStudentdetails(student: BadgeCountModel){
        Emit(BadgeFetch(Event.RESULT, student))
    }

    fun noresult(error_message: String){
        Emit(BadgeFetch(Event.NO_RESULT, error_message))
    }


   

    fun postDeleteditem() {
        Emit(BadgeFetch(Event.DELETED_ITEM))
    }

    fun  postDeletionFailure(error_message: String) {
        Emit(BadgeFetch(Event.DELETION_FAILURE, error_message))

    }

    fun isAdmin()=interactor.isAdmin

}