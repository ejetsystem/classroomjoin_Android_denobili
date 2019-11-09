package com.classroomjoin.app.forgotPasswordPage

import android.content.Context
import com.classroomjoin.app.helper_utils.Interactor

class ForgotPasswordInteractor

internal constructor(context: Context)
    : Interactor(context) {

    fun postdata(forgotPasswordModel: String, listener: ForgotPasswordApiManager.ForgotPasswordRequestListener,context: Context?) {
        val manager = ForgotPasswordApiManager()
        manager.postdata(forgotPasswordModel, apiInterface, listener,context)

    }


}
