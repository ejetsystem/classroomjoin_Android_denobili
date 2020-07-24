package com.denobili.app.loginPage.signupGoogle

import android.content.Context
import com.denobili.app.helper_utils.Interactor

class SignInGoogleInteractor
internal constructor(context: Context)
    : Interactor(context) {

    fun postdata(otPverifyModel: SignInGoogleModel, listener: SignInGoogleApiManager.SignInGoogleRequestListener, context: Context?) {
        val manager = SignInGoogleApiManager()
        manager.postdata(otPverifyModel, apiInterface, listener, context)
    }
}