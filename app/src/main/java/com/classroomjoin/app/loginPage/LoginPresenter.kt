package com.classroomjoin.app.loginPage

import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import com.classroomjoin.app.signUpPage.SignUpModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error


class LoginPresenter(context: Context) : Presenter(), AnkoLogger {

    private val interactor: LoginInteractor
    var context11: Context? = null

    init {
        context11 = context
        interactor = LoginInteractor(context, this)
    }

    fun postdata(loginModel: LoginModel, change_account: Boolean) {
        interactor.postData(loginModel, change_account)
    }


    fun serverError() {
        error { "server error called" }
        Emit(LoginEvent(Event.SERVER_ERROR, context11!!.getString(R.string.serverError)))
    }

    fun postsuccess() {
        Emit(LoginEvent(Event.POST_SUCCESS))
    }

    fun setError(error_message: String?) {
        error { "set error called" }
        Emit(LoginEvent(Event.POST_FAILURE, error_message))
    }

    fun postRegister(signUpModel: SignUpModel, change_account: Boolean) {
        interactor.postSignup(signUpModel, change_account)

    }

}