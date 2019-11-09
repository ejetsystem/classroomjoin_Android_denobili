package com.classroomjoin.app.loginPage.loiginMicroService

import android.content.Context
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger

class LoginMicroPresenter(context11: Context) : Presenter(), AnkoLogger, LoginMicroApiManager.LoginMicroRequestListener {

    private var context: Context? = null


    override fun onRequestSuccesful() {
        Emit(LoginMicroEvent(Event.POST_SUCCESS))

    }

    override fun onError(error: String) {
        Emit(LoginMicroEvent(Event.ERROR, error))
    }


    override fun onRequestError(error: String) {
        Emit(LoginMicroEvent(Event.POST_FAILURE, error))
    }

    private val interactor: LoginMicroInteractor

    init {
        context = context11
        interactor = LoginMicroInteractor(context11)
    }

    fun postdata(otPverifyModel: String) {
        interactor.postdata(otPverifyModel, this, context)
    }

}