package com.classroomjoin.app.loginPage.signupGoogle

import android.content.Context
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger

class SigninGooglePresenter(context11: Context) : Presenter(), AnkoLogger, SignInGoogleApiManager.SignInGoogleRequestListener {

    private var context: Context? = null


    override fun onRequestSuccesful() {
        Emit(SignInGoogleEvent(Event.POST_SUCCESS))

    }

    override fun onError(error: String) {
        Emit(SignInGoogleEvent(Event.ERROR, error))
    }


    override fun onRequestError(error: String) {
        Emit(SignInGoogleEvent(Event.POST_FAILURE, error))
    }

    private val interactor: SignInGoogleInteractor

    init {
        context = context11
        interactor = SignInGoogleInteractor(context11)
    }

    fun postdata(otPverifyModel: SignInGoogleModel) {
        interactor.postdata(otPverifyModel, this, context)
    }

}