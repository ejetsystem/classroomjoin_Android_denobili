package com.denobili.app.classEditPage

import android.content.Context
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger


class EditClassPresenter(context: Context) : Presenter(), AnkoLogger {

    private val interactor: EditClassInteractor = EditClassInteractor(context, this)


    fun postsuccess() {
        Emit(EditClassEvent(Event.POST_SUCCESS))
    }

    fun postfailure(error: String) {
        Emit(EditClassEvent(Event.POST_FAILURE))
    }

    fun setError(error: String) {
        Emit(EditClassEvent(Event.ERROR, error))
    }

    fun serverError() {
        Emit(EditClassEvent(Event.SERVER_ERROR))
    }


    fun postdata(addClassModel: AddClassModel) {
        interactor.addClass(addClassModel)
    }

    fun deleteclass(class_id: String?) {
        interactor.deleteclass(class_id)
    }

    fun deleteditem() {
        Emit(EditClassEvent(Event.DELETED_ITEM))
    }

    fun deletionFailure(error: String) {
        Emit(EditClassEvent(Event.DELETION_FAILURE, error))
    }


}