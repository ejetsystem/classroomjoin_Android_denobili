package com.denobili.app.sign_step.sign_mobile_manager

import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter
import com.denobili.app.signUpPage.SignUpEvent
import com.denobili.app.sign_step.signup_manager.SignupMobileModel

class SignMobPresenter (private val context: Context): Presenter() {

    private val interactor: SignMobInteractor

    init {
        interactor= SignMobInteractor(context,this)
    }

    fun postdata(model: SignupMobileModel) {
        interactor.postData(model)
    }

    fun serverError() {
        Emit(SignUpEvent(Event.SERVER_ERROR, context.getString(R.string.some_error)))
    }

    fun postsuccess() {
        Emit(SignUpEvent(Event.POST_SUCCESS))
    }

    fun  setError(error_message: String) {
        Emit(SignUpEvent(Event.POST_FAILURE, error_message))
    }

}