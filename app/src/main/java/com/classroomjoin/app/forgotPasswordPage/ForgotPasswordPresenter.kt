package com.classroomjoin.app.forgotPasswordPage

import android.content.Context
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger


class ForgotPasswordPresenter(context11: Context) : Presenter(), AnkoLogger, ForgotPasswordApiManager.ForgotPasswordRequestListener {


    private var context: Context? = null

    override fun onRequestSuccesful() {
        Emit(ForgotPasswordEvent(Event.POST_SUCCESS))
    }
    override fun onError(error: String) {
        Emit(ForgotPasswordEvent(Event.ERROR, error))
    }


    override fun onRequestError(error: String) {
        Emit(ForgotPasswordEvent(Event.POST_FAILURE, error))
    }

    private val interactor: ForgotPasswordInteractor

    init {
        context = context11

        interactor = ForgotPasswordInteractor(context11)
    }

    fun postdata(model: String) {
        interactor.postdata(model, this,context)
    }

}