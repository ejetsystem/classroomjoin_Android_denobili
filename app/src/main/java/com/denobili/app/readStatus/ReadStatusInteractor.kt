package com.denobili.app.readStatus

import android.content.Context
import com.denobili.app.helper_utils.Interactor

class ReadStatusInteractor
internal constructor(context: Context)
    : Interactor(context) {

    fun postdata(otPverifyModel: ReadStatusModel, listener: ReadStatusApiManager.ReadStatusRequestListener, context: Context?) {
        val manager = ReadStatusApiManager()
        manager.postdata(accesstoken, otPverifyModel, apiInterface, listener, context)

    }


}