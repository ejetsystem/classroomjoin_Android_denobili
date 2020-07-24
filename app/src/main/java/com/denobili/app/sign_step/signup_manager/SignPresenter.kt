package com.denobili.app.sign_step.signup_manager

import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter
import com.denobili.app.signUpPage.SignUpEvent

class SignPresenter (private val context: Context): Presenter() {

    private val interactor: SignInteractor

    init {
        interactor= SignInteractor(context,this)
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