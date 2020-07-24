package com.denobili.app.signUpPage.getOTPsignup

import android.content.Context
import com.denobili.app.helper_utils.Interactor

class SignupOTPInteractor


    internal constructor(context: Context)
        : Interactor(context) {

    fun postdata(otPverifyModel: SignupOTPModel, listener: SignupOTPApiManager.SignupOTPRequestListener,context: Context?){
        val manager= SignupOTPApiManager()
        manager.postdata(otPverifyModel,apiInterface,listener,context)

    }





}