package com.classroomjoin.app.signUpPage.getOTPsignup

import android.content.Context
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger

class SignupOTPPresenter(context11: Context) : Presenter(), AnkoLogger, SignupOTPApiManager.SignupOTPRequestListener {

    private var context: Context? = null


    override fun onRequestSuccesful() {
        Emit(SignupOTPEvent(Event.POST_SUCCESS))

    }

    override fun onError(error: String) {
        Emit(SignupOTPEvent(Event.ERROR, error))
    }


    override fun onRequestError(error: String) {
        Emit(SignupOTPEvent(Event.POST_FAILURE, error))
    }

    private val interactor: SignupOTPInteractor

    init {
        context = context11
        interactor = SignupOTPInteractor(context11)
    }

    fun postdata(otPverifyModel: SignupOTPModel) {
        interactor.postdata(otPverifyModel, this, context)
    }

}