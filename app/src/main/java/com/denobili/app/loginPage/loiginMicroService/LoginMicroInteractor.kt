package com.denobili.app.loginPage.loiginMicroService

import android.content.Context
import com.denobili.app.helper_utils.Interactor

class LoginMicroInteractor
internal constructor(context: Context)
    : Interactor(context) {

    fun postdata(otPverifyModel: String, listener: LoginMicroApiManager.LoginMicroRequestListener, context: Context?) {
        val manager = LoginMicroApiManager()
        manager.postdata(otPverifyModel, apiInterface, listener, context)
    }
}