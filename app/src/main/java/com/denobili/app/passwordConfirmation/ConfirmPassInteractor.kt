package com.denobili.app.passwordConfirmation

import android.content.Context
import com.denobili.app.helper_utils.Interactor

class ConfirmPassInteractor
    internal constructor(context: Context)
        : Interactor(context) {

    fun postdata(otPverifyModel: ConfirmPassModel,listener: ConfirmPassApiManager.ConfirmPassRequestListener,context: Context?){
        val manager= ConfirmPassApiManager()
        manager.postdata(otPverifyModel,apiInterface,listener,context)

    }





}
